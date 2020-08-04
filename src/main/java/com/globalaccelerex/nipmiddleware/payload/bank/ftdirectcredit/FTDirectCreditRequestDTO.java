package com.globalaccelerex.nipmiddleware.payload.bank.ftdirectcredit;

import lombok.Data;


@Data
public class FTDirectCreditRequestDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String nameEnquiryRef; //optional

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
}
