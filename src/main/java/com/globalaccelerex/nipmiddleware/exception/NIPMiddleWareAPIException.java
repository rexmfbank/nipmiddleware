package com.globalaccelerex.nipmiddleware.exception;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import lombok.*;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_202;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NIPMiddleWareAPIException extends RuntimeException {

    private IMarker marker;

    private ErrorResponse errorResponse;

    public void buildFailureStatusException(String responseMessage,IMarker marker){
        errorResponse = ErrorResponse.builder()
                .responseCode(NIP_201.getCode())
                .responseMessage(responseMessage)
                .build();
        setMarker(marker);
    }

    public void buildPendingStatusException(String responseMessage,IMarker marker){
        errorResponse = ErrorResponse.builder()
                .responseCode(NIP_202.getCode())
                .responseMessage(responseMessage)
                .build();
        setMarker(marker);
    }

    public void buildExceptionFromEnum(NIPResponseCodeEnum nipResponseCodeEnum,IMarker marker){
        errorResponse = ErrorResponse.builder()
                .responseCode(nipResponseCodeEnum.getCode())
                .responseMessage(nipResponseCodeEnum.getDescription())
                .build();
        setMarker(marker);
    }
}

