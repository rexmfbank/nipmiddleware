package com.globalaccelerex.nipmiddleware.payload.client.updateclient;

import com.globalaccelerex.nipmiddleware.enums.ClientStatusEnum;
import com.globalaccelerex.nipmiddleware.payload.client.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateClientStatusRequest extends BaseRequest {

    @NotNull(message = " status is required")
    private ClientStatusEnum status;
}
