package com.globalaccelerex.nipmiddleware.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalaccelerex.nipmiddleware.config.AccessControlConfig;
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
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;
import java.util.TimeZone;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_102;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_114;

@Slf4j
public class AccessControlHttpClient {

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    private final AccessControlConfig config;

    private final RestTemplate restTemplate;

    private AccessControlHttpClient(AccessControlConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public <T> T postRequest(String path, Object request, Class<T> responseType, HttpHeaders headers, Map<String,String> additionalHeaderAttribute) {
        headers = (headers == null) ? createHeaders(additionalHeaderAttribute)  :headers ;
        HttpEntity<Object> entity = new HttpEntity(request, headers);
        String response = null;
        try {
            val responseEntity = restTemplate.exchange(HTTPHelpers.buildURI(config.getBaseUrl(), path), HttpMethod.POST, entity, String.class);
            response = responseEntity.getBody();
            if (String.class.equals(responseType)) {
                return (T) response;
            }
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (AccessControlException ex) {
            response = ex.getMessage();
            throw ex;
        } catch (ResourceAccessException ex) {
            val httpException = new AccessControlException(new ErrorResponse(NIP_114));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } catch (IOException | URISyntaxException | RestClientException ex) {
            final AccessControlException httpException = new AccessControlException( new ErrorResponse(NIP_102));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } finally {
            log.trace(String.format("====Response==== URL: %s Body: %s ", config.getBaseUrl() + path, StringUtils.defaultString(response)));
        }
    }

    public <T> T getRequest( String path, Map<String, String> requestParameters, Class<T> responseType, HttpHeaders headers,Map<String,String> additionalHeaderAttribute) {
        headers = (headers == null) ? createHeaders(additionalHeaderAttribute)  :headers ;
        val entity = new HttpEntity("parameters", headers);
        String response = null;
        try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(HTTPHelpers.buildURI(config.getBaseUrl(), path, requestParameters), HttpMethod.GET, entity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                response = responseEntity.getBody();
            }
            if (String.class.equals(responseType)) {
                return (T) response;
            }
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (AccessControlException ex) {

            response = ex.getMessage();

            throw ex;
        } catch (ResourceAccessException ex) {
            val httpException = new AccessControlException( new ErrorResponse(NIP_114));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } catch (IOException | URISyntaxException | RestClientException ex) {
            val httpException = new AccessControlException( new ErrorResponse(NIP_102));
            response = httpException.getMessage() + "=> Server Response " + ex.getMessage();
            throw httpException;
        } finally {
            log.trace(String.format("====Response==== URL: %s Body: %s ", config.getBaseUrl() + path, StringUtils.defaultString(response)));
        }
    }

    private HttpHeaders createHeaders(Map<String,String> additionalHeaderAttribute) {
        HttpHeaders headers = new HttpHeaders();
        String auth = config.getUsername() + ":" + config.getPassword();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try{
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8")));
        }catch (UnsupportedEncodingException ex) {
            log.debug("Error encoding auth details", ex);
        }
        if (additionalHeaderAttribute != null){
            additionalHeaderAttribute.forEach((k,v)-> headers.set(k, v) );
        }

        return headers;
    }

    public static class HTTPClientBuilder {

        private final AccessControlConfig config;
        private RestTemplate restTemplate;

        public HTTPClientBuilder(AccessControlConfig config) {
            this.config = config;
        }

        public HTTPClientBuilder restTemplate(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        public AccessControlHttpClient createClient() {
            return new AccessControlHttpClient(config, restTemplate);
        }
    }
}
