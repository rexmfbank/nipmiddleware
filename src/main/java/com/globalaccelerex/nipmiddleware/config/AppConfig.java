package com.globalaccelerex.nipmiddleware.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app")
@Component
@Data
@Validated
public class AppConfig {

    private String environment;

    @NotNull
    private String name;

    @NotNull
    private String baseUrl;

    @NotNull
    private String tsqUrl;

    @NotNull
    private String soapActionUrl;

}
