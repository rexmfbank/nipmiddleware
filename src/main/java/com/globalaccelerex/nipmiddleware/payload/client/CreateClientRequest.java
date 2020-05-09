package com.globalaccelerex.nipmiddleware.payload.client;

import com.globalaccelerex.nipmiddleware.annotation.Nuban;
import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ToString
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

    @NotBlank(message = "NIP Bank Code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String bankCode;

    @Nuban
    @NotBlank(message = "Account No is required")
    private String accountNo;

    @NotBlank(message = "Originator Bank Code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String originatorBankCode;

}
