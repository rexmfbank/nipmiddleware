package com.globalaccelerex.nipmiddleware.endpoint;


import com.globalaccelerex.nipmiddleware.facade.inward.FTInwardFacade;
import com.globalaccelerex.nipmiddleware.facade.inward.LienInwardFacade;
import com.globalaccelerex.nipmiddleware.facade.inward.NIPInwardFacade;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import static com.globalaccelerex.nipmiddleware.api.NipAPI.*;

@Slf4j
@Endpoint
public class NIPInwardEndpoint {

    private final NIPInwardFacade nipInwardFacade;

    private final FTInwardFacade ftInwardFacade;

    private final LienInwardFacade lienInwardFacade;

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public NIPInwardEndpoint(NIPInwardFacade nipInwardFacade, FTInwardFacade ftInwardFacade, LienInwardFacade lienInwardFacade, HttpServletRequest httpServletRequest){
        this.nipInwardFacade = nipInwardFacade;
        this.ftInwardFacade = ftInwardFacade;
        this.lienInwardFacade = lienInwardFacade;
        this.httpServletRequest = httpServletRequest;
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = NAME_ENQUIRY_REQUEST)
    public @ResponsePayload  JAXBElement<String> handleNE(@RequestPayload JAXBElement<String> nameEnquirySingleItem) {
        final val iMarker = Marker.fromString();
        iMarker.info("<<< NameEnquiry >>>");
        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , nameEnquirySingleItem.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val nameEnquirySingleItemResponse = nipInwardFacade.handleNameEnquiry(nameEnquirySingleItem.getValue(),iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(nameEnquirySingleItemResponse.getReturn() , false);

            return new JAXBElement<>(new QName(NAME_ENQUIRY_REQUEST),
                    String.class, nameEnquirySingleItemResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FI_LIST_REQUEST)
    public @ResponsePayload JAXBElement<String> updateFI(@RequestPayload JAXBElement<String> financialinstitutionlist){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< FIList >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , financialinstitutionlist.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val financialInstitutionListResponse = nipInwardFacade.handleFI(financialinstitutionlist.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(financialInstitutionListResponse.getReturn() , false);
            return new JAXBElement<>(new QName(FI_LIST_REQUEST),String.class, financialInstitutionListResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_DIRECT_DEBIT_REQUEST)
    public @ResponsePayload JAXBElement<String> handleFT_DD(@RequestPayload JAXBElement<String> fundtransfersingleitemDd){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< FT Single Item Dd >>>");


        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , fundtransfersingleitemDd.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val fundtransfersingleitemDdResponse = ftInwardFacade.handleFTDirectDebit(fundtransfersingleitemDd.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(fundtransfersingleitemDdResponse.getReturn() , false);
            return new JAXBElement<>(new QName(FT_DIRECT_DEBIT_REQUEST),String.class, fundtransfersingleitemDdResponse.getReturn());
        }finally {
            iMarker.done();
        }

    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_DIRECT_CREDIT_REQUEST)
    public @ResponsePayload JAXBElement<String> handleFT_DC(@RequestPayload JAXBElement<String> fundtransfersingleitemDc){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< FT Single Item Dc >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , fundtransfersingleitemDc.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val fundtransfersingleitemDcResponse = ftInwardFacade.handleFTDirectCredit(fundtransfersingleitemDc.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(fundtransfersingleitemDcResponse.getReturn() , false);
            return new JAXBElement<>(new QName(FT_DIRECT_CREDIT_REQUEST),String.class, fundtransfersingleitemDcResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = TSQ_REQUEST)
    public @ResponsePayload JAXBElement<String> handleTSQ(@RequestPayload JAXBElement<String> txnStatusQuerySingleItem){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< TSQ >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , txnStatusQuerySingleItem.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val txnStatusQuerySingleItemResponse = nipInwardFacade.handleTSQ(txnStatusQuerySingleItem.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(txnStatusQuerySingleItemResponse.getReturn() , false);
            return new JAXBElement<>(new QName(TSQ_REQUEST),String.class, txnStatusQuerySingleItemResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_ADVICE_DIRECT_CREDIT_REQUEST)
    public @ResponsePayload JAXBElement<String> handleFundTransferAdvice_DC(@RequestPayload JAXBElement<String> fundtransferAdviceDC){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< FT Advice DC >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , fundtransferAdviceDC.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val fundtransferAdviceDcResponse = ftInwardFacade.handleFTAdviceDirectCredit(fundtransferAdviceDC.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(fundtransferAdviceDcResponse.getReturn() , false);
            return new JAXBElement<>(new QName(FT_ADVICE_DIRECT_CREDIT_REQUEST),String.class, fundtransferAdviceDcResponse.getReturn());
        }finally {
            iMarker.done();
        }


    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_ADVICE_DIRECT_DEBIT_REQUEST)
    public @ResponsePayload JAXBElement<String> handleFundTransferAdvice_DD(@RequestPayload JAXBElement<String> fundtransferAdviceDD){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< FT Advice DD >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , fundtransferAdviceDD.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val fundtransferAdviceDdResponse = ftInwardFacade.handleFTAdviceDirectDebit(fundtransferAdviceDD.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(fundtransferAdviceDdResponse.getReturn() , false);
            return new JAXBElement<>(new QName(FT_ADVICE_DIRECT_DEBIT_REQUEST),String.class, fundtransferAdviceDdResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart =MANDATE_ADVICE_REQUEST)
    public @ResponsePayload JAXBElement<String> handleMandateAdvice(@RequestPayload JAXBElement<String> mandateAdvice){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< Mandate Advice >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , mandateAdvice.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val mandateAdviceResponse = nipInwardFacade.handleMandateAdvice(mandateAdvice.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(mandateAdviceResponse.getReturn() , false);
            return new JAXBElement<>(new QName(MANDATE_ADVICE_REQUEST),String.class, mandateAdviceResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart =ACCOUNT_BLOCK_REQUEST)
    public @ResponsePayload JAXBElement<String> handleAccountBlock(@RequestPayload JAXBElement<String> accountblock) {
        final val iMarker = Marker.fromString();
        iMarker.info("<<< Account Block >>>");

        try {
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , accountblock.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val accountBlockResponse = lienInwardFacade.handleAccountBlock(accountblock.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(accountBlockResponse.getReturn(), false);
            return new JAXBElement<>(new QName(ACCOUNT_BLOCK_REQUEST),String.class, accountBlockResponse.getReturn());
        } finally {
            iMarker.done();
        }
    }


    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart =ACCOUNT_UNBLOCK_REQUEST)
    public @ResponsePayload JAXBElement<String> handleAccountUnblock(@RequestPayload JAXBElement<String> accountUnblock){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< Account Unblock >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , accountUnblock.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val accountunblockResponse = lienInwardFacade.handleAccountUnblock(accountUnblock.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(accountunblockResponse.getReturn() , false);
            return new JAXBElement<>(new QName(ACCOUNT_UNBLOCK_REQUEST),String.class, accountunblockResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = AMOUNT_BLOCK_REQUEST)
    public @ResponsePayload JAXBElement<String> handleAmountBlock(@RequestPayload JAXBElement<String> amountblock){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< AmountBlock >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , amountblock.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val amountBlockResponse = lienInwardFacade.handleAmountBlock(amountblock.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(amountBlockResponse.getReturn() , false);
            return new JAXBElement<>(new QName(AMOUNT_BLOCK_REQUEST),String.class, amountBlockResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = AMOUNT_UNBLOCK_REQUEST)
    public @ResponsePayload JAXBElement<String> handleAmountUnblock(@RequestPayload JAXBElement<String> amountunblock){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< AmountUnBlock >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , amountunblock.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val amountUnblockResponse = lienInwardFacade.handleAmountUnblock(amountunblock.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(amountUnblockResponse.getReturn() , false);
            return new JAXBElement<>(new QName(AMOUNT_UNBLOCK_REQUEST),String.class, amountUnblockResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @ResponsePayload
    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = BALANCE_ENQUIRY_REQUEST)
    public  JAXBElement<String> handleBalanceEnquiry(@RequestPayload JAXBElement<String> balanceEnquiry){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< Balance Enquiry >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , balanceEnquiry.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");

            final val balanceEnquiryResponse = nipInwardFacade.handleBalanceEnquiry(balanceEnquiry.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(balanceEnquiryResponse.getReturn() , false);
            return new JAXBElement<>(new QName(BALANCE_ENQUIRY_REQUEST),String.class, balanceEnquiryResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart =FT_CREDIT_ACKNOWLEDGEMENT_REQUEST)
    public @ResponsePayload JAXBElement<String> handleFTAckCredit(@RequestPayload JAXBElement<String> ftackcreditrequest){
        final val iMarker = Marker.fromString();
        iMarker.info("<<< Ft Acknowledgement Credit >>>");

        try{
            val requestURI = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
            iMarker.setMainRequest(requestURI , ftackcreditrequest.getValue()+String.format( " from %s",httpServletRequest.getRemoteAddr()), false);

            val originatingInstitutionCode = StringUtils.substringBefore(StringUtils.substringAfter(requestURI,"ws/"), "/");
            final val ftAckCreditRequestResponse = ftInwardFacade.handleFTAckCredit(ftackcreditrequest.getValue(), iMarker,originatingInstitutionCode);
            iMarker.setMainResponse(ftAckCreditRequestResponse.getReturn() , false);
            return new JAXBElement<>(new QName(FT_CREDIT_ACKNOWLEDGEMENT_REQUEST),String.class, ftAckCreditRequestResponse.getReturn());
        }finally {
            iMarker.done();
        }
    }
}
