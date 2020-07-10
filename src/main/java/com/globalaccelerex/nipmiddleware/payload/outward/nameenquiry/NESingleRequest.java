package com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry;

import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NESingleRequest extends BaseRequest {

    @NotEmpty(message = "destination bank code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String destinationBankCode;

    //@Nuban(ignoreIfEmpty = false)
    @NotEmpty(message = "Account No is required")
    private String accountNo;

    @NotBlank(message = "Originator Bank Code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String originatorBankCode;


}

