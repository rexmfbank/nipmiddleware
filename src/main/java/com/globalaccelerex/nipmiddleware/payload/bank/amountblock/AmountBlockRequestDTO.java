package com.globalaccelerex.nipmiddleware.payload.bank.amountblock;

import lombok.Data;

@Data
public class AmountBlockRequestDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String referenceCode;

    private String destinationAccountName;

    private String destinationBVN;

    private String destinationAccountNo;

    private String reasonCode;

    private String narration;

    private String amount;
}
