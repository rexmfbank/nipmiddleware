package com.globalaccelerex.nipmiddleware.payload.client.updateclient;

import com.globalaccelerex.nipmiddleware.payload.client.BaseRequest;
import com.globalaccelerex.nipmiddleware.payload.client.accountdetail.AccountDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@ToString(callSuper = true)
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

    @Valid
    private AccountDetail accountDetail;

    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed for originatorBankCode")
    private String originatorBankCode;

    @DecimalMin(value = "-90",message = "Latitude can not be less than -90")
    @DecimalMax(value = "90" ,message = "Latitude can not be greater than 90")
    private Double latitude ;

    @DecimalMin(value = "-180" ,message = "Longitude can not be less than -180")
    @DecimalMax(value = "180" ,message = "Longitude can not be greater than 180")
    private Double longitude ;
}
