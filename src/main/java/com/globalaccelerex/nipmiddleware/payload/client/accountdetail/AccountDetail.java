package com.globalaccelerex.nipmiddleware.payload.client.accountdetail;

import com.globalaccelerex.nipmiddleware.annotation.Nuban;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AccountDetail {

    @NotBlank(message = "NIP Bank Code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String bankCode;

    @Nuban(ignoreIfEmpty = false)
    @NotBlank(message = "Account No is required")
    private String accountNo;

    private String bvn;

    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed for kycLevel")
    private String kycLevel;

    private String accountName;

    public boolean isDetailsAvailable(){
        return StringUtils.isNoneBlank(bvn,kycLevel,accountName);
    }
}
