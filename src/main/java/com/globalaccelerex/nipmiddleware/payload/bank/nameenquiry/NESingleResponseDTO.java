package com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry;

import com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
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
