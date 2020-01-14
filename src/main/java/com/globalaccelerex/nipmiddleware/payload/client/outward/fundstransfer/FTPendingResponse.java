package com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_09;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

@Getter
@Setter
@JsonIgnoreProperties({"sessionId"})
public class FTPendingResponse extends BaseResponse {

    private String paymentReference;

    public FTPendingResponse(boolean flag){
        setResponseCode(flag? NIP_108.getCode() :NIP_09.getCode() );
    }

}

