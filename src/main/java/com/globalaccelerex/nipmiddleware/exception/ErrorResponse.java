package com.globalaccelerex.nipmiddleware.exception;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String responseMessage;

    private String responseCode;

    public ErrorResponse(NIPResponseCodeEnum nipResponseCodeEnum){
        responseCode = nipResponseCodeEnum.getCode();
        responseMessage = nipResponseCodeEnum.getDescription();
    }

}

