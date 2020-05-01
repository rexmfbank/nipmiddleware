package com.globalaccelerex.nipmiddleware.config;

import com.globalaccelerex.nipmiddleware.service.ws.NIPOutwardWS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Slf4j
@Configuration
public class WsdlConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.globalaccelerex.nipmiddleware.payload.nip.ws",
                "com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry");
        return marshaller;
    }


    @Bean
    public NIPOutwardWS nipOutwardWS(Jaxb2Marshaller marshaller){
        final NIPOutwardWS nipOutwardWS = new NIPOutwardWS();
        nipOutwardWS.setDefaultUri(appConfig.getBaseUrl());
        nipOutwardWS.setMarshaller(marshaller);
        nipOutwardWS.setUnmarshaller(marshaller);
        return nipOutwardWS;
    }
}
