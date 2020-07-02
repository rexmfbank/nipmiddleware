package com.globalaccelerex.nipmiddleware.facade.inward;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.util.SSMUtil;
import com.globalaccelerex.nipmiddleware.util.XmlUtil;

public abstract class AbstractInwardFacade {

    private final SSMUtil ssmUtil;

    protected final XmlUtil xmlUtil;

    protected final NipConfig nipConfig;

    public AbstractInwardFacade(SSMUtil ssmUtil, XmlUtil xmlUtil, NipConfig nipConfig){
        this.ssmUtil = ssmUtil;
        this.xmlUtil = xmlUtil;
        this.nipConfig = nipConfig;
    }


    protected String encryptString(String clearString, String originatingInstitutionCode, IMarker marker){
        return ssmUtil.encryptRequest(clearString,originatingInstitutionCode,marker);
    }

    protected String decryptString(String encryptedString, String originatingInstitutionCode, IMarker marker){
        return ssmUtil.decryptResponse(encryptedString,originatingInstitutionCode,marker);
    }


    protected boolean ignoreEncryption(){
        return nipConfig.isIgnoreEncryption();
    }
}
