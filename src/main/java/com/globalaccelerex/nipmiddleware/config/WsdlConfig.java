package com.globalaccelerex.nipmiddleware.config;

import com.globalaccelerex.nipmiddleware.exception.HttpException;
import com.globalaccelerex.nipmiddleware.service.ws.NIPOutwardWS;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class WsdlConfig {

    private final static int DEFAULT_CONNECTION_TIMEOUT = 60000;
    private final static int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60000;
    private final static int DEFAULT_CONNECTION_SOCKET_TIMEOUT = 60000;

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

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_CONNECTION_SOCKET_TIMEOUT)
                .build();

        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();

        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new HttpException(e);
        }
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(requestConfig)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(sslContext)
                .addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor())
                .build();

        HttpComponentsMessageSender httpsender = new HttpComponentsMessageSender();
        httpsender.setHttpClient(httpClient);
        try {
            httpsender.createConnection(new URI(appConfig.getBaseUrl() ));
        } catch (URISyntaxException | IOException ex) {
            log.info("uri error", ex);
        }

        nipOutwardWS.setMessageSender(httpsender);

        WebServiceMessageSender[] senders = nipOutwardWS.getMessageSenders();
        for (WebServiceMessageSender sender : senders) {
            if (sender instanceof HttpComponentsMessageSender) {
                HttpComponentsMessageSender hSender = (HttpComponentsMessageSender) sender;
                hSender.setHttpClient(httpClient);
                log.info("setting http client for sender");
            }
        }
        return nipOutwardWS;
    }
}
