package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
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

        NESingleRequestVO neSingleRequestVO = nipOutwardMapper.mapNESingleRequestVO.apply(neSingleRequest);

        final val sessionId = neSingleRequestVO.getSessionId();
        final val clientId = neSingleRequest.getClientId();

        String neSingleRequestXmlString = xmlUtil.marshal(NESingleRequestVO.class, neSingleRequestVO);

        iMarker.setRequest(" NESingleRequestXmlString  to NIBSS ", neSingleRequestXmlString);
        final val encryptedXmlString = encryptString(neSingleRequestXmlString);
        log.info(" Encrypted Xml String :::: {} \n" , encryptedXmlString);
        val neSingleItem = new Nameenquirysingleitem();
        neSingleItem.setRequest(encryptedXmlString);
        iMarker.info(" Sending Request to NIPOutwardWS ");
        val nameEnquirySingleItemResponse = nipOutwardWS.nameEnquiry(iMarker, neSingleItem);
        iMarker.setResponse("Name Enquiry response Object from NIBSS " +nameEnquirySingleItemResponse.toString());
        iMarker.info(" Received  Response from NIPOutwardWS ");
        if(StringUtils.isEmpty(nameEnquirySingleItemResponse.getReturn())){
            throw new NIPMiddleWareAPIException("Error"," Response String from NIBSS not present ",false);
        }
        final val neSingleResponseXmlString = decryptString(nameEnquirySingleItemResponse.getReturn());
        iMarker.setResponse("Name Enquiry response String from NIBSS : NE" +neSingleResponseXmlString);

        final val neSingleResponseVO = xmlUtil.unmarshal(neSingleResponseXmlString, NESingleResponseVO.class);

        final val neSingleResponse = nipOutwardMapper.mapNESingleResponseVO.apply(neSingleResponseVO);
        neSingleResponse.setSessionId(sessionId);
        neSingleResponse.setClientId(clientId);
        return neSingleResponse;
    }

    @Async
    public void doFundsTransferAsync(FTSingleCreditRequest ftSingleCreditRequest, String sessionId){
        log.info("::::: Handling Async Method for Funds Transfer ::::::: ");
        val iMarker = ftSingleCreditRequest.getMarker();
        // do a mapping to entity and save record in db
        final val neSingleRequest = nipOutwardMapper.mapNESingleRequest.apply(ftSingleCreditRequest);
        neSingleRequest.setMarker(iMarker);
        final val fundsTransferEntity = nipOutwardMapper.mapFundsTransferEntity.apply(ftSingleCreditRequest);
        fundsTransferEntity.setSessionId(sessionId);
        NESingleResponse neSingleResponse = null;
        if(StringUtils.isEmpty(ftSingleCreditRequest.getNameEnquiryReference())){
            //we need to do a nameEnquiry
            neSingleResponse = doNameEnquiry(neSingleRequest);
            fundsTransferEntity.setNameEnquiryReference(neSingleResponse.getSessionId());
            if(StringUtils.isNotEmpty(ftSingleCreditRequest.getBeneficiaryBVN()) && !StringUtils.equalsIgnoreCase(ftSingleCreditRequest.getBeneficiaryBVN(),neSingleResponse.getBankVerificationNo())){
                //the supplied BVN and the NIBSS BVN are not the same
                //update the db
                fundsTransferEntity.setResponseCode(NIP_104.getCode());
                fundsTransferEntity.setPaymentStatusEnum(FAILED);
                fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);
                //send a response to the client callback
                return;
            }
            if(StringUtils.isEmpty(neSingleResponse.getAccountName()) || (!neSingleResponse.isSuccessResponse())){
                //discontinue FT since we don't have a response as regards the beneficiary account name or
                //update the db
                fundsTransferEntity.setResponseCode(NIP_105.getCode());
                fundsTransferEntity.setPaymentStatusEnum(FAILED);
                fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);
                //send a response to the client callback
                return;
            }
        }

        try{
            // go ahead with the FT
            final val ftSingleCreditRequestVO = nipOutwardMapper.mapFTSingleCreditRequestVO.apply(ftSingleCreditRequest);
            ftSingleCreditRequestVO.setSessionId(sessionId);
            ftSingleCreditRequestVO.setNameEnquiryRef(neSingleResponse == null ? ftSingleCreditRequest.getNameEnquiryReference() :  neSingleResponse.getSessionId());

            fundsTransferEntity.setPaymentStatusEnum(PENDING);
            fundsTransferEntity.setResponseCode(NIP_09.getCode());
            fundsTransferDbService.saveFundsTransferEntity(fundsTransferEntity);

            iMarker.setRequest(" Mapping to FTSingleCreditRequestVO ", ftSingleCreditRequestVO.toString());
            String ftSingleCreditRequestXmlString = xmlUtil.marshal(FTSingleCreditRequestVO.class, ftSingleCreditRequestVO);
            log.info(" Raw Xml String :::: {} \n" , ftSingleCreditRequestXmlString);
            final val encryptedXmlString = encryptString(ftSingleCreditRequestXmlString);
            log.info(" Encrypted Xml String :::: {} \n" , encryptedXmlString);

            final val fundtransfersingleitemDc = new FundtransfersingleitemDc();
            fundtransfersingleitemDc.setRequest(encryptedXmlString);
            iMarker.info(" Sending Request to NIPOutwardWS ");
            final val fundTransferSingleItemDcResponse = nipOutwardWS.fundsTransfer(iMarker, fundtransfersingleitemDc);
            iMarker.info(" Received  Response from NIPOutwardWS ");
            iMarker.setResponse(" Received  Response from NIPOutwardWS : FT "+ fundTransferSingleItemDcResponse.toString());
            if(StringUtils.isEmpty(fundTransferSingleItemDcResponse.getReturn())){

                //update db
                fundsTransferDbService.updateFTResponseCode(sessionId, NIP_106.getCode());
                //call client endpoint
                return;
            }
            log.info("Encrypted Response String from NIBSS :::::: {}" , fundTransferSingleItemDcResponse.getReturn());
            final val ftSingleItemDcResponseXmlString = decryptString(fundTransferSingleItemDcResponse.getReturn());
            iMarker.info(" Mapping  Response String from NIPOutwardWS to FTSingleResponseVO " + ftSingleItemDcResponseXmlString);

            final val ftSingleCreditResponseVO = xmlUtil.unmarshal(ftSingleItemDcResponseXmlString, FTSingleCreditResponseVO.class);
            iMarker.info(" FTSingleCreditResponseVO " + ftSingleCreditResponseVO.toString());

            //simulate a waiting period of 60secs
            /* */
            log.info("----------- Stimulating 1 minute wait ---------------");
            Thread.sleep(60000);
            log.info("----------- Completed 1 minute wait ---------------");

            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(ftSingleCreditResponseVO.getSessionId());

            String tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            log.info(" Raw Xml String :::: {} \n" , tsqSingleItemRequestXmlString);
            iMarker.setRequest(" tsqSingleItemRequestXmlString  to NIBSS ", tsqSingleItemRequestXmlString);

            final val encryptedTsqSingleItemRequestXmlString = encryptString(tsqSingleItemRequestXmlString);
            log.info(" Encrypted Xml String :::: {} \n" , encryptedTsqSingleItemRequestXmlString);

            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedTsqSingleItemRequestXmlString);

            final val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);

            final val tsqSingleItemResponseXmlString = decryptString(txnStatusQuerySingleItemResponse.getReturn());
            log.info(" Clear String response :: {}" , tsqSingleItemResponseXmlString);
            iMarker.setResponse(" Received  Response from NIPOutwardWS TSQ "+ tsqSingleItemResponseXmlString);

            final val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);
            iMarker.info(" TsqSingleItemResponseVO " + tsqSingleItemResponseVO.toString());

            //update db
            fundsTransferDbService.updateFTResponseCode(tsqSingleItemResponseVO.getSessionId(), tsqSingleItemResponseVO.getResponseCode());
            //call client endpoint and push response to him
        }catch(Exception ex){
            log.error("Caught this exception ::::: {}", ex);
            fundsTransferDbService.updateFTResponseCode(sessionId, NIP_107.getCode());

        }
    }

    public boolean confirmClientAndPaymentReference(FTSingleCreditRequest ftSingleCreditRequest){
        return fundsTransferDbService.confirmClientAndPaymentReference(ftSingleCreditRequest.getClientId(), ftSingleCreditRequest.getPaymentReference());
    }

    public TsqResponse doTsq(TsqRequest tsqRequest){
        log.info("::::: Handling Tsq ::::::: ");
        val iMarker = tsqRequest.getMarker();
        iMarker.setRequest(" TSQRequest from client payload ", tsqRequest.toString());
        TsqResponse tsqResponse = null;
        //check if transaction is pending before doing the webservice call
        final val fundsTransferEntity = fundsTransferDbService.findRecord(tsqRequest.getClientId(), tsqRequest.getPaymentReference());
        final val sessionId = fundsTransferEntity.getSessionId();
        if(fundsTransferEntity.isPending()){
            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(sessionId);

            iMarker.setRequest(" Mapping to tsqSingleItemRequestVO ", tsqSingleItemRequestVO.toString());
            final val tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            log.info(" Raw Xml String :::: {} \n" , tsqSingleItemRequestXmlString);

            final val encryptedXmlString = encryptString(tsqSingleItemRequestXmlString);
            log.info(" Encrypted Xml String :::: {} \n" , encryptedXmlString);

            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedXmlString);

            iMarker.info(" Sending Request to NIPOutwardWS ");

            final val txnstatusquerysingleitemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);
            log.info(" Received  Response from NIPOutwardWS ");

            final val tsqSingleItemResponseXmlString = decryptString(txnstatusquerysingleitemResponse.getReturn());
            log.info(" Clear String response :: {}" , tsqSingleItemResponseXmlString);

            final val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);
            iMarker.info(" TsqSingleItemResponseVO " + tsqSingleItemResponseVO.toString());

            tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);

            //update db
            fundsTransferDbService.updateFTResponseCode(sessionId, tsqSingleItemResponseVO.getResponseCode());
            //update response
            tsqResponse.setResponseCode(tsqSingleItemResponseVO.getResponseCode());
            log.info(" Tsq response :: {}" , tsqResponse.toString());
        }else{
            //convert entity to tsqresponse
            tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);
        }
        tsqResponse.setClientId(tsqRequest.getClientId());
        return tsqResponse;
    }

    private String encryptString(String clearString){
        return ssmUtil.encryptRequest(clearString);
    }

    private String decryptString(String encryptedString){
        return ssmUtil.decryptResponse(encryptedString);
    }
}
