package com.globalaccelerex.nipmiddleware.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.http.HTTPHelpers;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TimeZone;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.CONNECTION_ERROR_MSG;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.ERROR_OCCURRED_MSG;

@Slf4j
public class BankHttpClient {

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    private final RestTemplate restTemplate;

    private BankHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public <T> T postRequest(String baseUrl ,String path, Object request, Class<T> responseType, HttpHeaders headers, Map<String,String> additionalHeaderAttribute) {
        headers = (headers == null) ? createHeaders(additionalHeaderAttribute)  :headers ;
        HttpEntity<Object> entity = new HttpEntity(request, headers);
        String response = null;
        try {
            final val requestString = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(request);

            val responseEntity = restTemplate.exchange(HTTPHelpers.buildURI(baseUrl, path), HttpMethod.POST, entity, String.class);
            response = responseEntity.getBody();
            if (String.class.equals(responseType)) {
                return (T) response;
            }
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (BankAPIException ex) {
            response = ex.getMessage();
            throw ex;
        } catch (ResourceAccessException ex) {
            val httpException = new BankAPIException(new ErrorResponse(CONNECTION_ERROR_MSG ,NIP_201.getCode()));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } catch (IOException | URISyntaxException | RestClientException ex) {
            final BankAPIException httpException = new BankAPIException( new ErrorResponse(ERROR_OCCURRED_MSG,NIP_201.getCode()));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } finally {
            log.info(String.format("====Response==== URL: %s Body: %s ", baseUrl+ path, StringUtils.defaultString(response)));
        }
    }

    public <T> T getRequest(String baseUrl , String path, Map<String, String> requestParameters, Class<T> responseType, HttpHeaders headers,Map<String,String> additionalHeaderAttribute) {
        headers = (headers == null) ? createHeaders(additionalHeaderAttribute)  :headers ;
        val entity = new HttpEntity("parameters", headers);
        String response = null;
        try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(HTTPHelpers.buildURI(baseUrl, path, requestParameters), HttpMethod.GET, entity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response = responseEntity.getBody();
            }
            if (String.class.equals(responseType)) {
                return (T) response;
            }
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (BankAPIException ex) {

            response = ex.getMessage();

            throw ex;
        } catch (ResourceAccessException ex) {
            val httpException = new BankAPIException( new ErrorResponse(ERROR_OCCURRED_MSG , NIP_201.getCode()));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } catch (IOException | URISyntaxException | RestClientException ex) {
            val httpException = new BankAPIException( new ErrorResponse(ERROR_OCCURRED_MSG , NIP_201.getCode()));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } finally {
            log.info(String.format("====Response==== URL: %s Body: %s ", baseUrl + path, StringUtils.defaultString(response)));
        }
    }
    //@TODO need to decide on authentication
    private HttpHeaders createHeaders(Map<String,String> additionalHeaderAttribute) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (additionalHeaderAttribute != null){
            additionalHeaderAttribute.forEach((k,v)-> headers.set(k, v) );
        }
        return headers;
    }

    public static class HTTPClientBuilder {

        private RestTemplate restTemplate;


        public BankHttpClient.HTTPClientBuilder restTemplate(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        public BankHttpClient createClient() {
            return new BankHttpClient(restTemplate);
        }
    }
}
