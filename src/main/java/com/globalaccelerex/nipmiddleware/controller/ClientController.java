package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.facade.ClientFacade;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import com.globalaccelerex.nipmiddleware.payload.client.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.*;

@Slf4j
@RestController
@RequestMapping(ADMIN_API)
public class ClientController {

    private final ClientFacade clientFacade;

    @Autowired
    public ClientController(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    @PostMapping(CREATE_CLIENT)
    public ResponseEntity<CreateClientResponse> createClient(@Valid @RequestBody CreateClientRequest createClientRequest){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< createClient  >>>>>>>>");
        marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                build().toUri().toASCIIString(), createClientRequest.toString(), true);
        createClientRequest.setMarker(marker);
        try{
            final val createClientResponse = clientFacade.createClient(createClientRequest);
            marker.setMainResponse(createClientResponse.toString(), true);
            return new ResponseEntity(createClientResponse, HttpStatus.OK);
        }finally {
            marker.done();
        }
    }

    @GetMapping(GET_CLIENT)
    public ResponseEntity<ClientDetail> getClient(@PathVariable(PATH_VARIABLE_CLIENT_ID) String clientId) {
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< GetClient  >>>>>>>>");
        marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                build().toUri().toASCIIString(), clientId, false);
        try{
            final val clientDetail = clientFacade.getClientDetail(clientId, marker);
            marker.setMainResponse(clientDetail.toString(), false);
            return new ResponseEntity(clientDetail, HttpStatus.OK);
        }finally {
            marker.done();
        }
    }

    @GetMapping(GET_CLIENTS)
    public ResponseEntity<GetClientsResponse> getClients(@Valid @ModelAttribute GetClientsRequest getClientsRequest) {
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< Get Clients  >>>>>>>>");
        getClientsRequest.setMarker(marker);
        marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                build().toUri().toASCIIString(), getClientsRequest.toString(), false);
        try {
            final val getClientsResponse = clientFacade.getClients(getClientsRequest);
            marker.setMainResponse(getClientsResponse.toString(), false);
            return new ResponseEntity(getClientsResponse, HttpStatus.OK);
        } finally {
            marker.done();
        }
    }

    @PostMapping(UPDATE_CLIENT)
    public ResponseEntity<?> updateClient(@Valid @RequestBody UpdateClientRequest updateClientRequest){
        IMarker marker = Marker.fromString();
        marker.info("<<<<<<<< Update Client  >>>>>>>>");
        updateClientRequest.setMarker(marker);
        marker.setMainRequest(ServletUriComponentsBuilder.fromCurrentRequestUri().
                build().toUri().toASCIIString(), updateClientRequest.toString(), false);
        try {
            clientFacade.updateClient(updateClientRequest);
            marker.setMainResponse("", false);
            return new ResponseEntity(HttpStatus.OK);
        } finally {
            marker.done();
        }
    }


}
