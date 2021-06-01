package com.globalaccelerex.nipmiddleware.payload.client.createClient;

import com.globalaccelerex.nipmiddleware.payload.client.BaseRequest;
import com.globalaccelerex.nipmiddleware.payload.client.accountdetail.AccountDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@ToString(callSuper = true , exclude = "clientPassword")
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

    @Valid
    @NotNull(message = " Account details cannot be null ")
    private AccountDetail accountDetail;

    @NotBlank(message = "Originator Bank Code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed for originatorBankCode")
    private String originatorBankCode;

    @DecimalMin(value = "-90", inclusive = true ,message = "Latitude can not be less than -90")
    @DecimalMax(value = "90", inclusive = true ,message = "Latitude can not be greater than 90")
    private Double latitude ;

    @DecimalMin(value = "-180", inclusive = true ,message = "Longitude can not be less than -180")
    @DecimalMax(value = "180", inclusive = true ,message = "Longitude can not be greater than 180")
    private Double longitude ;

}
