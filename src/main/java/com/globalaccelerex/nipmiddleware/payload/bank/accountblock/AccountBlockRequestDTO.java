package com.globalaccelerex.nipmiddleware.payload.bank.accountblock;

import lombok.Data;


@Data
public class AccountBlockRequestDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String referenceCode;

    private String destinationAccountName;

    private String destinationBVN;

    private String destinationAccountNo;

    private String reasonCode;

    private String narration;

}
