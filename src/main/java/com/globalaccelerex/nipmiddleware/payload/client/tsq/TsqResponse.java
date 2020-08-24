package com.globalaccelerex.nipmiddleware.payload.client.tsq;

import com.globalaccelerex.nipmiddleware.payload.client.BaseResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TsqResponse extends BaseResponse {

    private String sessionId;

    private String destinationBankCode;

    private String destinationAccountNo;

    private String beneficiaryAccountName;

    private String beneficiaryBVN;

    private String beneficiaryKYCLevel;

    private String originatorAccountName;

    private String originatorAccountNo;

    private String originatorBVN;

    private String originatorKYCLevel;

    private String transactionLocation;

    private String narration;

    private String paymentReference;

    private BigDecimal amount;

    private String nameEnquiryReference;

    private String originatorBankCode;
}
