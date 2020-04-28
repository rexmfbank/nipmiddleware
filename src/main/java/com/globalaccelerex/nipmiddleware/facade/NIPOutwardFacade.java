package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.messaging.QueueMode;
import com.globalaccelerex.nipmiddleware.messaging.QueuePayload;
import com.globalaccelerex.nipmiddleware.messaging.SQSService;
import com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleResponse;
import com.globalaccelerex.nipmiddleware.payload.client.outward.tsq.TsqRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.tsq.TsqResponse;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.fundtransfer.FTSingleCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.fundtransfer.FTSingleCreditResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.FundtransfersingleitemDc;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Nameenquirysingleitem;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Txnstatusquerysingleitem;
import com.globalaccelerex.nipmiddleware.service.db.FundsTransferDbService;
import com.globalaccelerex.nipmiddleware.service.ws.NIPOutwardWS;
import com.globalaccelerex.nipmiddleware.util.MockFTResponse;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;
import static com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum.FAILED;
import static com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum.PENDING;
import static com.globalaccelerex.nipmiddleware.messaging.QueueMode.CALLBACK;
import static com.globalaccelerex.nipmiddleware.messaging.QueueMode.TSQ;

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

    @Autowired
    public NIPOutwardFacade(XmlUtil xmlUtil, NIPOutwardMapper nipOutwardMapper, NIPOutwardWS nipOutwardWS,
                            SSMUtil ssmUtil, FundsTransferDbService fundsTransferDbService, NipConfig nipConfig, SQSService sqsService) {
        this.xmlUtil = xmlUtil;
        this.nipOutwardMapper = nipOutwardMapper;
        this.nipOutwardWS = nipOutwardWS;
        this.ssmUtil = ssmUtil;
        this.fundsTransferDbService = fundsTransferDbService;
        this.nipConfig = nipConfig;
        this.sqsService = sqsService;
    }


    public NESingleResponse doNameEnquiry(NESingleRequest neSingleRequest){
        val iMarker = neSingleRequest.getMarker();
        NESingleRequestVO neSingleRequestVO = nipOutwardMapper.mapNESingleRequestVO.apply(neSingleRequest);

        final val sessionId = neSingleRequestVO.getSessionId();
        final val clientId = neSingleRequest.getClientId();

        String neSingleRequestXmlString = xmlUtil.marshal(NESingleRequestVO.class, neSingleRequestVO);

        iMarker.setRequest(" Clear NESingleRequestXmlString  ", neSingleRequestXmlString);
        final val encryptedXmlString = encryptString(neSingleRequestXmlString);

        val neSingleItem = new Nameenquirysingleitem();
        neSingleItem.setRequest(encryptedXmlString);
        iMarker.info(" Sending Request to NIPOutwardWS for NameEnquiry");
        val nameEnquirySingleItemResponse = nipOutwardWS.nameEnquiry(iMarker, neSingleItem);


        if(StringUtils.isEmpty(nameEnquirySingleItemResponse.getReturn())){
            iMarker.info(" Empty  Response from NIPOutwardWS ");
            final val errorResponse = new ErrorResponse(NIP_01);
            throw new NIPMiddleWareAPIException(iMarker,errorResponse);
        }
        iMarker.info(" Received  Response from NIPOutwardWS for NameEnquiry");
        final val neSingleResponseXmlString = decryptString(nameEnquirySingleItemResponse.getReturn());
        iMarker.info("Clear Name Enquiry response  from NIBSS " +neSingleResponseXmlString);

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
                if(StringUtils.isNotEmpty(ftSingleCreditRequest.getBeneficiaryBVN()) && !StringUtils.equalsIgnoreCase(ftSingleCreditRequest.getBeneficiaryBVN(),neSingleResponse.getBankVerificationNo())){
                    //the supplied BVN and the NIBSS BVN are not the same
                    //update the db
                    fundsTransferEntity.setResponseCode(NIP_104.getCode());
                    fundsTransferEntity.setPaymentStatusEnum(FAILED);
                    fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);
                    //write a response to SQS to do client callback
                    writeToSQS(clientId,CALLBACK, sessionId);
                    return;
                }
                if(StringUtils.isEmpty(neSingleResponse.getAccountName()) || (!NIPResponseCodeEnum.isSuccess(neSingleResponse.getResponseCode()))){
                    //discontinue FT since we don't have a response as regards the beneficiary account name
                    //update the db
                    fundsTransferEntity.setResponseCode(NIP_105.getCode());
                    fundsTransferEntity.setPaymentStatusEnum(FAILED);
                    fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);
                    //write a response to SQS to do client callback
                    writeToSQS(clientId,CALLBACK, sessionId);
                    return;
                }
                ftSingleCreditRequest.setBeneficiaryBVN(neSingleResponse.getBankVerificationNo());
                ftSingleCreditRequest.setBeneficiaryKYCLevel(neSingleResponse.getKycLevel());
            }
        }catch (Exception exception){

            iMarker.info(exception.getMessage(),exception);
            fundsTransferEntity.setResponseCode(NIP_105.getCode());
            fundsTransferEntity.setPaymentStatusEnum(FAILED);
            fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);
            //write a response to SQS to do client callback
             writeToSQS(clientId,CALLBACK, sessionId);
            return;
        }


        try{
            // go ahead with the FT
            final val ftSingleCreditRequestVO = nipOutwardMapper.mapFTSingleCreditRequestVO.apply(ftSingleCreditRequest);
            ftSingleCreditRequestVO.setSessionId(sessionId);
            ftSingleCreditRequestVO.setNameEnquiryRef(StringUtils.defaultIfBlank(ftSingleCreditRequest.getNameEnquiryReference() ,neSingleResponse.getNameEnquiryReference()));

            fundsTransferEntity.setBeneficiaryBVN(neSingleResponse.getBankVerificationNo());
            fundsTransferEntity.setBeneficiaryKYCLevel(neSingleResponse.getKycLevel());
            fundsTransferEntity.setPaymentStatusEnum(PENDING);
            fundsTransferEntity.setResponseCode(NIP_09.getCode());
            fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);

            String ftSingleCreditRequestXmlString = xmlUtil.marshal(FTSingleCreditRequestVO.class, ftSingleCreditRequestVO);
            iMarker.info(" Clear ftSingleCreditRequestXml String  " + ftSingleCreditRequestXmlString);
            final val encryptedXmlString = encryptString(ftSingleCreditRequestXmlString);


            final val fundTransferSingleItemDc = new FundtransfersingleitemDc();
            fundTransferSingleItemDc.setRequest(encryptedXmlString);
            iMarker.info(" Sending FT Request to NIPOutwardWS ");
            final val fundTransferSingleItemDcResponse = nipOutwardWS.fundsTransfer(iMarker, fundTransferSingleItemDc);
            iMarker.info(" Received  Response from NIPOutwardWS >>>>> " + fundTransferSingleItemDcResponse.getReturn());
            if(StringUtils.isEmpty(fundTransferSingleItemDcResponse.getReturn())){
                //update db
                log.info(" Received  No Response from NIPOutwardWS  " );
                fundsTransferDbService.updateFTResponseCode(sessionId, NIP_106.getCode(),clientId);
                //write a response to SQS to do Tsq
                writeToSQS(clientId,TSQ, sessionId);
                return;
            }
            final val ftSingleItemDcResponseXmlString = decryptString(fundTransferSingleItemDcResponse.getReturn());
            iMarker.info(" Clear  Response from NIPOutwardWS : FT   " + ftSingleItemDcResponseXmlString);

            final val ftSingleCreditResponseVO = xmlUtil.unmarshal(ftSingleItemDcResponseXmlString, FTSingleCreditResponseVO.class);
            iMarker.setResponse(" Response from NIPOutwardWS : FT  " + ftSingleCreditResponseVO.toString());
            //write a response to SQS to do Tsq
            writeToSQS(clientId,TSQ, sessionId);

        }catch(Exception exception){
            iMarker.info(exception.getMessage(),exception);
            fundsTransferDbService.updateFTResponseCode(sessionId, NIP_107.getCode(),clientId);
            //write a response to SQS to do Tsq
            writeToSQS(clientId,TSQ, sessionId);
        }
    }

    private void writeToSQS(String clientId, QueueMode queueMode ,String sessionId){

        val ftQueuePayload = QueuePayload.builder()
                .clientId(clientId)
                .mode(queueMode)
                .reQueue(true)
                .sessionId(sessionId)
                .waitDuration(nipConfig.getTsqWaitTime())
                .build();
        sqsService.send(ftQueuePayload, nipConfig.getTsqWaitTime());
    }

    public boolean confirmClientAndPaymentReference(FTSingleCreditRequest ftSingleCreditRequest){
        return fundsTransferDbService.confirmClientAndPaymentReference(ftSingleCreditRequest.getClientId(), ftSingleCreditRequest.getPaymentReference());
    }

    public TsqResponse doTsq(TsqRequest tsqRequest){
        val iMarker = tsqRequest.getMarker();
        val clientId = tsqRequest.getClientId();
        iMarker.info("::::: Handling Tsq ::::::: ");

        iMarker.setRequest(" TSQRequest from client payload ", tsqRequest.toString());
        TsqResponse tsqResponse = null;
        //check if transaction is pending before doing the webservice call
        final val fundsTransferEntity = fundsTransferDbService.findRecord(clientId, tsqRequest.getPaymentReference(),iMarker);
        final val sessionId = fundsTransferEntity.getSessionId();
        if(fundsTransferEntity.isPending()){
            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(sessionId);


            final val tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            iMarker.setRequest(" Clear TsqSingleItemRequestXmlString ", tsqSingleItemRequestXmlString);
            final val encryptedXmlString = encryptString(tsqSingleItemRequestXmlString);


            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedXmlString);

            iMarker.info(" Sending Request to NIPOutwardWS ");

            final val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);
            iMarker.info(" Received  Response from NIPOutwardWS ");

            final val tsqSingleItemResponseXmlString = decryptString(txnStatusQuerySingleItemResponse.getReturn());
            iMarker.setResponse(" Clear  Response from NIPOutwardWS : TSQ "+ tsqSingleItemResponseXmlString);

            final val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);

            tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);

            fundsTransferDbService.updateFTResponseCode(sessionId, tsqSingleItemResponseVO.getResponseCode(),clientId);
            tsqResponse.setResponseCode(tsqSingleItemResponseVO.getResponseCode());

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
