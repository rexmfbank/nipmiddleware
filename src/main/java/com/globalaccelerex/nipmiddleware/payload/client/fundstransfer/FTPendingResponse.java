package com.globalaccelerex.nipmiddleware.payload.client.fundstransfer;

import com.globalaccelerex.nipmiddleware.payload.client.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_09;

@Getter
@Setter
@ToString(callSuper = true)
public class FTPendingResponse extends BaseResponse {

    private String sessionId;

    private String paymentReference;

    public FTPendingResponse(){
        setResponseCode(NIP_09.getCode() );
    }

}

