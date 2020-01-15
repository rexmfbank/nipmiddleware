package com.globalaccelerex.nipmiddleware.endpoint;


import com.globalaccelerex.nipmiddleware.facade.FTInwardFacade;
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

    @Autowired
    public NIPInwardEndpoint(NIPInwardFacade nipInwardFacade, FTInwardFacade ftInwardFacade){
        this.nipInwardFacade = nipInwardFacade;
        this.ftInwardFacade = ftInwardFacade;
    }

    @PayloadRoot(namespace = INWARD_TARGET_NAMESPACE, localPart = NAME_ENQUIRY_REQUEST)
    public @ResponsePayload  JAXBElement<NameenquirysingleitemResponse> handleNE(@RequestPayload JAXBElement<Nameenquirysingleitem> nameEnquirySingleItem) {
        log.info("<<< NameEnquiry >>>");
        final val iMarker = Marker.fromString();
        try{
            iMarker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), nameEnquirySingleItem.getValue().toString(), false);
            final val objectFactory = new ObjectFactory();
            final val nameEnquirySingleItemResponse = nipInwardFacade.handleNameEnquiry(nameEnquirySingleItem.getValue(),iMarker);
            iMarker.setMainResponse(nameEnquirySingleItemResponse.toString() , false);
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
                    build().toUri().toASCIIString(), financialinstitutionlist.getValue().toString(), false);
            final val objectFactory = new ObjectFactory();
            final val financialInstitutionListResponse = nipInwardFacade.handleFI(financialinstitutionlist.getValue(), iMarker);
            iMarker.setMainResponse(financialInstitutionListResponse.toString() , false);
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
                    build().toUri().toASCIIString(), fundtransfersingleitemDd.getValue().toString(), false);
            final val objectFactory = new ObjectFactory();
            final val fundtransfersingleitemDdResponse = ftInwardFacade.handleFT_DD(fundtransfersingleitemDd.getValue(), iMarker);
            iMarker.setMainResponse(fundtransfersingleitemDdResponse.toString() , false);
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
                    build().toUri().toASCIIString(), fundtransfersingleitemDc.getValue().toString(), false);
            final val objectFactory = new ObjectFactory();


            final val fundtransfersingleitemDcResponse = ftInwardFacade.handleFT_DC(fundtransfersingleitemDc.getValue(), iMarker);
            iMarker.setMainResponse(fundtransfersingleitemDcResponse.toString() , false);
            return objectFactory.createFundtransfersingleitemDcResponse(fundtransfersingleitemDcResponse);
        }finally {
            iMarker.done();
        }

    }
}
