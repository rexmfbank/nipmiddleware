package com.globalaccelerex.nipmiddleware.security;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import lombok.*;
import org.springframework.security.authentication.AuthenticationServiceException;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AccessControlException extends AuthenticationServiceException {

    private ErrorResponse errorResponse;

    public AccessControlException(ErrorResponse errorResponse){
        super(errorResponse.getResponseMessage());
        setErrorResponse(errorResponse);
    }



}
