package com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer;

import com.globalaccelerex.nipmiddleware.annotation.AmountConstraint;
import com.globalaccelerex.nipmiddleware.payload.client.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class FTSingleCreditRequest extends BaseRequest {

    @NotBlank
    private String destinationInstitutionCode;

    @NotBlank
    private String paymentReference;

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

    private String originatorBVN;

    private String originatorKYCLevel;

    private String narration;//max 100 , optional

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal amount;

    private String transactionLocation;

}

