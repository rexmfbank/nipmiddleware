package com.globalaccelerex.nipmiddleware.exception;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GenericExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NIPMiddleWareAPIException.class)
    public ErrorResponse handleNIPMiddleWareException(NIPMiddleWareAPIException exception){
        final val marker = exception.getMarker();
        marker.info("NIPMiddleWareAPI Exception: =>" + printStackTrace(exception));
        marker.done();
        return exception.getErrorResponse();
    }

    private String printStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}
