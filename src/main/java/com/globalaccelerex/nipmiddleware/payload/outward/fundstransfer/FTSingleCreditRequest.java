package com.globalaccelerex.nipmiddleware.payload.outward.fundstransfer;

import com.globalaccelerex.nipmiddleware.annotation.Nuban;
import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class FTSingleCreditRequest extends BaseRequest {

    @NotBlank (message = "destination bank code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String destinationBankCode;

    @Nuban
    @NotBlank(message = "destination account number is required")
    private String destinationAccountNo;

    @NotBlank(message = "payment reference is required")
    private String paymentReference;

    @Nuban
    @NotBlank(message = "originator account no is required")
    private String originatorAccountNo;

    @DecimalMin(value = "0.00", inclusive = false ,message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Originator Bank Code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String originatorBankCode;

    private String nameEnquiryReference;

    private String beneficiaryAccountName;

    private String beneficiaryBVN; // optional

    private String beneficiaryKYCLevel;//optional

    private String originatorAccountName;//optional

    private String originatorBVN;

    private String originatorKYCLevel;

    private String narration;//max 100 , optional

    private String transactionLocation;

    public void updateCompulsoryFields(ClientEntity clientEntity){
        if (StringUtils.isBlank(originatorAccountName)){
            originatorAccountName= clientEntity.getAccountName();
        }
        if(StringUtils.isBlank(originatorBVN)){
            originatorBVN = clientEntity.getBvn();
        }
        if(StringUtils.isBlank(originatorKYCLevel)){
            originatorKYCLevel = clientEntity.getKycLevel();
        }
        if(StringUtils.isBlank(narration)){
            narration = "Transaction of " + amount;
        }
    }

}

