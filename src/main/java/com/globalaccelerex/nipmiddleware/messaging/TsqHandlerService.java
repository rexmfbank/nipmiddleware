package com.globalaccelerex.nipmiddleware.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import com.globalaccelerex.nipmiddleware.http.HTTPHelpers;
import com.globalaccelerex.nipmiddleware.http.HTTPRestTemplate;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Txnstatusquerysingleitem;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.service.db.FundsTransferDbService;
import com.globalaccelerex.nipmiddleware.service.ws.NIPOutwardWS;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

@Service
public class TsqHandlerService {

    private final XmlUtil xmlUtil;

    private final NIPOutwardMapper nipOutwardMapper;

    private final NIPOutwardWS nipOutwardWS;

    private final HTTPRestTemplate hTTPRestTemplate;

    private final SSMUtil ssmUtil;

    private final FundsTransferDbService fundsTransferDbService;

    private final ClientDbService clientDbService;



    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    @Autowired
    public TsqHandlerService(XmlUtil xmlUtil, NIPOutwardMapper nipOutwardMapper, NIPOutwardWS nipOutwardWS,
                             HTTPRestTemplate hTTPRestTemplate, SSMUtil ssmUtil, FundsTransferDbService fundsTransferDbService,
                             ClientDbService clientDbService) {
        this.xmlUtil = xmlUtil;
        this.nipOutwardMapper = nipOutwardMapper;
        this.nipOutwardWS = nipOutwardWS;
        this.hTTPRestTemplate = hTTPRestTemplate;
        this.ssmUtil = ssmUtil;
        this.fundsTransferDbService = fundsTransferDbService;
        this.clientDbService = clientDbService;
    }

    public QueuePayload handleTsq(IMarker marker, QueuePayload queuePayload){
        FundsTransferEntity fundsTransferEntity = null;
        try{
            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(queuePayload.getSessionId());

            String tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            marker.setRequest(" clear tsqSingleItemRequestXmlString  to NIBSS ", tsqSingleItemRequestXmlString);

            final val encryptedTsqSingleItemRequestXmlString = ssmUtil.encryptRequest(tsqSingleItemRequestXmlString);

            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedTsqSingleItemRequestXmlString);

            final val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(marker, txnstatusquerysingleitem);
            final val tsqSingleItemResponseXmlString = ssmUtil.decryptResponse(txnStatusQuerySingleItemResponse.getReturn());
            marker.setResponse(" Clear  Response from NIPOutwardWS TSQ "+ tsqSingleItemResponseXmlString);

            final val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);
            marker.info(" TsqSingleItemResponseVO " + tsqSingleItemResponseVO.toString());

            //update db
            fundsTransferEntity = fundsTransferDbService.updateFTResponseCode(tsqSingleItemResponseVO.getSessionId(), tsqSingleItemResponseVO.getResponseCode());

            val clientEntity = clientDbService.findClientByClientId(queuePayload.getClientId());
            val callbackUrl = clientEntity.getCallbackUrl();

            if(StringUtils.isNotBlank(callbackUrl)) {
                val tsqResponse = nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
                tsqResponse.setClientId(queuePayload.getClientId());

                marker.setRequest(callbackUrl, OBJECT_MAPPER.writeValueAsString(tsqResponse));
                final val tsqCallbackResponse = hTTPRestTemplate.getClient()
                        .postForObject(HTTPHelpers.buildURI(callbackUrl, ""), tsqResponse, String.class);
                marker.setResponse(tsqCallbackResponse.toString());
            }
        }catch (Exception ex){
            marker.info("Error occurred while handling FT  ", ex);
            queuePayload.setReQueue(true);
            marker.setResponse(ex.getMessage());
            queuePayload.setWaitDuration(queuePayload.getWaitDuration());
            return queuePayload;
        }
        queuePayload.setReQueue(false);
        queuePayload.setWaitDuration(0);
        return queuePayload;
    }

}
