package com.globalaccelerex.nipmiddleware.exception;


import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.model.Response;

public class HttpException extends ApplicationException {

    private final Response errorCode = Response.HTTP_ERROR;
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public HttpException(IMarker marker, String message, Throwable cause) {
        super(message, cause);
        setMarker(marker);
        setCode(errorCode);
    }
    
    public HttpException(IMarker marker, String message) {
        super(message);
        setMarker(marker);
        setCode(errorCode);
    }

    public HttpException() {
        super(Response.HTTP_ERROR.toString());
        setCode(errorCode);
    }

    public HttpException(Exception e) {
        super(e);
        setCode(errorCode);
    }

    public HttpException(String message) {
        super(message);
        setCode(errorCode);
    }
    
    public HttpException (IMarker marker, ErrorResponse errorResponse){
        super(errorResponse.getResponseMessage());
        try{
            setCode(Response.valueOf(errorResponse.getResponseCode()));
        }catch (IllegalArgumentException ex){
            setCode(errorCode);
        }
    }
    
    public HttpException(IMarker marker,Response errorCode) {
        super(errorCode.getResponseMessage());
        setCode(errorCode);
        setMarker(marker);
    }

}
