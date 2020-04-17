package com.globalaccelerex.nipmiddleware.security.outward;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

@Data
@Builder
@Slf4j
@ToString
public class OutwardAuthenticationData {

    private String nonce;

    private String timestamp;

    private String signature;

    private String accessToken;

    private String userToken;

    private String httpMethod;

    private String encodedURL;

    private String clientId;

    private static final String DELIMITER = "&&";

    public boolean isValidSignature() {
        val cipherStr = new StringBuilder().append(accessToken)
                .append(DELIMITER)
                .append(userToken)
                .append(DELIMITER)
                .append(clientId)
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
