package com.globalaccelerex.nipmiddleware.payload.bank.accountunblock;

import lombok.Data;

@Data
public class AccountUnblockRequestDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String referenceCode;

    private String destinationAccountName;

    private String destinationBVN;

    private String destinationAccountNo;

    private String reasonCode;

    private String narration;
}
