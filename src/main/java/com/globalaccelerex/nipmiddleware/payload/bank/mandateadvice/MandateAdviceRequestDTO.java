package com.globalaccelerex.nipmiddleware.payload.bank.mandateadvice;

import lombok.Data;


@Data
public class MandateAdviceRequestDTO {

    private String sessionId;

    private String destinationCode;

    private String mandateReferenceNo;

    private String amount;

    private String debitAccountName;

    private String debitAccountNo;

    private String debitBVN;

    private String debitKYCLevel;

    private String beneficiaryAccountName;

    private String beneficiaryAccountNo;

    private String beneficiaryBVN;

    private String beneficiaryKYCLevel;
}
