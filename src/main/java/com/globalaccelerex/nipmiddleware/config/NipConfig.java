/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
@Component
@Data
@Validated
@ConfigurationProperties(prefix = "nip")
public class NipConfig {

    @NotNull
    private String webServiceUrl;


    @NotNull
    private String tsqUrl;

    @NotNull
    private String soapActionUrl;

    @NotNull
    private String ssmPrivateKeyPath;

    @NotNull
    private String ssmPublicKeyPath;

    @NotNull
    private String ssmPasswordKey;

    private boolean ignoreEncryption;

    @NotNull
    private String bankUrl;



}

