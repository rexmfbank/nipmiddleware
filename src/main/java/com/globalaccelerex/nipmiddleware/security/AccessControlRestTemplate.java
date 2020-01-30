package com.globalaccelerex.nipmiddleware.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import lombok.val;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TimeZone;

public class AccessControlRestTemplate {

    private RestTemplate restTemplate;

    private final static int DEFAULT_CONNECTION_TIMEOUT = 120000;
    private final static int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 120000;
    private final static int DEFAULT_CONNECTION_SOCKET_TIMEOUT = 120000;

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    public RestTemplate getClient() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(getClientHttpRequestFactory());
            restTemplate.setErrorHandler(new ClientErrorHandler());
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        }
        return restTemplate;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_CONNECTION_SOCKET_TIMEOUT)
                .build();
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    class ClientErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return !response.getStatusCode().is2xxSuccessful();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            Scanner sc = new Scanner(response.getBody());
            sc.useDelimiter("\\A");

            String rawBody = sc.next();
            try{
                val errorResponse = OBJECT_MAPPER.readValue(rawBody, ErrorResponse.class);
                throw new AccessControlException(errorResponse);
            }catch(IOException ex){
                final val errorResponse = new ErrorResponse();
                errorResponse.setResponseMessage(rawBody);
                errorResponse.setResponseCode("111");
                throw new AccessControlException(errorResponse);
            }

        }

    }
}
