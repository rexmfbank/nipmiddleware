package com.globalaccelerex.nipmiddleware.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

@Slf4j
public class FileUtil {

    public String resolvePath(String path){
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
}
