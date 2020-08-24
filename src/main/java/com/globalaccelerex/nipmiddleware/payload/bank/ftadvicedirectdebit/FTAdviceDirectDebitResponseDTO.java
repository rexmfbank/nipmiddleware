package com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectdebit;

import lombok.Data;

@Data
public class FTAdviceDirectDebitResponseDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String nameEnquiryRef; //optional

    private String beneficiaryAccountName;

    private String beneficiaryAccountNo;

    private String beneficiaryBVN;

    private String beneficiaryKYCLevel;

    private String debitAccountName;

    private String debitAccountNo;

    private String debitBVN;

    private String debitKYCLevel;

    private String transactionLocation;

    private String narration;

    private String paymentReference;

    private String amount;

    private String mandateReferenceNo;

    private String transactionFee;

    private String responseCode;
}
