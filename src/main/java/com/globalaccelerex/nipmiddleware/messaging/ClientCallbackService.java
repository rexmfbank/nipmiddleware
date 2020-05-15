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
import com.globalaccelerex.nipmiddleware.util.SystemSettingUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

import static com.globalaccelerex.nipmiddleware.messaging.SQSService.DEFAULT_MAX_WAIT_IN_SECONDS;
import static com.globalaccelerex.nipmiddleware.util.SystemSettingUtil.CALL_NIBSS_API;
import static com.globalaccelerex.nipmiddleware.util.SystemSettingUtil.DOWN_STATUS;


@Service
public class ClientCallbackService {

    private final XmlUtil xmlUtil;

    private final NIPOutwardMapper nipOutwardMapper;

    private final NIPOutwardWS nipOutwardWS;

    private final HTTPRestTemplate hTTPRestTemplate;

    private final SSMUtil ssmUtil;

    private final FundsTransferDbService fundsTransferDbService;

    private final SystemSettingUtil systemSettingUtil;

    private final ClientDbService clientDbService;

    public static final int DEFAULT_QUEUE_WAIT_PERIOD = 30;

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    @Autowired
    public ClientCallbackService(XmlUtil xmlUtil, NIPOutwardMapper nipOutwardMapper, NIPOutwardWS nipOutwardWS,
                                 HTTPRestTemplate hTTPRestTemplate, SSMUtil ssmUtil, FundsTransferDbService fundsTransferDbService,
                                 SystemSettingUtil systemSettingUtil, ClientDbService clientDbService) {
        this.xmlUtil = xmlUtil;
        this.nipOutwardMapper = nipOutwardMapper;
        this.nipOutwardWS = nipOutwardWS;
        this.hTTPRestTemplate = hTTPRestTemplate;
        this.ssmUtil = ssmUtil;
        this.fundsTransferDbService = fundsTransferDbService;
        this.systemSettingUtil = systemSettingUtil;
        this.clientDbService = clientDbService;
    }

    public QueuePayload handleCallback(IMarker marker, QueuePayload queuePayload){
        val clientId = queuePayload.getClientId();
        val sessionId = queuePayload.getSessionId();
        try{
            val fundsTransferEntity = fundsTransferDbService.findRecord(clientId, sessionId);
            val clientEntityOpt = clientDbService.findClientByClientId(clientId);

            val callbackUrl = clientEntityOpt.isPresent() ? clientEntityOpt.get().getCallbackUrl() : StringUtils.EMPTY;

            if(StringUtils.isNotBlank(callbackUrl)) {
                val tsqResponse = nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
                tsqResponse.setClientId(clientId);

                marker.setRequest(callbackUrl, OBJECT_MAPPER.writeValueAsString(tsqResponse));
                final val tsqCallbackResponse = hTTPRestTemplate.getClient()
                        .postForObject(HTTPHelpers.buildURI(callbackUrl, ""), tsqResponse, String.class);
                marker.setResponse(tsqCallbackResponse);
            }

        }catch (Exception ex){
            marker.info("Error occurred while doing callback  ", ex);
            queuePayload.setReQueue(true);
            marker.setResponse(ex.getMessage());
            queuePayload.setWaitDuration((queuePayload.getWaitDuration() == 0) ? DEFAULT_QUEUE_WAIT_PERIOD : queuePayload.getWaitDuration() + DEFAULT_QUEUE_WAIT_PERIOD);

        }finally {
            if(queuePayload.getWaitDuration() > DEFAULT_MAX_WAIT_IN_SECONDS){
                queuePayload.setReQueue(false);
                queuePayload.setWaitDuration(0);
            }
        }

        return queuePayload;
    }

    public QueuePayload handleTsq(IMarker marker, QueuePayload queuePayload){
        FundsTransferEntity fundsTransferEntity = null;
        val clientId = queuePayload.getClientId();
        val sessionId = queuePayload.getSessionId();
        val originatorBankCode = queuePayload.getOriginatorBankCode();

        try{
            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(sessionId,originatorBankCode);

            String tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            marker.setRequest(" clear tsqSingleItemRequestXmlString  to NIBSS ", tsqSingleItemRequestXmlString);

            final val encryptedTsqSingleItemRequestXmlString = ssmUtil.encryptRequest(tsqSingleItemRequestXmlString);

            final val txnStatusQuerySingleitem = new Txnstatusquerysingleitem();
            txnStatusQuerySingleitem.setRequest(encryptedTsqSingleItemRequestXmlString);

            val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(marker, txnStatusQuerySingleitem);
            if(StringUtils.isBlank(txnStatusQuerySingleItemResponse.getReturn())){
                marker.info(" Empty  Response from NIPOutwardWS For TSQ ");
                systemSettingUtil.changeStatus(CALL_NIBSS_API,DOWN_STATUS);
            }else {
                val tsqSingleItemResponseXmlString = ssmUtil.decryptResponse(txnStatusQuerySingleItemResponse.getReturn());
                marker.setResponse(" Clear  Response from NIPOutwardWS TSQ "+ tsqSingleItemResponseXmlString);

                val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);
                marker.info(" TsqSingleItemResponseVO " + tsqSingleItemResponseVO.toString());

                val responseCode = tsqSingleItemResponseVO.getResponseCode();

                fundsTransferEntity = fundsTransferDbService.updateFTResponseCode(sessionId,responseCode,clientId );

                if (StringUtils.isNotBlank(fundsTransferEntity.getResponseCode())){
                    val clientEntityOpt = clientDbService.findClientByClientId(clientId);

                    val callbackUrl = clientEntityOpt.isPresent() ? clientEntityOpt.get().getCallbackUrl() : StringUtils.EMPTY;

                    if(StringUtils.isNotBlank(callbackUrl)) {
                        val tsqResponse = nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
                        tsqResponse.setClientId(clientId);

                        marker.setRequest(callbackUrl, OBJECT_MAPPER.writeValueAsString(tsqResponse));
                        final val tsqCallbackResponse = hTTPRestTemplate.getClient()
                                .postForObject(HTTPHelpers.buildURI(callbackUrl, ""), tsqResponse, String.class);
                        marker.setResponse(tsqCallbackResponse);
                    }
                }
            }
        }catch (Exception ex){
            marker.info("Error occurred while handling FT  ", ex);
            queuePayload.setReQueue(true);
            marker.setResponse(ex.getMessage());
            queuePayload.setWaitDuration((queuePayload.getWaitDuration() == 0) ? DEFAULT_QUEUE_WAIT_PERIOD : queuePayload.getWaitDuration() + DEFAULT_QUEUE_WAIT_PERIOD);
        }finally {
            if(queuePayload.getWaitDuration() > DEFAULT_MAX_WAIT_IN_SECONDS){
                queuePayload.setReQueue(false);
                queuePayload.setWaitDuration(0);
            }
        }
        return queuePayload;
    }


}
