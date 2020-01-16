package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
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

    protected String encryptString(String clearString){
        return ssmUtil.encryptRequest(clearString);
    }

    protected String decryptString(String encryptedString){
        return ssmUtil.decryptResponse(encryptedString);
    }

    protected boolean ignoreEncryption(){
        return nipConfig.isIgnoreEncryption();
    }
}
