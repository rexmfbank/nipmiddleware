package com.globalaccelerex.nipmiddleware.payload.bank.balanceenquiry;

import lombok.Data;

@Data
public class BalanceEnquiryRequestDTO {


    private String sessionId;

    private String destinationInstitutionCode;

    private String authorizationCode;

    private String accountName;

    private String bvn;
    private String accountNo;
}
