package com.globalaccelerex.nipmiddleware.payload.bank.balanceenquiry;

import lombok.Data;


@Data
public class BalanceEnquiryResponseDTO {

    private String sessionID;

    private String destinationInstitutionCode;

    private String authorizationCode;

    private String accountName;

    private String bvn;
    private String accountNo;

    private String availableBalance;

    private String responseCode;
}
