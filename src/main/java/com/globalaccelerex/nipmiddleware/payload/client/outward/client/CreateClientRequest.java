package com.globalaccelerex.nipmiddleware.payload.client.outward.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ToString
@JsonIgnoreProperties({"clientPassword"})
@EqualsAndHashCode(callSuper = true)
public class CreateClientRequest extends BaseRequest {

    @NotBlank(message = " Client Name required ")
    private String clientName;

    @NotBlank(message = " client password required ")
    private String clientPassword;

    private String businessDesc;

    @URL(message = " Requires a valid callback url ")
    private String callbackUrl;

    @Email (message = " Requires a valid email ")
    private String contactEmail;

    private String contactPhone;

}
