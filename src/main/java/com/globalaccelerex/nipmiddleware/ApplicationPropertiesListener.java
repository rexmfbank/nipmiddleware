/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author HP
 */
@Slf4j
public class ApplicationPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private AWSSimpleSystemsManagement awsClient;

    public ApplicationPropertiesListener() {
        awsClient = AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        environment.getPropertySources().forEach((source) -> {
            if (source.getName().contains("applicationConfig")) {

                LinkedHashMap<String,Object> map = (LinkedHashMap) source.getSource();
                map.keySet().forEach( (key) -> {
                    String keyvalue = map.get(key).toString();
                    if (keyvalue.toString().startsWith("{ssm-parameter}")){
                        String value = getSSMParameter( ( keyvalue.replace("{ssm-parameter}", "")));
                        Properties props = new Properties();
                        props.put(key, value);
                        log.info("Updated property value for "+map.get(key).toString() );
                        environment.getPropertySources().addFirst(new PropertiesPropertySource(key, props));
                    }
                });
            }
        });

    }

    private String getSSMParameter(String key) {
        List<Parameter> parameters = awsClient.getParameters(new GetParametersRequest()
                .withNames(key)
                .withWithDecryption(true)).getParameters();
        for (Parameter param : parameters) {
            if (param.getName().equalsIgnoreCase(key)) {
                return param.getValue();
            }
        }
        return key;
    }
}
