package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.facade.outward.TsqFacade;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.INWARD_API;
import static com.globalaccelerex.nipmiddleware.api.ClientAPI.INWARD_TSQ;

@Slf4j
@RestController
@RequestMapping(INWARD_API)
public class InwardController extends APIController{

    private TsqFacade tsqFacade;

    @GetMapping(path = INWARD_TSQ)
    public ResponseEntity<?> doTsq(@PathVariable("clientId") String clientId,@PathVariable("sessionId") String sessionId){
        IMarker marker = Marker.fromString(sessionId);
        marker.info("<<<<<<<< do Inward Tsq >>>>>>>>");
        try {
            ResponseEntity entity = validateClient(marker, clientId);
            if (entity != null) {
                return entity;
            }

            marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                    build().toUri().toASCIIString(), sessionId, false);
            final val tsqResponse = tsqFacade.doInwardTsq(clientId, marker, sessionId);
            marker.setMainResponse(tsqResponse.toString(), false);
            return new ResponseEntity(tsqResponse, HttpStatus.OK);
        }finally {
            marker.done();
        }
    }

    @Autowired
    public void setTsqFacade(TsqFacade tsqFacade) {
        this.tsqFacade = tsqFacade;
    }
}
