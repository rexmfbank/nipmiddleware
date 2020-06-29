package com.globalaccelerex.nipmiddleware.institution;

import com.globalaccelerex.nipmiddleware.util.FileUtil;
import lombok.val;


public interface BankConfig {

    String getPrivateKeyPath() ;

    String getPublicKeyPath() ;

    String getPasswordKey() ;

    default String[] updateFilePath(String privateKey , String publicKey){
        val fileUtil = new FileUtil();
        privateKey = fileUtil.resolvePath(privateKey);
        publicKey = fileUtil.resolvePath(publicKey);
        final val output = new String[2];
        output[0] = privateKey;
        output[1] = publicKey;
        return output;
    }
}
