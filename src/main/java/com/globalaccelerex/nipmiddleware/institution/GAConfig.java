package com.globalaccelerex.nipmiddleware.institution;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

@Data
@Slf4j
@Validated
@Component
@ConfigurationProperties(prefix = "ga")
public class GAConfig implements BankConfig {

    @NotBlank
    private String privateKeyPath;

    @NotBlank
    private String publicKeyPath;

    @NotBlank
    private String passwordKey;


    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public String getPasswordKey() {
        return passwordKey;
    }

    @PostConstruct
    public void init(){
        final val updateFilePathArray = updateFilePath(privateKeyPath, publicKeyPath);
        privateKeyPath = updateFilePathArray[0];
        publicKeyPath = updateFilePathArray[1];
    }
}
