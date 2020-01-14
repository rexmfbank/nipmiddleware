package com.globalaccelerex.nipmiddleware.service.ws;

import com.globalaccelerex.nipmiddleware.config.AppConfig;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.Nameenquirysingleitem;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.NameenquirysingleitemResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.bind.JAXBElement;

import static com.globalaccelerex.nipmiddleware.api.NipAPI.NAME_ENQUIRY_OUTWARD;


@Service
public class NIPOutwardWS extends WebServiceGatewaySupport {

    @Autowired
    private AppConfig appConfig;


    public NameenquirysingleitemResponse nameEnquiry(IMarker marker, Nameenquirysingleitem nameEnquirySingleItem) {

        val webServiceURL = appConfig.getBaseUrl();
        //marker.info("webservice URL "+webServiceURL);
        //val soapActionURL = appConfig.getSoapActionUrl() + "nameenquirysingleitemRequest";
        val soapActionURL = appConfig.getSoapActionUrl() + NAME_ENQUIRY_OUTWARD;
        NameenquirysingleitemResponse nameenquirysingleitemResponse = new NameenquirysingleitemResponse();
        marker.info(" ====== Doing NameEnquiry ====== ");
        try {
            val response = (JAXBElement<NameenquirysingleitemResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(webServiceURL, nameEnquirySingleItem,
                            message -> ((SoapMessage) message).setSoapAction(soapActionURL));
            nameenquirysingleitemResponse = response.getValue();
            marker.info("Response String :::: " + nameenquirysingleitemResponse.getReturn());
        } catch (Exception ex) {
            marker.info(ex);
            throw new NIPMiddleWareAPIException("Error",ex.getMessage(),false);
        }
        marker.info("done with Name Enquiry");
        return nameenquirysingleitemResponse;
    }
}
