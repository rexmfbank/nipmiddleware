package com.globalaccelerex.nipmiddleware.security;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AuthenticationServiceException;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccessControlException extends AuthenticationServiceException {

    private ErrorResponse errorResponse;

}
