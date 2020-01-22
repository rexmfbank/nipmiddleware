package com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer;

import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class FTSingleCreditRequest extends BaseRequest {

    @NotBlank
    private String destinationInstitutionCode;

    private String nameEnquiryReference;

    @NotBlank
    private String beneficiaryAccountName;

    @NotBlank
    private String beneficiaryAccountNo;

    private String beneficiaryBVN; // optional

    private String beneficiaryKYCLevel;//optional

    @NotBlank
    private String originatorAccountName;

    @NotBlank
    private String originatorAccountNo;

    @NotBlank
    private String originatorBVN;

    private String originatorKYCLevel;

    private String narration;//max 100 , optional

    //@AmountConstraint
    private String amount;//decimal place

}

