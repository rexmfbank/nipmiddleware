package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.security.client.ClientAuthenticationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.*;

@Slf4j
@Controller
public class APIController {

    protected ClientAuthenticationData getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null){
            return null;
        }
        if (!(auth.getPrincipal() instanceof ClientAuthenticationData)) {
            return null;
        }
        return (ClientAuthenticationData) auth.getPrincipal();
    }

    protected ResponseEntity validateClient(IMarker marker, String clientId) {
        ClientAuthenticationData token = getPrincipal();
        if (token == null) {
            marker.setMainResponse("Invalid authentication", false);

            return new ResponseEntity(ErrorResponse.builder()
                    .responseCode(NIP_201.getCode())
                    .responseMessage(INVALID_AUTHENTICATION_MSG)
                    .build(), HttpStatus.UNAUTHORIZED);
        }
        if (token.getClient() == null) {
            marker.setMainResponse("Client not found", false);
            return new ResponseEntity(ErrorResponse.builder()
                    .responseCode(NIP_201.getCode())
                    .responseMessage(CLIENT_NOT_FOUND_MSG)
                    .build(), HttpStatus.UNAUTHORIZED);

        }

        if (!clientId.equalsIgnoreCase(token.getClient().getClientId())) {
            marker.setMainResponse("Unauthorised access", false);
            return new ResponseEntity(ErrorResponse.builder()
                    .responseCode(NIP_201.getCode())
                    .responseMessage(CLIENT_ID_NOT_MATCHING_MSG)
                    .build(), HttpStatus.UNAUTHORIZED);

        }
        return null;
    }
}
