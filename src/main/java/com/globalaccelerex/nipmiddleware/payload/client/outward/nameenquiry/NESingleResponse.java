package com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry;

import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class NESingleResponse extends BaseResponse {

    private String accountNo;

    private String accountName;

    private String bankVerificationNo;

    private String kycLevel;

    private String nameEnquiryReference;

}
