package com.globalaccelerex.nipmiddleware.payload.client.outward.tsq;

import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class TsqRequest extends BaseRequest {

    private String paymentReference;
}