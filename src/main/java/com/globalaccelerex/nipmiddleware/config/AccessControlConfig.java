package com.globalaccelerex.nipmiddleware.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Data
@Validated
@ConfigurationProperties(prefix = "ac")
public class AccessControlConfig {

    private String baseUrl;

    private String username;

    private String password;

}
