package com.globalaccelerex.nipmiddleware.payload.outward.tsq;

import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class TsqRequest extends BaseRequest {

    private String sessionId;

    @NotEmpty(message = "PaymentReference is required")
    private String paymentReference;

}