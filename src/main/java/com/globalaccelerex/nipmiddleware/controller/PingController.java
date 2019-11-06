package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.http.HTTPHelpers;
import com.globalaccelerex.nipmiddleware.http.HTTPRestTemplate;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.model.PingRequest;
import com.globalaccelerex.nipmiddleware.model.Response;
import com.globalaccelerex.nipmiddleware.service.NipConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/nipmiddleware/v1")
public class PingController {


    @Autowired
    private HTTPRestTemplate hTTPRestTemplate;
    @Autowired
    private NipConfig nipConfig;

    @GetMapping("ping")
    public ResponseEntity<?> ping(@Valid @ModelAttribute PingRequest request) {
        IMarker marker = Marker.fromString();
        Integer port = (request.getPort() == null) ?84 : request.getPort();
            String result = "";
            try {

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Object> entity = null ;
                    entity = new HttpEntity(headers);
                result=  hTTPRestTemplate.getClient().exchange(HTTPHelpers.buildURI(nipConfig.getBaseUrl()+port.toString(),"/"),HttpMethod.POST,entity,String.class).getBody();
            }catch (HttpClientErrorException http){
                result = http.getResponseBodyAsString();
                marker.setResponse(result);
            }
            catch (Exception ex) {
                marker.info(ex.getMessage(), ex);
                marker.setResponse(result);
            }
            finally {
            marker.done();
        }
        return new ResponseEntity( HttpStatus.OK);
    }
}
