package com.globalaccelerex.nipmiddleware.payload.bank.mandateadvice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MandateAdviceResponseDTO {

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

    private String responseCode;
}
