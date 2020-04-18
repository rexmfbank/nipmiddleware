package com.globalaccelerex.nipmiddleware.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessControlResponse {
    private String accessToken;
    private String accessSecret;
    private String allowedServices;
    private long validTill ;
}
