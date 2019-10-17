package com.globalaccelerex.nipmiddleware.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PingRequest extends APIRequest {

private Integer port;
}
