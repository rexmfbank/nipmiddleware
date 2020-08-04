package com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry;

import lombok.Data;

@Data
public class NESingleRequestDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String accountNo;
}
