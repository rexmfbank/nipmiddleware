package com.globalaccelerex.nipmiddleware.endpoint;


import com.globalaccelerex.nipmiddleware.facade.FTInwardFacade;
import com.globalaccelerex.nipmiddleware.facade.LienInwardFacade;
import com.globalaccelerex.nipmiddleware.facade.NIPInwardFacade;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;

import static com.globalaccelerex.nipmiddleware.api.NipAPI.*;

@Slf4j
@Endpoint
public class NIPInwardEndpoint {

    private final NIPInwardFacade nipInwardFacade;

    private final FTInwardFacade ftInwardFacade;

    private final LienInwardFacade lienInwardFacade;

    @Autowired
    public NIPInwardEndpoint(NIPInwardFacade nipInwardFacade, FTInwardFacade ftInwardFacade, LienInwardFacade lienInwardFacade){
        this.nipInwardFacade = nipInwardFacade;
        this.ftInwardFacade = ftInwardFacade;
        this.lienInwardFacade = lienInwardFacade;
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = NAME_ENQUIRY_REQUEST)
    public @ResponsePayload  JAXBElement<NameenquirysingleitemResponse> handleNE(@RequestPayload JAXBElement<Nameenquirysingleitem> nameEnquirySingleItem) {
        log.info("<<< NameEnquiry >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), nameEnquirySingleItem.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val nameEnquirySingleItemResponse = nipInwardFacade.handleNameEnquiry(nameEnquirySingleItem.getValue(),iMarker);
            iMarker.setMainResponse(nameEnquirySingleItemResponse.getReturn() , false);
            return objectFactory.createNameenquirysingleitemResponse(nameEnquirySingleItemResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FI_LIST_REQUEST)
    public @ResponsePayload JAXBElement<FinancialinstitutionlistResponse> updateFI(@RequestPayload JAXBElement<Financialinstitutionlist> financialinstitutionlist){
        log.info("<<< FIList >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), financialinstitutionlist.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val financialInstitutionListResponse = nipInwardFacade.handleFI(financialinstitutionlist.getValue(), iMarker);
            iMarker.setMainResponse(financialInstitutionListResponse.getReturn() , false);
            return objectFactory.createFinancialinstitutionlistResponse(financialInstitutionListResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_DIRECT_DEBIT_REQUEST)
    public @ResponsePayload JAXBElement<FundtransfersingleitemDdResponse> handleFT_DD(@RequestPayload JAXBElement<FundtransfersingleitemDd> fundtransfersingleitemDd){
        log.info("<<< FT Single Item Dd >>>");
        final val iMarker = Marker.fromString();

        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), fundtransfersingleitemDd.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val fundtransfersingleitemDdResponse = ftInwardFacade.handleFT_DD(fundtransfersingleitemDd.getValue(), iMarker);
            iMarker.setMainResponse(fundtransfersingleitemDdResponse.getReturn() , false);
            return objectFactory.createFundtransfersingleitemDdResponse(fundtransfersingleitemDdResponse);
        }finally {
            iMarker.done();
        }

    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_DIRECT_CREDIT_REQUEST)
    public @ResponsePayload JAXBElement<FundtransfersingleitemDcResponse> handleFT_DC(@RequestPayload JAXBElement<FundtransfersingleitemDc> fundtransfersingleitemDc){
        log.info("<<< FT Single Item Dc >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), fundtransfersingleitemDc.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val fundtransfersingleitemDcResponse = ftInwardFacade.handleFT_DC(fundtransfersingleitemDc.getValue(), iMarker);
            iMarker.setMainResponse(fundtransfersingleitemDcResponse.getReturn() , false);
            return objectFactory.createFundtransfersingleitemDcResponse(fundtransfersingleitemDcResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = TSQ_REQUEST)
    public @ResponsePayload JAXBElement<TxnstatusquerysingleitemResponse> handleTSQ(@RequestPayload JAXBElement<Txnstatusquerysingleitem> txnStatusQuerySingleItem){
        log.info("<<< TSQ >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), txnStatusQuerySingleItem.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val txnStatusQuerySingleItemResponse = nipInwardFacade.handleTSQ(txnStatusQuerySingleItem.getValue(), iMarker);
            iMarker.setMainResponse(txnStatusQuerySingleItemResponse.getReturn() , false);
            return objectFactory.createTxnstatusquerysingleitemResponse(txnStatusQuerySingleItemResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_ADVICE_DIRECT_CREDIT_REQUEST)
    public @ResponsePayload JAXBElement<FundtransferAdviceDcResponse> handleFundTransferAdvice_DC(@RequestPayload JAXBElement<FundtransferAdviceDc> fundtransferAdviceDC){
        log.info("<<< FT Advice DC >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), fundtransferAdviceDC.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val fundtransferAdviceDcResponse = ftInwardFacade.handleFTAdvice_DC(fundtransferAdviceDC.getValue(), iMarker);
            iMarker.setMainResponse(fundtransferAdviceDcResponse.getReturn() , false);
            return objectFactory.createFundtransferAdviceDcResponse(fundtransferAdviceDcResponse);
        }finally {
            iMarker.done();
        }


    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = FT_ADVICE_DIRECT_DEBIT_REQUEST)
    public @ResponsePayload JAXBElement<FundtransferAdviceDdResponse> handleFundTransferAdvice_DD(@RequestPayload JAXBElement<FundtransferAdviceDd> fundtransferAdviceDD){
        log.info("<<< FT Advice DD >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), fundtransferAdviceDD.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val fundtransferAdviceDdResponse = ftInwardFacade.handleFTAdvice_DD(fundtransferAdviceDD.getValue(), iMarker);
            iMarker.setMainResponse(fundtransferAdviceDdResponse.getReturn() , false);
            return objectFactory.createFundtransferAdviceDdResponse(fundtransferAdviceDdResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart =MANDATE_ADVICE_REQUEST)
    public @ResponsePayload JAXBElement<MandateadviceResponse> handleMandateAdvice(@RequestPayload JAXBElement<Mandateadvice> mandateAdvice){
        log.info("<<< Mandate Advice >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), mandateAdvice.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val mandateAdviceResponse = nipInwardFacade.handleMandateAdvice(mandateAdvice.getValue(), iMarker);
            iMarker.setMainResponse(mandateAdviceResponse.getReturn() , false);
            return objectFactory.createMandateadviceResponse(mandateAdviceResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart =ACCOUNT_BLOCK_REQUEST)
    public @ResponsePayload JAXBElement<AccountblockResponse> handleAccountBlock(@RequestPayload JAXBElement<Accountblock> accountblock) {
        log.info("<<< Account Block >>>");
        final val iMarker = Marker.fromString();
        try {
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), accountblock.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val accountBlockResponse = lienInwardFacade.handleAccountBlock(accountblock.getValue(), iMarker);
            iMarker.setMainResponse(accountBlockResponse.getReturn(), false);
            return objectFactory.createAccountblockResponse(accountBlockResponse);
        } finally {
            iMarker.done();
        }
    }


    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart =ACCOUNT_UNBLOCK_REQUEST)
    public @ResponsePayload JAXBElement<AccountunblockResponse> handleAccountUnblock(@RequestPayload JAXBElement<Accountunblock> accountUnblock){
        log.info("<<< Account Unblock >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), accountUnblock.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val accountunblockResponse = lienInwardFacade.handleAccountUnblock(accountUnblock.getValue(), iMarker);
            iMarker.setMainResponse(accountunblockResponse.getReturn() , false);
            return objectFactory.createAccountunblockResponse(accountunblockResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = AMOUNT_BLOCK_REQUEST)
    public @ResponsePayload JAXBElement<AmountblockResponse> handleAmountBlock(@RequestPayload JAXBElement<Amountblock> amountblock){
        log.info("<<< AmountBlock >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), amountblock.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val amountBlockResponse = lienInwardFacade.handleAmountBlock(amountblock.getValue(), iMarker);
            iMarker.setMainResponse(amountBlockResponse.getReturn() , false);
            return objectFactory.createAmountblockResponse(amountBlockResponse);
        }finally {
            iMarker.done();
        }
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = AMOUNT_UNBLOCK_REQUEST)
    public @ResponsePayload JAXBElement<AmountunblockResponse> handleAmountUnblock(@RequestPayload JAXBElement<Amountunblock> amountunblock){
        log.info("<<< AmountUnBlock >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), amountunblock.getValue().getRequest(), false);
            final val objectFactory = new ObjectFactory();
            final val amountUnblockResponse = lienInwardFacade.handleAmountUnblock(amountunblock.getValue(), iMarker);
            iMarker.setMainResponse(amountUnblockResponse.getReturn() , false);
            return objectFactory.createAmountunblockResponse(amountUnblockResponse);
        }finally {
            iMarker.done();
        }
    }

    @ResponsePayload
    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = BALANCE_ENQUIRY_REQUEST)
    public  JAXBElement<BalanceenquiryResponse> handleBalanceEnquiry(@RequestPayload JAXBElement<Balanceenquiry> balanceEnquiry){
        log.info("<<< Balance Enquiry >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), balanceEnquiry.getValue().getRequest(), false);
        final val objectFactory = new ObjectFactory();
            final val balanceEnquiryResponse = nipInwardFacade.handleBalanceEnquiry(balanceEnquiry.getValue(), iMarker);
            return objectFactory.createBalanceenquiryResponse(balanceEnquiryResponse);
        }finally {
            iMarker.done();
        }
    }
}
