package com.globalaccelerex.nipmiddleware.payload.client.outward.tsq;

import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class TsqResponse extends BaseResponse {

    private String destinationInstitutionCode;

    private String beneficiaryAccountName;

    private String beneficiaryAccountNo;

    private String beneficiaryBVN;

    private String beneficiaryKYCLevel;

    private String originatorAccountName;

    private String originatorAccountNo;

    private String originatorBVN;

    private String originatorKYCLevel;

    private String transactionLocation;

    private String narration;

    private String paymentReference;

    private String amount;

    private String nameEnquiryReference;

    private String originatorInstitutionCode;
}
