package com.globalaccelerex.nipmiddleware.exception;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NIPMiddleWareAPIException extends RuntimeException {

    private IMarker marker;

    private ErrorResponse errorResponse;

    public NIPMiddleWareAPIException(String responseCode , String responseMessage,IMarker marker ){
        errorResponse = ErrorResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
        setMarker(marker);
    }

    public NIPMiddleWareAPIException(NIPResponseCodeEnum nipResponseCodeEnum,IMarker marker){
        errorResponse = ErrorResponse.builder()
                .responseCode(nipResponseCodeEnum.getCode())
                .responseMessage(nipResponseCodeEnum.getDescription())
                .build();
        setMarker(marker);
    }
}

