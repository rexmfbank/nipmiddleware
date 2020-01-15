package com.globalaccelerex.nipmiddleware.endpoint;


import com.globalaccelerex.nipmiddleware.facade.NIPInwardFacade;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Nameenquirysingleitem;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.NameenquirysingleitemResponse;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.ObjectFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;

import static com.globalaccelerex.nipmiddleware.api.NipAPI.INWARD_TARGET_NAMESPACE;
import static com.globalaccelerex.nipmiddleware.api.NipAPI.NAME_ENQUIRY_REQUEST;

@Slf4j
@Endpoint
public class NIPInwardEndpoint {

    private final NIPInwardFacade nipInwardFacade;

    @Autowired
    public NIPInwardEndpoint(NIPInwardFacade nipInwardFacade){
        this.nipInwardFacade = nipInwardFacade;
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
}
