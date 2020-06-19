package com.globalaccelerex.nipmiddleware.institution;

import com.globalaccelerex.nipmiddleware.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class DefaultConfig {

    private FileUtil fileUtil;


    protected String[] updateFilePath(String privateKey , String publicKey){
        fileUtil = new FileUtil();
        privateKey = fileUtil.resolvePath(privateKey);
        publicKey = fileUtil.resolvePath(publicKey);
        final val output = new String[2];
        output[0] = privateKey;
        output[1] = publicKey;
        return output;
    }
}
