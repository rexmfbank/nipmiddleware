package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.facade.ClientFacade;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.payload.client.updateclient.UpdateClientPasswordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.CLIENT_API;
import static com.globalaccelerex.nipmiddleware.api.ClientAPI.UPDATE_PASSWORD;

@Slf4j
@RestController
@RequestMapping(CLIENT_API)
public class ClientController extends APIController{

    private final ClientFacade clientFacade;

    @Autowired
    public ClientController(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    @PostMapping(UPDATE_PASSWORD)
    public ResponseEntity<?> updateClientPassword(@Valid @RequestBody UpdateClientPasswordRequest updateClientPasswordRequest){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< Update Client Password  >>>>>>>>");
        updateClientPasswordRequest.setMarker(marker);
        marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                build().toUri().toASCIIString(), updateClientPasswordRequest.toString(), false);
        try {

            ResponseEntity entity = validateClient(marker,updateClientPasswordRequest.getClientId());
            if (entity != null) {
                return entity;
            }

            clientFacade.updateClientPassword(updateClientPasswordRequest);
            marker.setMainResponse("", false);
            return new ResponseEntity(HttpStatus.OK);
        } finally {
            marker.done();
        }
    }


}
