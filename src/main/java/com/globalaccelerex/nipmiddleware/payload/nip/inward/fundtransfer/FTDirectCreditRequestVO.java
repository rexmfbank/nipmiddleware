package com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FTSingleCreditRequest")
public class FTDirectCreditRequestVO {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "DestinationInstitutionCode")
    private String destinationInstitutionCode;

    @XmlElement(name = "NameEnquiryRef")
    private String nameEnquiryRef; //optional

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "BeneficiaryAccountName")
    private String beneficiaryAccountName;

    @XmlElement(name = "BeneficiaryAccountNumber")
    private String beneficiaryAccountNo;

    @XmlElement(name = "BeneficiaryBankVerificationNumber")
    private String beneficiaryBVN;

    @XmlElement(name = "BeneficiaryKYCLevel")
    private String beneficiaryKYCLevel;

    @XmlElement(name = "OriginatorAccountName")
    private String originatorAccountName;

    @XmlElement(name = "OriginatorAccountNumber")
    private String originatorAccountNo;

    @XmlElement(name = "OriginatorBankVerificationNumber")
    private String originatorBVN;

    @XmlElement(name = "OriginatorKYCLevel")
    private String originatorKYCLevel;

    @XmlElement(name = "TransactionLocation")
    private String transactionLocation;

    @XmlElement(name = "Narration")
    private String narration;

    @XmlElement(name = "PaymentReference")
    private String paymentReference;

    @XmlElement(name = "Amount")
    private String amount;
}
