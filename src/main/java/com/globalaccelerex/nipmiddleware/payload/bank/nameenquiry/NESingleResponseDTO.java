package com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry;

import lombok.Data;

@Data
public class NESingleResponseDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String accountNo;

    private String accountName;

    private String bvn;

    private String kycLevel;

    private String responseCode;
}
