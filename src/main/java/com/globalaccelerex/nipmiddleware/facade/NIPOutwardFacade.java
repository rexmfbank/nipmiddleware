package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleResponse;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Nameenquirysingleitem;
import com.globalaccelerex.nipmiddleware.service.db.FundsTransferDbService;
import com.globalaccelerex.nipmiddleware.service.ws.NIPOutwardWS;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NIPOutwardFacade {

    private final XmlUtil xmlUtil;

    private final NIPOutwardMapper nipOutwardMapper;

    private final NIPOutwardWS nipOutwardWS;

    private final SSMUtil ssmUtil;

    private final FundsTransferDbService fundsTransferDbService;

    @Autowired
    public NIPOutwardFacade(XmlUtil xmlUtil, NIPOutwardMapper nipOutwardMapper, NIPOutwardWS nipOutwardWS, SSMUtil ssmUtil, FundsTransferDbService fundsTransferDbService) {
        this.xmlUtil = xmlUtil;
        this.nipOutwardMapper = nipOutwardMapper;
        this.nipOutwardWS = nipOutwardWS;
        this.ssmUtil = ssmUtil;
        this.fundsTransferDbService = fundsTransferDbService;
    }


    public NESingleResponse doNameEnquiry(NESingleRequest neSingleRequest){
        val iMarker = neSingleRequest.getMarker();
        iMarker.setRequest(" NESingleRequest from client payload ", neSingleRequest.toString());
        NESingleRequestVO neSingleRequestVO = nipOutwardMapper.mapNESingleRequestVO.apply(neSingleRequest);

        final val sessionId = neSingleRequestVO.getSessionId();
        final val requestId = neSingleRequest.getRequestId();

        iMarker.setRequest(" Mapping to NESingleRequestVO ", neSingleRequestVO.toString());
        String neSingleRequestXmlString = xmlUtil.marshal(NESingleRequestVO.class, neSingleRequestVO);
        log.info(" Raw Xml String :::: {} \n" , neSingleRequestXmlString);
        final val encryptedXmlString = encryptString(neSingleRequestXmlString);
        log.info(" Encrypted Xml String :::: {} \n" , encryptedXmlString);
        val neSingleItem = new Nameenquirysingleitem();
        neSingleItem.setRequest(encryptedXmlString);
        iMarker.info(" Sending Request to NIPOutwardWS ");
        val nameEnquirySingleItemResponse = nipOutwardWS.nameEnquiry(iMarker, neSingleItem);
        iMarker.info(" Received  Response from NIPOutwardWS ");
        if(StringUtils.isEmpty(nameEnquirySingleItemResponse.getReturn())){
            throw new NIPMiddleWareAPIException("Error"," Response String from NIBSS not present ",false);
        }
        final val neSingleResponseXmlString = decryptString(nameEnquirySingleItemResponse.getReturn());
        iMarker.info(" Mapping  Response String from NIPOutwardWS to NESingleResponseVO " + neSingleResponseXmlString);

        final val neSingleResponseVO = xmlUtil.unmarshal(neSingleResponseXmlString, NESingleResponseVO.class);
        iMarker.info(" NESingleResponseVO " + neSingleResponseVO.toString());


        final val neSingleResponse = nipOutwardMapper.mapNESingleResponseVO.apply(neSingleResponseVO);
        neSingleResponse.setSessionId(sessionId);
        neSingleResponse.setOriginalRequestId(requestId);
        iMarker.info(" NESingleResponse " + neSingleResponse.toString());
        return neSingleResponse;
    }

    private String encryptString(String clearString){
        return ssmUtil.encryptRequest(clearString);
    }

    private String decryptString(String encryptedString){
        return ssmUtil.decryptResponse(encryptedString);
    }
}
