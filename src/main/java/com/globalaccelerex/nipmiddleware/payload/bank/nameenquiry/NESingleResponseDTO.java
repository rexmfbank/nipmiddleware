package com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NESingleResponseDTO {

    private String sessionId;

    private String destinationInstitutionCode;

    private String accountNo;

    private String accountName;

    private String bvn;

    private String kycLevel;

    private String responseCode;

    private String responseDescription;

    public String getResponseDescription() {
        return NIPResponseCodeEnum.getResponseCodeEnum(responseCode).getDescription();
    }
}
