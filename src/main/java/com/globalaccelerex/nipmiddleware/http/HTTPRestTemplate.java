/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HTTPRestTemplate {

    private static final  int CONNECTION_TIMEOUT = 30000;
    private static final  int REQUEST_TIMEOUT = 30000;
    private static final int SOCKET_TIMEOUT = 30000 ; 
    
    private RestTemplate restTemplate;

    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    public RestTemplate getClient() {
        if (restTemplate == null) {
            this.restTemplate = new RestTemplate(getClientHttpRequestFactory());
            restTemplate.getInterceptors().add(new RequestInterceptor());
            restTemplate.setErrorHandler(new ClientErrorHandler());
        }
        return this.restTemplate;
    }
}
