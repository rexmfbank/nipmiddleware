package com.globalaccelerex.nipmiddleware.service.rest;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationServiceException;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BankAPIException extends AuthenticationServiceException {

    private ErrorResponse errorResponse;

    public BankAPIException(ErrorResponse errorResponse){
        super(errorResponse.getResponseMessage());
        setErrorResponse(errorResponse);
    }
}
