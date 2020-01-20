package com.globalaccelerex.nipmiddleware.security;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

@Data
@Builder
@Slf4j
public class AuthenticationData {

    private String nonce;

    private String timestamp;

    private String signature;

    private String accessToken;

    private String userAccessToken;

    private String accessSecret;

    private String httpMethod;

    private String encodedURL;

    private static final String DELIMITER = "&&";

    public boolean isValidSignature() {

        final val cipherStr = new StringBuilder().append(accessToken)
                .append(DELIMITER)
                .append(accessSecret)
                .append(DELIMITER)
                .append(timestamp)
                .append(DELIMITER)
                .append(nonce)
                .append(DELIMITER)
                .append(httpMethod)
                .append(DELIMITER)
                .append(encodedURL)
                .toString();
        log.trace("cipher is "+ cipherStr + " => "+ DigestUtils.sha512Hex(cipherStr));
        return DigestUtils.sha512Hex(cipherStr).equalsIgnoreCase(signature);

    }
}
