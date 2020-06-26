package com.globalaccelerex.nipmiddleware.institution;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;


@Slf4j
@ToString
@Data
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

    @PostConstruct
    public void init(){
        final val updateFilePathArray = updateFilePath(privateKeyPath, publicKeyPath);
        privateKeyPath = updateFilePathArray[0];
        publicKeyPath = updateFilePathArray[1];
    }

}
