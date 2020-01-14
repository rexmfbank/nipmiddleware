package com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry;

import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class NESingleRequest extends BaseRequest {

    @NotEmpty
    private String destinationInstitutionCode;

    @NotEmpty
    private String accountNo;


}

