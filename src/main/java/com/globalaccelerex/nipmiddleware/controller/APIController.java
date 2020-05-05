package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.security.client.ClientAuthenticationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

@Slf4j
public class APIController {

    protected ClientAuthenticationData getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null){
            return null;
        }
        if (!ClientAuthenticationData.class.isInstance(auth.getPrincipal())) {
            return null;
        }
        return (ClientAuthenticationData) auth.getPrincipal();
    }

    protected ResponseEntity validateClient(IMarker marker, String clientId) {
        ClientAuthenticationData token = getPrincipal();
        if (token == null) {
            marker.setMainResponse("Invalid authentication", false);
            return new ResponseEntity(new ErrorResponse(NIP_126), HttpStatus.UNAUTHORIZED);
        }
        if (token.getClient() == null) {
            marker.setMainResponse("Client not found", false);
            return new ResponseEntity(new ErrorResponse(NIP_124), HttpStatus.UNAUTHORIZED);
        }

        if (!clientId.equalsIgnoreCase(token.getClient().getClientId())) {
            marker.setMainResponse("Unauthorised access", false);
            return new ResponseEntity(new ErrorResponse(NIP_127), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
