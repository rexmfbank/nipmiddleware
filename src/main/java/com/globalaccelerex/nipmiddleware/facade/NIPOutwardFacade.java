package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.mapper.NIPOutwardMapper;
import com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleResponse;
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
        final val requestId = neSingleRequest.getRequestId();

        String neSingleRequestXmlString = xmlUtil.marshal(NESingleRequestVO.class, neSingleRequestVO);

        iMarker.setRequest(" Clear NESingleRequestXmlString  ", neSingleRequestXmlString);

        final val encryptedXmlString = encryptString(neSingleRequestXmlString);
        iMarker.setRequest(" Encrypted NESingleRequestXmlString   ", neSingleRequestXmlString);
        val neSingleItem = new Nameenquirysingleitem();
        neSingleItem.setRequest(encryptedXmlString);
        iMarker.info(" Sending Request to NIPOutwardWS ");
        val nameEnquirySingleItemResponse = nipOutwardWS.nameEnquiry(iMarker, neSingleItem);
        iMarker.info(" Received  Response from NIPOutwardWS ");
        iMarker.setResponse("Encrypted Name Enquiry response  from NIBSS " +nameEnquirySingleItemResponse.getReturn());
        if(StringUtils.isEmpty(nameEnquirySingleItemResponse.getReturn())){
            throw new NIPMiddleWareAPIException("Error"," Response String from NIBSS not present ",false);
        }
        final val neSingleResponseXmlString = decryptString(nameEnquirySingleItemResponse.getReturn());
        iMarker.setResponse("Clear Name Enquiry response  from NIBSS " +neSingleResponseXmlString);

        final val neSingleResponseVO = xmlUtil.unmarshal(neSingleResponseXmlString, NESingleResponseVO.class);



        final val neSingleResponse = nipOutwardMapper.mapNESingleResponseVO.apply(neSingleResponseVO);
        neSingleResponse.setSessionId(sessionId);
        neSingleResponse.setOriginalRequestId(requestId);
        iMarker.info(" NESingleResponse " + neSingleResponse.toString());
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
            iMarker.setRequest(" Clear ftSingleCreditRequestXml String ", ftSingleCreditRequestXmlString);
            final val encryptedXmlString = encryptString(ftSingleCreditRequestXmlString);
            iMarker.setRequest(" Encrypted ftSingleCreditRequestXml String ", encryptedXmlString);

            final val fundtransfersingleitemDc = new FundtransfersingleitemDc();
            fundtransfersingleitemDc.setRequest(encryptedXmlString);
            iMarker.info(" Sending Request to NIPOutwardWS ");
            final val fundTransferSingleItemDcResponse = nipOutwardWS.fundsTransfer(iMarker, fundtransfersingleitemDc);
            iMarker.info(" Received  Response from NIPOutwardWS ");
            iMarker.setResponse(" Encrypted  Response from NIPOutwardWS : FT "+ fundTransferSingleItemDcResponse.getReturn());
            if(StringUtils.isEmpty(fundTransferSingleItemDcResponse.getReturn())){

                //update db
                fundsTransferDbService.updateFTResponseCode(sessionId, NIP_106.getCode());
                //call client endpoint
                return;
            }

            final val ftSingleItemDcResponseXmlString = decryptString(fundTransferSingleItemDcResponse.getReturn());
            iMarker.setResponse(" Clear  Response from NIPOutwardWS : FT  " + ftSingleItemDcResponseXmlString);

            final val ftSingleCreditResponseVO = xmlUtil.unmarshal(ftSingleItemDcResponseXmlString, FTSingleCreditResponseVO.class);
            iMarker.info(" FTSingleCreditResponseVO " + ftSingleCreditResponseVO.toString());

            //simulate a waiting period of 60secs
            /* */
            log.info("----------- Stimulating  {} milliseconds wait ---------------",nipConfig.getTsqWaitTime());
            Thread.sleep(nipConfig.getTsqWaitTime());
            log.info("----------- Completed  {} milliseconds wait ---------------",nipConfig.getTsqWaitTime());

            final val tsqSingleItemRequestVO = nipOutwardMapper.buildTsqSingleItemRequestVO(ftSingleCreditResponseVO.getSessionId());

            String tsqSingleItemRequestXmlString = xmlUtil.marshal(TsqSingleItemRequestVO.class, tsqSingleItemRequestVO);

            final val encryptedTsqSingleItemRequestXmlString = encryptString(tsqSingleItemRequestXmlString);
            iMarker.setRequest(" Encrypted tsqSingleItemRequestXmlString  to NIBSS ", encryptedTsqSingleItemRequestXmlString);

            final val txnstatusquerysingleitem = new Txnstatusquerysingleitem();
            txnstatusquerysingleitem.setRequest(encryptedTsqSingleItemRequestXmlString);

            final val txnStatusQuerySingleItemResponse = nipOutwardWS.txnStatus(iMarker, txnstatusquerysingleitem);
            iMarker.setResponse(" Encrypted  Response from NIPOutwardWS : TSQ "+ txnStatusQuerySingleItemResponse.getReturn());

            final val tsqSingleItemResponseXmlString = decryptString(txnStatusQuerySingleItemResponse.getReturn());
            iMarker.setResponse(" Clear  Response from NIPOutwardWS TSQ "+ tsqSingleItemResponseXmlString);

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
    private String encryptString(String clearString){
        return ssmUtil.encryptRequest(clearString);
    }

    private String decryptString(String encryptedString){
        return ssmUtil.decryptResponse(encryptedString);
    }
}
