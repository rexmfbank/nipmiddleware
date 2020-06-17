package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.messaging.QueueMode;
import com.globalaccelerex.nipmiddleware.messaging.QueuePayload;
import com.globalaccelerex.nipmiddleware.messaging.SQSService;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.fundtransfer.FTSingleCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.fundtransfer.FTSingleCreditResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.FundtransfersingleitemDc;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Nameenquirysingleitem;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Txnstatusquerysingleitem;
import com.globalaccelerex.nipmiddleware.payload.outward.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry.NESingleResponse;
import com.globalaccelerex.nipmiddleware.payload.outward.tsq.TsqRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.tsq.TsqResponse;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.service.db.FundsTransferDbService;
import com.globalaccelerex.nipmiddleware.service.ws.NIPOutwardWS;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.SystemSettingUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;
import static com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum.FAILED;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.*;
import static com.globalaccelerex.nipmiddleware.messaging.QueueMode.CALLBACK;
import static com.globalaccelerex.nipmiddleware.messaging.QueueMode.TSQ;
import static com.globalaccelerex.nipmiddleware.messaging.SQSService.TSQ_WAIT_DURATION_IN_SECONDS;
import static com.globalaccelerex.nipmiddleware.util.SystemSettingUtil.CALL_NIBSS_API;
import static com.globalaccelerex.nipmiddleware.util.SystemSettingUtil.DOWN_STATUS;


@Slf4j
@Service
public class NIPOutwardFacade {

    private final XmlUtil xmlUtil;

    private final NIPOutwardMapper nipOutwardMapper;

    private final NIPOutwardWS nipOutwardWS;

    private final SSMUtil ssmUtil;

    private final FundsTransferDbService fundsTransferDbService;

    private final NipConfig nipConfig;

    private final SQSService sqsService;

    private final ClientDbService clientDbService;

    private final SystemSettingUtil systemSettingUtil;

    @Autowired
    public NIPOutwardFacade(XmlUtil xmlUtil, NIPOutwardMapper nipOutwardMapper, NIPOutwardWS nipOutwardWS,
                            SSMUtil ssmUtil, FundsTransferDbService fundsTransferDbService, NipConfig nipConfig,
                            SQSService sqsService, ClientDbService clientDbService, SystemSettingUtil systemSettingUtil) {
        this.xmlUtil = xmlUtil;
        this.nipOutwardMapper = nipOutwardMapper;
        this.nipOutwardWS = nipOutwardWS;
        this.ssmUtil = ssmUtil;
        this.fundsTransferDbService = fundsTransferDbService;
        this.nipConfig = nipConfig;
        this.sqsService = sqsService;
        this.clientDbService = clientDbService;
        this.systemSettingUtil = systemSettingUtil;
    }


    public NESingleResponse doNameEnquiry(NESingleRequest neSingleRequest){
        val iMarker = neSingleRequest.getMarker();
        NESingleRequestVO neSingleRequestVO = nipOutwardMapper.mapNESingleRequestVO.apply(neSingleRequest);

        final val sessionId = neSingleRequestVO.getSessionId();
        final val clientId = neSingleRequest.getClientId();

        String neSingleRequestXmlString = xmlUtil.marshal(NESingleRequestVO.class, neSingleRequestVO);

        iMarker.setRequest(" Clear NESingleRequestXmlString  ====> ",  neSingleRequestXmlString);
        final val encryptedXmlString = encryptString(neSingleRequestXmlString);

        val neSingleItem = new Nameenquirysingleitem();
        neSingleItem.setRequest(encryptedXmlString);
        iMarker.info(" Sending Request to NIPOutwardWS for NameEnquiry");


        val nameEnquirySingleItemResponse = nipOutwardWS.nameEnquiry(iMarker, neSingleItem);
        if(StringUtils.isBlank(nameEnquirySingleItemResponse.getReturn())){
            iMarker.info(" Empty  Response from NIPOutwardWS ");
            systemSettingUtil.changeStatus(CALL_NIBSS_API,DOWN_STATUS);
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(NO_RESPONSE_FROM_NIBSS_MSG,iMarker);
            throw nipMiddleWareAPIException;
        }
        iMarker.info(" Received  Response from NIPOutwardWS for NameEnquiry");
        final val neSingleResponseXmlString = decryptString(nameEnquirySingleItemResponse.getReturn());
        iMarker.setResponse("Clear Name Enquiry response  from NIBSS " +neSingleResponseXmlString);

        final val neSingleResponseVO = xmlUtil.unmarshal(neSingleResponseXmlString, NESingleResponseVO.class);
        iMarker.info("Name Enquiry response  from NIBSS " +neSingleResponseVO.toString());
        final val neSingleResponse = nipOutwardMapper.mapNESingleResponseVO.apply(neSingleResponseVO);
        neSingleResponse.setNameEnquiryReference(sessionId);
        neSingleResponse.setClientId(clientId);
        return neSingleResponse;
    }

    @Async
    public void doFundsTransferAsync(FTSingleCreditRequest ftSingleCreditRequest, String sessionId){

        val iMarker = ftSingleCreditRequest.getMarker();
        val clientId = ftSingleCreditRequest.getClientId();
        final val originatorBankCode = ftSingleCreditRequest.getOriginatorBankCode();
        val clientEntity = clientDbService.findClientByClientId(ftSingleCreditRequest.getClientId()).get();
        ftSingleCreditRequest.updateCompulsoryFields(clientEntity);

        iMarker.info("::::: Handling Async Method for Funds Transfer ::::::: ");
        NESingleResponse neSingleResponse = null;
        final val fundsTransferEntity = nipOutwardMapper.mapFundsTransferEntity.apply(ftSingleCreditRequest);

        // do a mapping to entity and save record in db
        try {
            final val neSingleRequest = nipOutwardMapper.mapNESingleRequest.apply(ftSingleCreditRequest);
            neSingleRequest.setMarker(iMarker);
            fundsTransferEntity.setSessionId(sessionId);

            if(StringUtils.isEmpty(ftSingleCreditRequest.getNameEnquiryReference())){
                //we need to do a nameEnquiry
                neSingleResponse = doNameEnquiry(neSingleRequest);

                fundsTransferEntity.setNameEnquiryReference(neSingleResponse.getNameEnquiryReference());

                if(NIPResponseCodeEnum.isSuccess(neSingleResponse.getResponseCode())){
                    ftSingleCreditRequest.setBeneficiaryBVN(neSingleResponse.getBankVerificationNo());
                    ftSingleCreditRequest.setBeneficiaryKYCLevel(neSingleResponse.getKycLevel());
                }else {
                    //No need to continue with FT
                    //update the db
                    fundsTransferEntity.setResponseCode(neSingleResponse.getResponseCode());
                    val nipResponseCodeEnum = getResponseCodeEnum(neSingleResponse.getResponseCode());
                    fundsTransferEntity.setPaymentStatusEnum(nipResponseCodeEnum.getPaymentStatusEnum());
                    fundsTransferEntity.setResponseDescription(nipResponseCodeEnum.getDescription());
                    fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);
                    //write a response to SQS to do client callback
                    writeToSQS(clientId,CALLBACK, sessionId,originatorBankCode);
                    return;
                }
            }
        }catch (Exception exception){
            iMarker.info(exception.getMessage(),exception);
            fundsTransferEntity.setResponseCode(NIP_201.getCode());
            fundsTransferEntity.setResponseDescription(NAME_ENQUIRY_FAILED_MSG);
            fundsTransferEntity.setPaymentStatusEnum(FAILED);
            fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);
            //write a response to SQS to do client callback
             writeToSQS(clientId,CALLBACK, sessionId,originatorBankCode);
            return;
        }


        try{
            // go ahead with the FT

            final val ftSingleCreditRequestVO = nipOutwardMapper.mapFTSingleCreditRequestVO.apply(ftSingleCreditRequest);
            ftSingleCreditRequestVO.setSessionId(sessionId);
            ftSingleCreditRequestVO.setNameEnquiryRef(StringUtils.defaultIfBlank(ftSingleCreditRequest.getNameEnquiryReference() ,neSingleResponse.getNameEnquiryReference()));

            fundsTransferEntity.setBeneficiaryBVN(neSingleResponse.getBankVerificationNo());
            fundsTransferEntity.setBeneficiaryKYCLevel(neSingleResponse.getKycLevel());
            fundsTransferEntity.setPaymentStatusEnum(NIP_09.getPaymentStatusEnum());
            fundsTransferEntity.setResponseCode(NIP_09.getCode());
            fundsTransferEntity.setResponseDescription(NIP_09.getDescription());
            fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);

            String ftSingleCreditRequestXmlString = xmlUtil.marshal(FTSingleCreditRequestVO.class, ftSingleCreditRequestVO);
            iMarker.setRequest(" Clear ftSingleCreditRequestXml String  " , ftSingleCreditRequestXmlString);
            final val encryptedXmlString = encryptString(ftSingleCreditRequestXmlString);


            final val fundTransferSingleItemDc = new FundtransfersingleitemDc();
            fundTransferSingleItemDc.setRequest(encryptedXmlString);
            iMarker.info(" Sending FT Request to NIPOutwardWS ");

            final val fundTransferSingleItemDcResponse = nipOutwardWS.fundsTransfer(iMarker, fundTransferSingleItemDc);
            iMarker.info(" Received  Response from NIPOutwardWS >>>>> " + fundTransferSingleItemDcResponse.getReturn());
            if(StringUtils.isBlank(fundTransferSingleItemDcResponse.getReturn())){
                //update db
                iMarker.info(" Received  No Response from NIPOutwardWS  " );
                fundsTransferDbService.updateFTResponseCode(sessionId, NIP_202.getCode(),clientId,NO_RESPONSE_FROM_NIBSS_MSG,iMarker);
                systemSettingUtil.changeStatus(CALL_NIBSS_API,DOWN_STATUS);
                //write a response to SQS to do Tsq
                writeToSQS(clientId,TSQ, sessionId,originatorBankCode);
                return;
            }
            final val ftSingleItemDcResponseXmlString = decryptString(fundTransferSingleItemDcResponse.getReturn());
            iMarker.setResponse(" Clear  Response from NIPOutwardWS : FT   " + ftSingleItemDcResponseXmlString);

            final val ftSingleCreditResponseVO = xmlUtil.unmarshal(ftSingleItemDcResponseXmlString, FTSingleCreditResponseVO.class);
            iMarker.info(" Response from NIPOutwardWS : FT  " + ftSingleCreditResponseVO.toString());
            //write a response to SQS to do Tsq
            writeToSQS(clientId,TSQ, sessionId,originatorBankCode);
        }catch(Exception exception){
            iMarker.info(exception.getMessage(),exception);
            fundsTransferDbService.updateFTResponseCode(sessionId, NIP_202.getCode(),clientId,TRANSACTION_NOT_COMPLETED_MSG,iMarker);
            //write a response to SQS to do Tsq
            writeToSQS(clientId,TSQ, sessionId,originatorBankCode);
        }
        iMarker.done();
    }

    private void writeToSQS(String clientId, QueueMode queueMode ,String sessionId , String originatorBankCode){

        /*  */
        val ftQueuePayload = QueuePayload.builder()
                .clientId(clientId)
                .mode(queueMode)
                .originatorBankCode(originatorBankCode)
                .reQueue(true)
                .sessionId(sessionId)
                .waitDuration(TSQ_WAIT_DURATION_IN_SECONDS)
                .build();
        sqsService.send(ftQueuePayload, TSQ_WAIT_DURATION_IN_SECONDS);


    }

    public boolean confirmClientAndPaymentReference(FTSingleCreditRequest ftSingleCreditRequest){
        return fundsTransferDbService.confirmClientAndPaymentReference(ftSingleCreditRequest.getClientId(), ftSingleCreditRequest.getPaymentReference());
    }

    public String validateCompulsoryFields(FTSingleCreditRequest ftSingleCreditRequest){
        val clientEntityOpt = clientDbService.findClientByClientId(ftSingleCreditRequest.getClientId());
        val clientEntity = clientEntityOpt.get();
        val stringBuilder = new StringBuilder();
        if(StringUtils.isBlank(ftSingleCreditRequest.getOriginatorAccountName()) && StringUtils.isBlank(clientEntity.getAccountName())){
            stringBuilder.append("Originator Account Name is required ; ");
        }
        if(StringUtils.isBlank(ftSingleCreditRequest.getOriginatorBVN()) && StringUtils.isBlank(clientEntity.getBvn())){
            stringBuilder.append("Originator BVN  is required ; ");
        }
        if(StringUtils.isBlank(ftSingleCreditRequest.getOriginatorKYCLevel()) && StringUtils.isBlank(clientEntity.getKycLevel())){
            stringBuilder.append("Originator KYC is required ; ");
        }
        if(StringUtils.isBlank(ftSingleCreditRequest.getOriginatorAccountNo()) && StringUtils.isBlank(clientEntity.getAccountNo())){
            stringBuilder.append("Originator Account No is required ; ");
        }
        if(StringUtils.isBlank(ftSingleCreditRequest.getOriginatorBankCode()) && StringUtils.isBlank(clientEntity.getOriginatorBankCode())){
            stringBuilder.append("Originator Bank Code is required ; ");
        }
        if(ftSingleCreditRequest.getLatitude() == null && StringUtils.isBlank(clientEntity.getLatitude())){
            stringBuilder.append("Latitude is required ; ");
        }
        if(ftSingleCreditRequest.getLongitude() == null && StringUtils.isBlank(clientEntity.getLongitude())){
            stringBuilder.append("Longitude is required.  ");
        }
        val errorMsg = stringBuilder.toString();
        //check error message is blank
        //ensure that OriginatorBankCode is present so as to generate sessionId
        if( StringUtils.isBlank(errorMsg) && StringUtils.isBlank(ftSingleCreditRequest.getOriginatorBankCode())){
            ftSingleCreditRequest.setOriginatorBankCode(clientEntity.getOriginatorBankCode());
        }
        return errorMsg;
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

            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(sessionId,originatorBankCode);

            final val tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            iMarker.info(" Clear TsqSingleItemRequestXmlString " + tsqSingleItemRequestXmlString);
            final val encryptedXmlString = encryptString(tsqSingleItemRequestXmlString);

            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedXmlString);

            iMarker.info(" Sending Request to NIPOutwardWS ");

            final val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);


            if(StringUtils.isBlank(txnStatusQuerySingleItemResponse.getReturn())){
                iMarker.info(" Empty  Response from NIPOutwardWS ");
                systemSettingUtil.changeStatus(CALL_NIBSS_API,DOWN_STATUS);
                val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
                nipMiddleWareAPIException.buildFailureStatusException(NO_RESPONSE_FROM_NIBSS_MSG,iMarker);
                throw nipMiddleWareAPIException;

            }else {
                iMarker.info(" Received  Response from NIPOutwardWS ");
                final val tsqSingleItemResponseXmlString = decryptString(txnStatusQuerySingleItemResponse.getReturn());
                iMarker.setResponse(" Clear  Response from NIPOutwardWS : TSQ "+ tsqSingleItemResponseXmlString);

                final val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);

                tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
                fundsTransferDbService.updateFTResponseCode(sessionId, tsqSingleItemResponseVO.getResponseCode(),clientId,StringUtils.EMPTY,iMarker);
                tsqResponse.setResponseCode(tsqSingleItemResponseVO.getResponseCode());
            }
        }else{
            //convert entity to tsqresponse
            tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
        }
        tsqResponse.setClientId(clientId);
        return tsqResponse;
    }

    private String encryptString(String clearString){
        return ssmUtil.encryptRequest(clearString);
    }

    private String decryptString(String encryptedString){
        return ssmUtil.decryptResponse(encryptedString);
    }
}
