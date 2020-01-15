package com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "FTSingleDebitRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class FTDirectDebitRequestVO {

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


    @XmlElement(name = "DebitAccountName")
    private String debitAccountName;


    @XmlElement(name = "DebitAccountNumber")
    private String debitAccountNo;


    @XmlElement(name = "DebitBankVerificationNumber")
    private String debitBVN;

    @XmlElement(name = "DebitKYCLevel")
    private String debitKYCLevel;

    @XmlElement(name = "TransactionLocation")
    private String transactionLocation;

    @XmlElement(name = "Narration")
    private String narration;

    @XmlElement(name = "PaymentReference")
    private String paymentReference;

    @XmlElement(name = "Amount")
    private String amount;

    @XmlElement(name = "MandateReferenceNumber")
    private String mandateReferenceNo;

    @XmlElement(name = "TransactionFee")
    private String transactionFee;
}
