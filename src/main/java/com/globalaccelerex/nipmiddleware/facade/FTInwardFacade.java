package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.*;
import com.globalaccelerex.nipmiddleware.payload.nip.ws.*;
import com.globalaccelerex.nipmiddleware.service.NIPInwardService;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FTInwardFacade extends AbstractInwardFacade{

    @Autowired
    private NIPInwardService nipInwardService;


    @Autowired
    public FTInwardFacade(SSMUtil ssmUtil, XmlUtil xmlUtil, NipConfig nipConfig) {
        super(ssmUtil,xmlUtil, nipConfig);
    }

    public FundtransfersingleitemDdResponse handleFT_DD(FundtransfersingleitemDd fundtransfersingleitemDd, IMarker marker, String originatingInstitutionCode){
        final val fundtransfersingleitemDdResponse = new FundtransfersingleitemDdResponse();
        try{
            final val encryptedFTDirectDebitString = fundtransfersingleitemDd.getRequest();
            final val clearFTDirectDebitString = nipConfig.isIgnoreEncryption() ? encryptedFTDirectDebitString: decryptString(encryptedFTDirectDebitString,originatingInstitutionCode,marker);

            marker.setRequest(" FT_Dd Clear String ",clearFTDirectDebitString);

            final val ftDirectDebitRequestVO = xmlUtil.unmarshal(clearFTDirectDebitString, FTDirectDebitRequestVO.class);

            // some backend calls

            final val ftDirectDebitResponseVO = nipInwardService.handleFT_DirectDebit(ftDirectDebitRequestVO);

            marker.setResponse("Response from FT_Dd CBA " + ftDirectDebitResponseVO.toString());

            final val ftDirectDebitResponseVOXmlString = xmlUtil.marshal(FTDirectDebitResponseVO.class, ftDirectDebitResponseVO);

            final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? ftDirectDebitResponseVOXmlString : encryptString(ftDirectDebitResponseVOXmlString,originatingInstitutionCode,marker);

            fundtransfersingleitemDdResponse.setReturn(encryptedXmlString);
        }catch (Exception exception){
            marker.info(exception.getMessage(),exception);
            fundtransfersingleitemDdResponse.setReturn("");
        }

        return fundtransfersingleitemDdResponse;
    }

    public FundtransfersingleitemDcResponse handleFT_DC(FundtransfersingleitemDc fundtransfersingleitemDc, IMarker marker, String originatingInstitutionCode){
        final val fundtransfersingleitemDcResponse = new FundtransfersingleitemDcResponse();
        try{
            final val encryptedFTSingleItemDCString = fundtransfersingleitemDc.getRequest();
            final val clearFTSingleItemDCString = nipConfig.isIgnoreEncryption() ? encryptedFTSingleItemDCString : decryptString(encryptedFTSingleItemDCString,originatingInstitutionCode,marker);

            marker.setRequest(" FT_Dc Clear String ",clearFTSingleItemDCString);

            final val ftDirectCreditRequestVO = xmlUtil.unmarshal(clearFTSingleItemDCString, FTDirectCreditRequestVO.class);

            // some backend calls

            final val ftDirectCreditResponseVO = nipInwardService.handleFT_DirectCredit(ftDirectCreditRequestVO);
            marker.setResponse("Response from FT_Dc CBA " + ftDirectCreditResponseVO.toString());


            final val ftDirectCreditResponseVOXmlString = xmlUtil.marshal(FTDirectCreditResponseVO.class, ftDirectCreditResponseVO);

            final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? ftDirectCreditResponseVOXmlString : encryptString(ftDirectCreditResponseVOXmlString,originatingInstitutionCode,marker);

            fundtransfersingleitemDcResponse.setReturn(encryptedXmlString);
        }catch (Exception exception){
            marker.info(exception.getMessage(),exception);
            fundtransfersingleitemDcResponse.setReturn("");
        }

        return fundtransfersingleitemDcResponse;
    }

    public FundtransferAdviceDcResponse handleFTAdvice_DC(FundtransferAdviceDc fundtransferAdviceDc, IMarker marker, String originatingInstitutionCode){
        final val fundtransferAdviceDcResponse = new FundtransferAdviceDcResponse();
        try{
            final val encryptedFTAdviceDirectCreditString = fundtransferAdviceDc.getRequest();
            final val clearFTAdviceDirectCreditString = nipConfig.isIgnoreEncryption() ? encryptedFTAdviceDirectCreditString : decryptString(encryptedFTAdviceDirectCreditString,originatingInstitutionCode,marker);

            marker.setRequest(" FT_Advice Dc Clear String ",clearFTAdviceDirectCreditString);

            final val ftAdviceDirectCreditRequestVO = xmlUtil.unmarshal(clearFTAdviceDirectCreditString, FTAdviceDirectCreditRequestVO.class);

            //some backend calls

            final val ftAdviceDirectCreditResponseVO = nipInwardService.handleFTAdviceDirectCredit(ftAdviceDirectCreditRequestVO);
            marker.setResponse("Response from FT_Advice_DC CBA " + ftAdviceDirectCreditResponseVO.toString());

            final val ftAdviseDirectCreditResponseVOXmlString = xmlUtil.marshal(FTAdviceDirectCreditResponseVO.class, ftAdviceDirectCreditResponseVO);

            final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? ftAdviseDirectCreditResponseVOXmlString : encryptString(ftAdviseDirectCreditResponseVOXmlString,originatingInstitutionCode,marker);

            fundtransferAdviceDcResponse.setReturn(encryptedXmlString);
        }catch (Exception exception){
            marker.info(exception.getMessage(),exception);
            fundtransferAdviceDcResponse.setReturn("");
        }

        return fundtransferAdviceDcResponse;
    }

    public FundtransferAdviceDdResponse handleFTAdvice_DD(FundtransferAdviceDd fundtransferAdviceDd, IMarker marker, String originatingInstitutionCode){
        final val fundtransferAdviceDdResponse = new FundtransferAdviceDdResponse();
        try{
            final val encryptedFTAdviceDirectDebitString = fundtransferAdviceDd.getRequest();
            final val clearFTAdviceDirectDebitString = nipConfig.isIgnoreEncryption() ? encryptedFTAdviceDirectDebitString : decryptString(encryptedFTAdviceDirectDebitString,originatingInstitutionCode,marker);

            marker.setRequest(" FT_Advice Dd Clear String ",clearFTAdviceDirectDebitString);

            final val ftAdviceDirectDebitRequestVO = xmlUtil.unmarshal(clearFTAdviceDirectDebitString, FTAdviceDirectDebitRequestVO.class);

            //some backend calls

            final val ftAdviceDirectDebitResponseVO = nipInwardService.handleFTAdviceDirectDebit(ftAdviceDirectDebitRequestVO);
            marker.setResponse("Response from FT_Advice_DC CBA " + ftAdviceDirectDebitResponseVO.toString());

            final val ftAdviseDirectDebitResponseVOXmlString = xmlUtil.marshal(FTAdviceDirectDebitResponseVO.class, ftAdviceDirectDebitResponseVO);

            final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? ftAdviseDirectDebitResponseVOXmlString : encryptString(ftAdviseDirectDebitResponseVOXmlString,originatingInstitutionCode,marker);

            fundtransferAdviceDdResponse.setReturn(encryptedXmlString);
        }catch (Exception exception){
            marker.info(exception.getMessage(),exception);
            fundtransferAdviceDdResponse.setReturn("");
        }

        return fundtransferAdviceDdResponse;
    }

    public FtackcreditrequestResponse handleFTAckCredit(Ftackcreditrequest ftackcreditrequest, IMarker marker, String originatingInstitutionCode){
        final val ftackcreditrequestResponse = new FtackcreditrequestResponse();
        try{
            final val encryptedFTAckCreditString = ftackcreditrequest.getRequest();
            final val clearFTAckCreditString = nipConfig.isIgnoreEncryption() ? encryptedFTAckCreditString : decryptString(encryptedFTAckCreditString,originatingInstitutionCode,marker);

            marker.setRequest(" FT Acknowledge Credit Clear String ",clearFTAckCreditString);

            final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? clearFTAckCreditString : encryptString(clearFTAckCreditString,originatingInstitutionCode,marker);
            marker.setResponse("Response from FT Acknowledge Credit  CBA " + encryptedXmlString);

            ftackcreditrequestResponse.setReturn(encryptedXmlString);
        }catch (Exception exception){
            marker.info(exception.getMessage(),exception);
            ftackcreditrequestResponse.setReturn("");
        }

        return ftackcreditrequestResponse;
    }

}
