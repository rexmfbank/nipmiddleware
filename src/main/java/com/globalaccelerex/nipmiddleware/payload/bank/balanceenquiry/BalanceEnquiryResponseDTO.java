package com.globalaccelerex.nipmiddleware.payload.bank.balanceenquiry;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BalanceEnquiryResponseDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String authorizationCode;

    private String accountName;

    private String bvn;
    private String accountNo;

    private String availableBalance;

    private String responseCode;

    private String responseDescription;

    public String getResponseDescription() {
        return NIPResponseCodeEnum.getResponseCodeEnum(responseCode).getDescription();
    }


}
