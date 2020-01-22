package com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer;

import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_09;

@Getter
@Setter
public class FTPendingResponse extends BaseResponse {

    public FTPendingResponse(){
        setResponseCode(NIP_09.getCode());
    }
}

