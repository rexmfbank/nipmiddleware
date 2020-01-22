package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer.FTPendingResponse;
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

@Service
public class NIPOutwardFacade {

    private final XmlUtil xmlUtil;

    private final NIPOutwardMapper nipOutwardMapper;

    private final NIPOutwardWS nipOutwardWS;

    private final SSMUtil ssmUtil;

    private final FundsTransferDbService fundsTransferDbService;

    @Autowired
    private NipConfig nipConfig;

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


        iMarker.setRequest(" Clear NESingleRequestXmlString  ", neSingleRequestXmlString);

        final val encryptedXmlString = encryptString(neSingleRequestXmlString);

        val neSingleItem = new Nameenquirysingleitem();
        neSingleItem.setRequest(encryptedXmlString);
        iMarker.info(" Sending Request to NIPOutwardWS ");
        val nameEnquirySingleItemResponse = nipOutwardWS.nameEnquiry(iMarker, neSingleItem);
        iMarker.setResponse("Encrypted Name Enquiry response  from NIBSS " +nameEnquirySingleItemResponse.getReturn());
        iMarker.info(" Received  Response from NIPOutwardWS ");

        if(StringUtils.isEmpty(nameEnquirySingleItemResponse.getReturn())){
            iMarker.setResponse(" Name Enquiry response  from NIBSS " +nameEnquirySingleItemResponse.getReturn());
            throw new NIPMiddleWareAPIException("Error"," Response String from NIBSS not present ",false);
        }
        final val neSingleResponseXmlString = decryptString(nameEnquirySingleItemResponse.getReturn());

        iMarker.setResponse("Clear Name Enquiry response  from NIBSS " +neSingleResponseXmlString);

        final val neSingleResponseVO = xmlUtil.unmarshal(neSingleResponseXmlString, NESingleResponseVO.class);

        final val neSingleResponse = nipOutwardMapper.mapNESingleResponseVO.apply(neSingleResponseVO);
        neSingleResponse.setSessionId(sessionId);
        neSingleResponse.setClientId(clientId);
        return neSingleResponse;
    }


    @Async
    public void doFundsTransferAsync(FTSingleCreditRequest ftSingleCreditRequest, String sessionId){
        val iMarker = ftSingleCreditRequest.getMarker();
        iMarker.info("::::: Handling Async Method for Funds Transfer ::::::: ");

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
                //discontinue FT since we don't have a response as regards the beneficiary account name
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


            String ftSingleCreditRequestXmlString = xmlUtil.marshal(FTSingleCreditRequestVO.class, ftSingleCreditRequestVO);
            iMarker.setRequest(" Clear ftSingleCreditRequestXml String ", ftSingleCreditRequestXmlString);
            final val encryptedXmlString = encryptString(ftSingleCreditRequestXmlString);


            final val fundtransfersingleitemDc = new FundtransfersingleitemDc();
            fundtransfersingleitemDc.setRequest(encryptedXmlString);
            iMarker.info(" Sending Request to NIPOutwardWS ");
            final val fundTransferSingleItemDcResponse = nipOutwardWS.fundsTransfer(iMarker, fundtransfersingleitemDc);
            iMarker.info(" Received  Response from NIPOutwardWS ");

            if(StringUtils.isEmpty(fundTransferSingleItemDcResponse.getReturn())){
                iMarker.setResponse(" No  Response from NIPOutwardWS : FT "+ fundTransferSingleItemDcResponse.getReturn());
                //update db
                fundsTransferDbService.updateFTResponseCode(sessionId, NIP_106.getCode());
                //call client endpoint
                return;
            }

            final val ftSingleItemDcResponseXmlString = decryptString(fundTransferSingleItemDcResponse.getReturn());
            iMarker.setResponse(" Clear  Response from NIPOutwardWS : FT  " + ftSingleItemDcResponseXmlString);

            final val ftSingleCreditResponseVO = xmlUtil.unmarshal(ftSingleItemDcResponseXmlString, FTSingleCreditResponseVO.class);


            //simulate a waiting period of 60secs
            /* */
            iMarker.info("----------- Stimulating  {} milliseconds wait ---------------"+nipConfig.getTsqWaitTime());
            Thread.sleep(nipConfig.getTsqWaitTime());
            iMarker.info("----------- Completed  {} milliseconds wait ---------------"+nipConfig.getTsqWaitTime());


            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(ftSingleCreditResponseVO.getSessionId());

            String tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            iMarker.setRequest(" tsqSingleItemRequestXmlString  to NIBSS ", tsqSingleItemRequestXmlString);
            final val encryptedTsqSingleItemRequestXmlString = encryptString(tsqSingleItemRequestXmlString);


            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedTsqSingleItemRequestXmlString);

            final val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);


            final val tsqSingleItemResponseXmlString = decryptString(txnStatusQuerySingleItemResponse.getReturn());

            iMarker.setResponse(" Clear  Response from NIPOutwardWS TSQ "+ tsqSingleItemResponseXmlString);

            final val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);
            iMarker.info(" TsqSingleItemResponseVO " + tsqSingleItemResponseVO.toString());

            //update db
            fundsTransferDbService.updateFTResponseCode(tsqSingleItemResponseVO.getSessionId(), tsqSingleItemResponseVO.getResponseCode());
            //call client endpoint and push response to him
        }catch(Exception ex){
            iMarker.info("Caught this exception ::::: {}", ex);
            fundsTransferDbService.updateFTResponseCode(sessionId, NIP_107.getCode());

        }
    }

    public boolean confirmClientAndPaymentReference(FTSingleCreditRequest ftSingleCreditRequest){
        return fundsTransferDbService.confirmClientAndPaymentReference(ftSingleCreditRequest.getClientId(), ftSingleCreditRequest.getPaymentReference());
    }

    public TsqResponse doTsq(TsqRequest tsqRequest){
        val iMarker = tsqRequest.getMarker();
        iMarker.info("::::: Handling Tsq ::::::: ");

        iMarker.setRequest(" TSQRequest from client payload ", tsqRequest.toString());
        TsqResponse tsqResponse = null;
        //check if transaction is pending before doing the webservice call
        final val fundsTransferEntity = fundsTransferDbService.findRecord(tsqRequest.getClientId(), tsqRequest.getPaymentReference());
        final val sessionId = fundsTransferEntity.getSessionId();
        if(fundsTransferEntity.isPending()){
            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(sessionId);


            final val tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);
            iMarker.setRequest(" Clear TsqSingleItemRequestXmlString ", tsqSingleItemRequestXmlString);
            final val encryptedXmlString = encryptString(tsqSingleItemRequestXmlString);
            iMarker.info(" Encrypted Xml String :::: {} \n" + encryptedXmlString);


            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedXmlString);

            iMarker.info(" Sending Request to NIPOutwardWS ");

            final val txnstatusquerysingleitemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);

            iMarker.info(" Received  Response from NIPOutwardWS ");

            final val tsqSingleItemResponseXmlString = decryptString(txnstatusquerysingleitemResponse.getReturn());
            iMarker.info(" Clear String response :: {}" + tsqSingleItemResponseXmlString);
            iMarker.setResponse(tsqSingleItemResponseXmlString);


            final val tsqSingleItemResponseVO = xmlUtil.unmarshal(tsqSingleItemResponseXmlString, TsqSingleItemResponseVO.class);

            tsqResponse =  nipOutwardMapper.mapTsqResponse.apply(fundsTransferEntity);

            fundsTransferDbService.updateFTResponseCode(sessionId, tsqSingleItemResponseVO.getResponseCode());
            tsqResponse.setResponseCode(tsqSingleItemResponseVO.getResponseCode());

            iMarker.info(" Tsq response :: {}" + tsqResponse.toString());

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
