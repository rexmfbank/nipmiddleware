package com.globalaccelerex.nipmiddleware.service.ws;

import com.globalaccelerex.nipmiddleware.config.AppConfig;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.bind.JAXBElement;

import static com.globalaccelerex.nipmiddleware.api.NipAPI.*;


@Service
public class NIPOutwardWS extends WebServiceGatewaySupport {

    @Autowired
    private AppConfig appConfig;


    public NameenquirysingleitemResponse nameEnquiry(IMarker marker, Nameenquirysingleitem nameEnquirySingleItem) {

        val webServiceURL = appConfig.getBaseUrl();
        val soapActionURL = appConfig.getSoapActionUrl() + NAME_ENQUIRY_OUTWARD;
        NameenquirysingleitemResponse nameenquirysingleitemResponse = new NameenquirysingleitemResponse();
        marker.info(" ====== Doing NameEnquiry ====== ");
        try {
            val response = (JAXBElement<NameenquirysingleitemResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(webServiceURL, nameEnquirySingleItem,
                            message -> ((SoapMessage) message).setSoapAction(soapActionURL));
            nameenquirysingleitemResponse = response.getValue();
        } catch (Exception ex) {
            marker.info(ex);
            throw new NIPMiddleWareAPIException("Error",ex.getMessage());
        }
        marker.info("done with Name Enquiry");
        return nameenquirysingleitemResponse;
    }

    public FundtransfersingleitemDcResponse fundsTransfer(IMarker marker, FundtransfersingleitemDc fundtransfersingleitemDc) {

        val webServiceURL = appConfig.getBaseUrl();
        val soapActionURL =appConfig.getSoapActionUrl() + FUNDS_TRANSFER_OUTWARD;
        FundtransfersingleitemDcResponse fundTransferSingleItemDcResponse = new FundtransfersingleitemDcResponse();
        marker.info(" ====== Doing FundsTransfer ====== ");
        try {
            val response = (JAXBElement<FundtransfersingleitemDcResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(webServiceURL, fundtransfersingleitemDc,
                            message -> ((SoapMessage) message).setSoapAction(soapActionURL));
            fundTransferSingleItemDcResponse = response.getValue();
        } catch (Exception ex) {
            marker.info(ex);
            throw new NIPMiddleWareAPIException("Error",ex.getMessage());
        }
        marker.info("done with FundsTransfer DC");
        return fundTransferSingleItemDcResponse;
    }

    public TxnstatusquerysingleitemResponse txnStatus(IMarker marker, Txnstatusquerysingleitem txnstatusquerysingleitem){
        val webServiceURL = appConfig.getTsqUrl();
        val soapActionURL = appConfig.getSoapActionUrl() + TSQ_OUTWARD;

        TxnstatusquerysingleitemResponse txnStatusQuerySingleItemResponse = new TxnstatusquerysingleitemResponse();
        marker.info(" ====== Doing TSQ ====== ");
        try{
            val response = (JAXBElement<TxnstatusquerysingleitemResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(webServiceURL, txnstatusquerysingleitem,
                            message -> ((SoapMessage) message).setSoapAction(soapActionURL));
            txnStatusQuerySingleItemResponse = response.getValue();
        }catch (Exception ex) {
            marker.info(ex);
            throw new NIPMiddleWareAPIException("Error",ex.getMessage());
        }
        marker.info("done with  TSQ");
        return txnStatusQuerySingleItemResponse;

    }
}
