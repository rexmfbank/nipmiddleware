package com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry;

import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class NESingleRequest extends BaseRequest {

    @NotEmpty(message = "destination bank code is required")
    private String destinationBankCode;

    @NotEmpty(message = "Account No is required")
    private String accountNo;

    @NotBlank(message = "Originator Bank Code is required")
    private String originatorBankCode;


}

