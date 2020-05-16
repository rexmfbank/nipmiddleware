package com.globalaccelerex.nipmiddleware.exception;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

    public NIPMiddleWareAPIException(NIPResponseCodeEnum nipResponseCodeEnum,String description ,IMarker marker){
        errorResponse = ErrorResponse.builder()
                .responseCode(nipResponseCodeEnum.getCode())
                .responseMessage(nipResponseCodeEnum.getDescription() + " : " +description)
                .build();
        setMarker(marker);
    }
}

