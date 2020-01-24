package com.globalaccelerex.nipmiddleware.payload.client.outward.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"sessionId"})
public class CreateClientResponse extends BaseResponse {

    @NotNull
    private String secretKey;
}
