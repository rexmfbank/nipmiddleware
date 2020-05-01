package com.globalaccelerex.nipmiddleware.payload.client.outward.client;

import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class ClientDetail extends BaseResponse {

    private String clientId;

    private String clientName;

    private String contactEmail;

    private String contactPhone;

    private String businessDesc;

    private String callbackUrl;

    private boolean active ;
}
