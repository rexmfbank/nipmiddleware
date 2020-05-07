package com.globalaccelerex.nipmiddleware.payload.client;

import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UpdateClientRequest extends BaseRequest {

    @NotBlank(message = " Client Name required ")
    private String clientName;

    private String businessDesc;

    @URL(message = " Requires a valid callback url ")
    private String callbackUrl;

    @Email(message = " Requires a valid email ")
    private String contactEmail;

    private String contactPhone;

    private Boolean active;
}
