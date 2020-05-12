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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FTSingleCreditRequest extends BaseRequest {

    @NotBlank (message = "destination bank code is required")
    @Pattern(regexp = "^[0-9]*$" , message = "Only digits are allowed ")
    private String destinationBankCode;

    @Nuban(ignoreIfEmpty = false)
    @NotBlank(message = "destination account number is required")
    private String destinationAccountNo;

    @NotBlank(message = "payment reference is required")
    private String paymentReference;

    @Nuban(ignoreIfEmpty = true)
    private String originatorAccountNo;

    @DecimalMin(value = "0.00", inclusive = false ,message = "Amount must be greater than zero")
    private BigDecimal amount;

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

    @DecimalMin(value = "-90", inclusive = true ,message = "Latitude can not be less than -90")
    @DecimalMin(value = "90", inclusive = true ,message = "Latitude can not be greater than 90")
    private Double latitude ;

    @DecimalMin(value = "-180", inclusive = true ,message = "Longitude can not be less than -180")
    @DecimalMin(value = "180", inclusive = true ,message = "Longitude can not be greater than 180")
    private Double longitude ;

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
        if(StringUtils.isBlank(originatorAccountNo)){
            originatorAccountNo = clientEntity.getAccountNo();
        }
        if(StringUtils.isBlank(originatorBankCode)){
            originatorBankCode = clientEntity.getBankCode();
        }
        if(latitude == null && StringUtils.isNotBlank(clientEntity.getLatitude())){
            latitude = Double.valueOf(clientEntity.getLatitude());
        }
        if(longitude == null && StringUtils.isNotBlank(clientEntity.getLongitude())){
            longitude = Double.valueOf(clientEntity.getLongitude());
        }
    }

}

