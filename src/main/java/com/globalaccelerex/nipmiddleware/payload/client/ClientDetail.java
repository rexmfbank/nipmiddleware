package com.globalaccelerex.nipmiddleware.payload.client;

import com.globalaccelerex.nipmiddleware.payload.outward.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ClientDetail extends BaseResponse {

    private String clientId;

    private String clientName;

    private String contactEmail;

    private String contactPhone;

    private String businessDesc;

    private String callbackUrl;

    private boolean active ;

    private String bankCode;

    private String accountNo;

    private String accountName;

    private String bvn;

    private String kycLevel;

    private String latitude ;

    private String longitude ;
}
