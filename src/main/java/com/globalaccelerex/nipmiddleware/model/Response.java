package com.globalaccelerex.nipmiddleware.model;

public enum Response {
    SUCCESS("SUCCESS","Transaction succesfull "),
    MISSING_OWNER("MISSING_OWNER","Transaction owner not found "),
    MISSING_COMMIT("MISSING_COMMIT","Transaction is missing a committed value  "),
    ROLLBACK_COMMIT("ROLLBACK_COMMIT","Transaction contains some commit cannot be rollback"),
    INVALID_TRANSACTION_STATE("SUCCESS","Transaction in unexpected state "),
    SYSTEM_ERROR("SYSTEM_ERROR","Unexpected error occurred "),
    REQUEST_METHOD_ERROR("REQUEST_METHOD_ERROR","REQUEST_METHOD_ERROR"),
    REQUEST_ERROR("REQUEST_ERROR","REQUEST_ERROR"),
    BAD_MESSAGE_ERROR("BAD_MESSAGE_ERROR","BAD_MESSAGE_ERROR"),
    HTTP_ERROR("HTTP_ERROR","Error occured while connecting to upstream system"),
    DATA_ACCESS_ERROR("DATA_ACCESS_ERROR","DATA_ACCESS_ERROR"),
    VALIDATION_ERROR("VALIDATION_ERROR","VALIDATION_ERROR"),
    TRANSACTION_NOT_FOUND("TRANSACTION_NOT_FOUND","Transaction not found ");


    private Response(String responseCode , String responseMessage ){
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
    }
    private String responseCode;
    private String responseMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
