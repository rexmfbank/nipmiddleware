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

    @NotBlank (message = "destination bank code is required")
    private String destinationBankCode;

    @NotBlank(message = "payment reference is required")
    private String paymentReference;

    @NotBlank(message = "destination account number is required")
    private String destinationAccountNo;

    @NotBlank(message = "originator account no is required")
    private String originatorAccountNo;

    private String nameEnquiryReference;

    private String beneficiaryAccountName;



    private String beneficiaryBVN; // optional

    private String beneficiaryKYCLevel;//optional

    private String originatorAccountName;//optional

    private String originatorBVN;

    private String originatorKYCLevel;

    private String narration;//max 100 , optional

    @DecimalMin(value = "0.00", inclusive = false ,message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String transactionLocation;

}

