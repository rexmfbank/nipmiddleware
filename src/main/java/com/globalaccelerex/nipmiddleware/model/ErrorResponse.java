/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private String responseMessage;
    private String responseCode;

    public ErrorResponse() {

    }

    public ErrorResponse(final Response response) {
        setResponseMessage(response.getResponseMessage());
        setResponseCode(response.getResponseCode());
    }

    public ErrorResponse(final Response response, final String errorMessage) {
        setResponseMessage(errorMessage);
        setResponseCode(response.getResponseCode());
    }

    public ErrorResponse(final String responseCode, final String responseMessage) {
        setResponseMessage(responseMessage);
        setResponseCode(responseCode);
    }

    public ErrorResponse(String errorMessage) {
        setResponseMessage(errorMessage);
        setResponseCode(Response.SYSTEM_ERROR.getResponseCode());
    }

    public ErrorResponse(Exception e) {
        setResponseMessage(e.getMessage());
        setResponseCode(Response.SYSTEM_ERROR.getResponseCode());
    }
}
