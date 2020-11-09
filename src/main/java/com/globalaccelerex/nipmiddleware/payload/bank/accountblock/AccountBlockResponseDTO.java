package com.globalaccelerex.nipmiddleware.payload.bank.accountblock;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountBlockResponseDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String referenceCode;

    private String destinationAccountName;

    private String destinationBVN;

    private String destinationAccountNo;

    private String reasonCode;

    private String narration;

    private String responseCode;
}
