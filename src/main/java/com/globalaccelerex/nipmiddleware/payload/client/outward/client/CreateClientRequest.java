package com.globalaccelerex.nipmiddleware.payload.client.outward.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class CreateClientRequest extends BaseRequest {

    @NotBlank
    private String clientName;

    @NotBlank
    private String clientPassword; // clientId & clientPassword ll be used to confirm the client

    private String businessDesc;

    private String callbackUrl;

    @Email
    private String contactEmail;

    private String contactPhone;

}
