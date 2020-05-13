package com.globalaccelerex.nipmiddleware.payload.client;

import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ResetPasswordRequest extends BaseRequest {
}
