package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import lombok.extern.slf4j.Slf4j;
import nfp.ssm.core.SSMLib;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

@Slf4j
@Component
public class SSMUtil {

    public String encryptRequest(String dataToEncrypt,String privateKeyPath,String publicKeyPath){
        SSMLib ssmLib = new SSMLib(publicKeyPath,privateKeyPath);
        return ssmLib.encryptMessage(dataToEncrypt);
    }

    public String decryptResponse(String dataToDecrypt,String privateKeyPath,String publicKeyPath, String passwordKey){
        SSMLib ssmLib = new SSMLib(publicKeyPath,privateKeyPath);
        return ssmLib.decryptFile(dataToDecrypt, passwordKey);
    }
}
