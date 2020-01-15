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

    public FundtransfersingleitemDdResponse handleFT_DD(FundtransfersingleitemDd fundtransfersingleitemDd, IMarker marker){
        final val encryptedFTDirectDebitString = fundtransfersingleitemDd.getRequest();
        final val clearFTDirectDebitString = nipConfig.isIgnoreEncryption() ? encryptedFTDirectDebitString: decryptString(encryptedFTDirectDebitString);

        marker.setRequest(" FI List Clear String ",clearFTDirectDebitString);

        final val ftDirectDebitRequestVO = xmlUtil.unmarshal(clearFTDirectDebitString, FTDirectDebitRequestVO.class);

        // some backend calls

        final val ftDirectDebitResponseVO = nipInwardService.handleFT_DirectDebit(ftDirectDebitRequestVO);

        marker.setResponse("Response from FT_Dd CBA " + ftDirectDebitResponseVO.toString());

        final val ftDirectDebitResponseVOXmlString = xmlUtil.marshal(FTDirectDebitResponseVO.class, ftDirectDebitResponseVO);

        final val encryptedXmlString = nipConfig.isIgnoreEncryption() ? ftDirectDebitResponseVOXmlString : encryptString(ftDirectDebitResponseVOXmlString);

        final val fundtransfersingleitemDdResponse = new FundtransfersingleitemDdResponse();
        fundtransfersingleitemDdResponse.setReturn(encryptedXmlString);
        return fundtransfersingleitemDdResponse;
    }




}
