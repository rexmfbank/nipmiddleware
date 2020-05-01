package com.globalaccelerex.nipmiddleware.payload.client.outward.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
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
}
