package com.globalaccelerex.nipmiddleware.payload.client.nameenquiry;

import com.globalaccelerex.nipmiddleware.payload.client.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NESingleResponse extends BaseResponse {

    private String accountNo;

    private String accountName;

    private String bankVerificationNo;

    private String kycLevel;

    private String nameEnquiryReference;

    private String destinationBankCode;

}
