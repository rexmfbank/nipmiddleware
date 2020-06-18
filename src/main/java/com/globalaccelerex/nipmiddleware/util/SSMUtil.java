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

    private SSMLib ssmLib;

    private final NipConfig nipConfig;

    private String privateKeyPath;

    private String publicKeyPath;

    @Autowired
    public SSMUtil(NipConfig nipConfig) {
        this.nipConfig = nipConfig;
    }

    @PostConstruct
    public void init(){
        privateKeyPath = resolvePath(nipConfig.getSsmPrivateKeyPath());

        publicKeyPath = resolvePath(nipConfig.getSsmPublicKeyPath());

    }

    private String resolvePath(String path){
        try {
            if (new File(path).exists()) {
                return path;
            }

            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new FileNotFoundException("Could not load the SSM keys");
            }
            File oFile = File.createTempFile(StringUtils.replacePattern(path, "[^a-zA-Z0-9]+", "_"), ".tmp");
            oFile.deleteOnExit();

            IOUtils.copy(is, new FileOutputStream(oFile));
            //log.info("Output Path for file: " + oFile.getAbsolutePath());
            return oFile.getAbsolutePath();
        } catch (Exception ex) {
            log.debug("error resolving path ", ex);
        }
        return path;
    }

    public String encryptRequest(String dataToEncrypt){
        SSMLib ssmLib = new SSMLib(publicKeyPath,privateKeyPath);
        return ssmLib.encryptMessage(dataToEncrypt);
    }

    public String decryptResponse(String dataToDecrypt){
        SSMLib ssmLib = new SSMLib(publicKeyPath,privateKeyPath);
        return ssmLib.decryptFile(dataToDecrypt, nipConfig.getSsmPasswordKey());
    }
}
