package com.globalaccelerex.nipmiddleware.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_100;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_96;


@ControllerAdvice
public class GenericExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NIPMiddleWareAPIException.class)
    public ErrorResponse handleNIPMiddleWareException(NIPMiddleWareAPIException exception){
        final val marker = exception.getMarker();
        marker.info("NIPMiddleWareAPI Exception: =>" ,exception);
        marker.setMainResponse(exception.getErrorResponse().toString(), false);
        marker.done();
        return exception.getErrorResponse();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processBeanValidationError(BindException ex) {
        IMarker marker = Marker.fromString();

        marker.info("processBeanValidationError  => " + printStackTrace(ex));
        List<FieldError> error = ex.getBindingResult().getFieldErrors();

        if (!error.isEmpty()){
            for(FieldError anError : error){
                marker.info("Field Name ::: [ " + anError.getField() + " ]  message ::: [ " + anError.getDefaultMessage() + " ] ");
            }
        }
        marker.done();
        return new ErrorResponse(NIP_100);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processHttpMessageConversionException(HttpMessageConversionException ex) {

        IMarker marker = Marker.fromString();
        marker.info("Controller Advice is being called.HttpMessageConversionException => " + printStackTrace(ex));
        marker.done();

        if (ex.getCause() instanceof InvalidFormatException){
            InvalidFormatException invalidFormatException = (InvalidFormatException)ex.getCause();
            String fieldName = "";
            if (invalidFormatException.getPath() != null && invalidFormatException.getPath().get(0) != null){
                fieldName = invalidFormatException.getPath().get(0).getFieldName();
            }
            return new ErrorResponse(NIP_100, "Unexpected value "+invalidFormatException.getValue().toString() + " sent for field "+fieldName);
        }
        return new ErrorResponse(NIP_100);
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleApplicationException(ApplicationException exception) {
        IMarker marker = exception.getMarker();
        ErrorResponse response = (exception.getErrorResponse() == null) ? new ErrorResponse(NIP_96.getCode(), exception.getMessage()) : exception.getErrorResponse();
        if (marker != null) {
            marker.info("Application Exception:", exception);
            marker.setMainResponse(response.toString(), false);
        }
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processValidationError(MethodArgumentNotValidException ex) {

        IMarker marker = Marker.fromString();

        marker.info("Controller Advice is being called. => " + printStackTrace(ex));
        marker.done();

        List<FieldError> error = ex.getBindingResult().getFieldErrors();
        if (error.isEmpty()){
            return new ErrorResponse(NIP_100, "Request validation failed.");
        }else{
            return new ErrorResponse(NIP_100,  error.get(0).getDefaultMessage());
        }
    }

    private String printStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}
