package com.globalaccelerex.nipmiddleware.payload.client;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.payload.outward.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true , exclude = "secretKey")
@EqualsAndHashCode(callSuper = true)
public class CreateClientResponse extends BaseResponse {

    public CreateClientResponse(NIPResponseCodeEnum nipResponseCodeEnum){
        super();
        setResponseCode(nipResponseCodeEnum.getCode());
        setResponseDescription(nipResponseCodeEnum.getDescription());
    }

    @NotNull
    private String secretKey;

    @NotNull
    private String clientName;

    private String contactEmail;

    private String contactPhone;

    private String businessDesc;

    private String callbackUrl;

    private String latitude ;

    private String longitude ;
}
