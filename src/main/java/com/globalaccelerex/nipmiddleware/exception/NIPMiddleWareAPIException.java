package com.globalaccelerex.nipmiddleware.exception;

import com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NIPMiddleWareAPIException extends RuntimeException {

    private ErrorResponse errorResponse;

    public NIPMiddleWareAPIException(String responseCode , String responseMessage ){
        errorResponse = ErrorResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }


}

