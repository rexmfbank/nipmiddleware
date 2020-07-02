package com.globalaccelerex.nipmiddleware.facade.inward;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.*;
import com.globalaccelerex.nipmiddleware.service.NIPInwardService;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NIPInwardFacade extends AbstractInwardFacade{

    @Autowired
    private NIPInwardService nipInwardService;

    @Autowired
    public NIPInwardFacade(SSMUtil ssmUtil, XmlUtil xmlUtil, NipConfig nipConfig) {
        super(ssmUtil, xmlUtil, nipConfig);
    }

    public NameenquirysingleitemResponse handleNameEnquiry(Nameenquirysingleitem nameenquirysingleitem, IMarker marker, String originatingInstitutionCode){
        final val nameEnquirySingleItemResponse = new NameenquirysingleitemResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                nameEnquirySingleItemResponse.setReturn(nameenquirysingleitem.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else{
                final val encryptedNEString = nameenquirysingleitem.getRequest();
                final val clearNEString = nipConfig.isIgnoreEncryption() ? encryptedNEString : decryptString(encryptedNEString,originatingInstitutionCode,marker);

                marker.setRequest(" NameEnquiry Clear String ",clearNEString);

                final val neSingleRequestVO = xmlUtil.unmarshal(clearNEString, NESingleRequestVO.class);
                //connect to backend service to retrieve the name
                // add a logic to determine which backend CBA to call based on destination code
                final val neSingleResponseVO = nipInwardService.handleNameEnquiry(neSingleRequestVO);

                marker.setResponse("Response from NameEnquiry CBA " + neSingleResponseVO.toString());

                final val neSingleResponseVOXmlString = xmlUtil.marshal(NESingleResponseVO.class, neSingleResponseVO);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? neSingleResponseVOXmlString : encryptString(neSingleResponseVOXmlString,originatingInstitutionCode,marker);

                nameEnquirySingleItemResponse.setReturn(encryptedXmlString);
            }

        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            nameEnquirySingleItemResponse.setReturn("");
        }
        return nameEnquirySingleItemResponse;
    }

    public FinancialinstitutionlistResponse handleFI (Financialinstitutionlist financialinstitutionlist, IMarker marker, String originatingInstitutionCode){
        val financialinstitutionlistResponse = new FinancialinstitutionlistResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                financialinstitutionlistResponse.setReturn(financialinstitutionlist.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else {
                final val encryptedFIListString = financialinstitutionlist.getRequest();
                final val clearFIListString = nipConfig.isIgnoreEncryption() ? encryptedFIListString : decryptString(encryptedFIListString,originatingInstitutionCode,marker);

                marker.setRequest(" FI List Clear String ",clearFIListString);

                final val financialInstitutionListRequest = xmlUtil.unmarshal(clearFIListString, FinancialInstitutionListRequestVO.class);

                val financialInstitutionListResponse = nipInwardService.handleFIList(financialInstitutionListRequest);

                marker.setResponse("Response from FIList CBA " + financialInstitutionListResponse.toString());

                val financialInstitutionListResponseXmlString = xmlUtil.marshal(FinancialInstitutionListResponseVO.class, financialInstitutionListResponse);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? financialInstitutionListResponseXmlString :  encryptString(financialInstitutionListResponseXmlString,originatingInstitutionCode,marker);

                financialinstitutionlistResponse.setReturn(encryptedXmlString);
            }

        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            financialinstitutionlistResponse.setReturn("");
        }

        return financialinstitutionlistResponse;
    }

    public TxnstatusquerysingleitemResponse handleTSQ(Txnstatusquerysingleitem txnstatusquerysingleitem, IMarker marker, String originatingInstitutionCode){

        final val txnstatusquerysingleitemResponse = new TxnstatusquerysingleitemResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                txnstatusquerysingleitemResponse.setReturn(txnstatusquerysingleitem.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else {
                final val encryptedTsqString = txnstatusquerysingleitem.getRequest();
                final val clearTsqString = nipConfig.isIgnoreEncryption() ? encryptedTsqString :
                        decryptString(encryptedTsqString,originatingInstitutionCode,marker);

                marker.setRequest(" TSQ Clear String ",clearTsqString);

                final val tsQuerySingleRequestVO = xmlUtil.unmarshal(clearTsqString, TSQuerySingleRequestVO.class);
                // some backend calls

                final val tsQuerySingleResponseVO = nipInwardService.handleTSQ(tsQuerySingleRequestVO);

                marker.setResponse("Response from TSQ CBA " + tsQuerySingleResponseVO.toString());

                final val tsQuerySingleResponseVOXmlString = xmlUtil.marshal(TSQuerySingleResponseVO.class, tsQuerySingleResponseVO);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? tsQuerySingleResponseVOXmlString :
                        encryptString(tsQuerySingleResponseVOXmlString,originatingInstitutionCode,marker);

                txnstatusquerysingleitemResponse.setReturn(encryptedXmlString);
            }

        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            txnstatusquerysingleitemResponse.setReturn("");
        }
        return txnstatusquerysingleitemResponse;

    }

    public MandateadviceResponse handleMandateAdvice(Mandateadvice mandateadvice, IMarker marker, String originatingInstitutionCode){
        final val mandateAdviceResponse = new MandateadviceResponse();
        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                mandateAdviceResponse.setReturn(mandateadvice.getRequest());
                marker.setResponse("Sending Encrypted Request as response ");
            }else {
                final val encryptedMandateAdviceString = mandateadvice.getRequest();
                final val clearMandateAdviceString = nipConfig.isIgnoreEncryption() ? encryptedMandateAdviceString : decryptString(encryptedMandateAdviceString,originatingInstitutionCode,marker);

                marker.setRequest(" Mandate Advice Clear String ",clearMandateAdviceString);

                final val mandateAdviceRequestVO = xmlUtil.unmarshal(clearMandateAdviceString, MandateAdviceRequestVO.class);

                // some backend calls

                final val mandateAdviceResponseVO = nipInwardService.handleMandateAdvice(mandateAdviceRequestVO);

                marker.setResponse("Response from Mandate Advice CBA " + mandateAdviceResponseVO.toString());

                final val mandateAdviceResponseVOXmlString = xmlUtil.marshal(MandateAdviceResponseVO.class, mandateAdviceResponseVO);

                final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? mandateAdviceResponseVOXmlString : encryptString(mandateAdviceResponseVOXmlString,originatingInstitutionCode,marker);

                mandateAdviceResponse.setReturn(encryptedXmlString);
            }
        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            mandateAdviceResponse.setReturn("");
        }

        return mandateAdviceResponse;
    }

    public BalanceenquiryResponse handleBalanceEnquiry(Balanceenquiry balanceenquiry, IMarker marker, String originatingInstitutionCode){
        final val balanceEnquiryResponse = new BalanceenquiryResponse();

        try{
            if(StringUtils.isBlank(originatingInstitutionCode)){
                marker.setRequest(" Originating Institution Code is not available ","");
                balanceEnquiryResponse.setReturn(balanceenquiry.getRequest());
                marker.setResponse("Sending Encrypted Request back ");
            }else {
                final val encryptedBalanceEnquiryString = balanceenquiry.getRequest();
                final val clearBalanceEnquiryString = nipConfig.isIgnoreEncryption() ? encryptedBalanceEnquiryString : decryptString(encryptedBalanceEnquiryString,originatingInstitutionCode,marker);

                marker.setRequest(" Balance Enquiry Clear String ",clearBalanceEnquiryString);

                final val balanceEnquiryRequestVO = xmlUtil.unmarshal(clearBalanceEnquiryString, BalanceEnquiryRequestVO.class);

                //some backend calls

                final val balanceEnquiryResponseVO = nipInwardService.handleBalanceEnquiry(balanceEnquiryRequestVO);

                marker.setResponse("Response from balance Enquiry CBA " + balanceEnquiryResponseVO.toString());

                final val balanceEnquiryResponseVOXmlString = xmlUtil.marshal(BalanceEnquiryResponseVO.class, balanceEnquiryResponseVO);

                final val encryptedXmlString =nipConfig.isIgnoreEncryption() ?balanceEnquiryResponseVOXmlString :  encryptString(balanceEnquiryResponseVOXmlString,originatingInstitutionCode,marker);

                balanceEnquiryResponse.setReturn(encryptedXmlString);
            }
        }catch (Exception exception){
            if(exception instanceof NIPMiddleWareAPIException){
                val nipMiddleWareAPIException =(NIPMiddleWareAPIException) exception;
                marker.info(nipMiddleWareAPIException.getErrorResponse().getResponseMessage(),nipMiddleWareAPIException);
            }else {
                marker.info(exception.getMessage(),exception);
            }
            balanceEnquiryResponse.setReturn("");
        }

        return balanceEnquiryResponse;
    }

}
