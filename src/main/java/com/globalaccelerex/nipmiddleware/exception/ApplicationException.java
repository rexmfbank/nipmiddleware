/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.exception;



import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.model.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationException extends RuntimeException {

    private IMarker marker;
    private Response code = Response.SYSTEM_ERROR;
    private ErrorResponse errorResponse;

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(IMarker marker, String message) {
        super(message);
        setMarker(marker);
        setCode(code);
    }

    public ApplicationException(IMarker marker, String message, Throwable cause) {
        super(message, cause);
        setMarker(marker);
        setCode(Response.SYSTEM_ERROR);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApplicationException(Response errorCode) {
        super(errorCode.getResponseMessage());
        setCode(errorCode);
    }
}
