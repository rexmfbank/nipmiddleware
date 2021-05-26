package com.globalaccelerex.nipmiddleware.facade.outward;

import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.payload.client.tsq.TsqRequest;
import com.globalaccelerex.nipmiddleware.payload.client.tsq.TsqResponse;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Txnstatusquerysingleitem;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.service.db.FundsTransferDbService;
import com.globalaccelerex.nipmiddleware.service.ws.NIPOutwardWS;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_15;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.CLIENT_NOT_FOUND_MSG;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.NO_RESPONSE_FROM_NIBSS_MSG;


@Slf4j
@Service
public class TsqFacade {

    private  FundsTransferDbService fundsTransferDbService;

    private  NIPOutwardMapper nipOutwardMapper;

    private  XmlUtil xmlUtil;

    private  NIPOutwardWS nipOutwardWS;

    private ClientDbService clientDbService;
    private SSMUtil ssmUtil;

    public TsqResponse doInwardTsq(String clientId , IMarker iMarker , String sessionId){

        iMarker.info("::::: Handling Inward Tsq ::::::: ");

        iMarker.setRequest(" sessionId for TsqRequest ", sessionId);

        val clientEntityOpt = clientDbService.findClientByClientId(clientId);
        if(!clientEntityOpt.isPresent()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_NOT_FOUND_MSG,iMarker);
            throw nipMiddleWareAPIException;
        }
        val clientEntity = clientEntityOpt.get();
        val originatorBankCode = clientEntity.getOriginatorBankCode();
        final val tsqSingleItemResponseVO = handleTsq(sessionId, originatorBankCode, iMarker);
        val tsqResponse =  TsqResponse.builder().sessionId(sessionId).build();
        tsqResponse.setClientId(clientId);
        tsqResponse.setResponseCode(tsqSingleItemResponseVO.getResponseCode());
        return tsqResponse;
    }

    public TsqResponse doTsq(TsqRequest tsqRequest){
        val iMarker = tsqRequest.getMarker();
        val clientId = tsqRequest.getClientId();
        iMarker.info("::::: Handling Tsq ::::::: ");

        iMarker.setRequest(" TSQRequest from client payload ", tsqRequest.toString());
        TsqResponse tsqResponse = null;
        //check if transaction is pending before doing the webservice call
        FundsTransferEntity fundsTransferEntity = null;

        if(StringUtils.isBlank(tsqRequest.getSessionId())){
            fundsTransferEntity = fundsTransferDbService.
                    findRecord(clientId, tsqRequest.getPaymentReference(),iMarker);
        }else {
            fundsTransferEntity = fundsTransferDbService.
                    findRecord(clientId, tsqRequest.getPaymentReference(),tsqRequest.getSessionId(),iMarker);
        }
        if(fundsTransferEntity == null){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildExceptionFromEnum(NIP_15 ,iMarker);
            throw nipMiddleWareAPIException;
        }
        final val sessionId = fundsTransferEntity.getSessionId();
        final val originatorBankCode = fundsTransferEntity.getOriginatorInstitutionCode();

        if(fundsTransferEntity.isPending()){
            final val tsqSingleItemResponseVO = handleTsq(sessionId, originatorBankCode, iMarker);
            tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
            fundsTransferDbService.updateFTResponseCode(sessionId, tsqSingleItemResponseVO.getResponseCode(),clientId,StringUtils.EMPTY,iMarker);
            tsqResponse.setResponseCode(tsqSingleItemResponseVO.getResponseCode());
        }else{
            //convert entity to tsqresponse
            tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
        }
        tsqResponse.setClientId(clientId);
        return tsqResponse;
    }

    private TsqSingleItemResponseVO handleTsq(String sessionId , String originatorBankCode , IMarker iMarker){
        final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(sessionId,originatorBankCode);

        final val tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
        iMarker.info(" Clear TsqSingleItemRequestXmlString " + tsqSingleItemRequestXmlString);
        final val encryptedXmlString = ssmUtil.encryptRequest(tsqSingleItemRequestXmlString,originatorBankCode , iMarker);

        final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
        txnstatusquerysingleitem.setRequest(encryptedXmlString);

        iMarker.info(" Sending Request to NIPOutwardWS ");

        final val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);

        if(StringUtils.isBlank(txnStatusQuerySingleItemResponse.getReturn())){
            iMarker.info(" Empty  Response from NIPOutwardWS ");
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(NO_RESPONSE_FROM_NIBSS_MSG,iMarker);
            throw nipMiddleWareAPIException;
        }
        iMarker.info(" Received  Response from NIPOutwardWS ");
        final val tsqSingleItemResponseXmlString =
                ssmUtil.decryptResponse(txnStatusQuerySingleItemResponse.getReturn(),originatorBankCode, iMarker);
        iMarker.setResponse(" Clear  Response from NIPOutwardWS : TSQ "+ tsqSingleItemResponseXmlString);

        return xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);
    }

    @Autowired
    public void setFundsTransferDbService(FundsTransferDbService fundsTransferDbService) {
        this.fundsTransferDbService = fundsTransferDbService;
    }

    @Autowired
    public void setNipOutwardMapper(NIPOutwardMapper nipOutwardMapper) {
        this.nipOutwardMapper = nipOutwardMapper;
    }

    @Autowired
    public void setXmlUtil(XmlUtil xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    @Autowired
    public void setNipOutwardWS(NIPOutwardWS nipOutwardWS) {
        this.nipOutwardWS = nipOutwardWS;
    }

    @Autowired
    public void setClientDbService(ClientDbService clientDbService) {
        this.clientDbService = clientDbService;
    }

    @Autowired
    public void setSsmUtil(SSMUtil ssmUtil) {
        this.ssmUtil = ssmUtil;
    }
}
