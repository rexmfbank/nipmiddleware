package com.globalaccelerex.nipmiddleware.exception;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.model.Response;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@ToString
@NoArgsConstructor
public class ErrorResponse {

    private String responseMessage;

    private String responseCode;

    public ErrorResponse(String responseMessage, String responseCode) {
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
    }

    public ErrorResponse(NIPResponseCodeEnum nipResponseCodeEnum){
        responseCode = nipResponseCodeEnum.getCode();
        responseMessage = nipResponseCodeEnum.getDescription();
    }

    public ErrorResponse(NIPResponseCodeEnum nipResponseCodeEnum, String responseMessage){
        responseCode = nipResponseCodeEnum.getCode();
        this.responseMessage = StringUtils.defaultIfBlank(responseMessage , nipResponseCodeEnum.getDescription());
    }

}

