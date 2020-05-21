package com.globalaccelerex.nipmiddleware.payload.client;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.payload.outward.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ResetPasswordResponse extends BaseResponse {

    public ResetPasswordResponse(NIPResponseCodeEnum nipResponseCodeEnum){
        super();
        setResponseCode(nipResponseCodeEnum.getCode());
        setResponseDescription(nipResponseCodeEnum.getDescription());
    }

    private String password ;

}
