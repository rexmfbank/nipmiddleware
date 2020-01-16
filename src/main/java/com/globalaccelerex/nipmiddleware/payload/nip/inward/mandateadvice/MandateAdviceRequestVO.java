package com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MandateAdviceRequest")
public class MandateAdviceRequestVO {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "DestinationInstitutionCode")
    private String destinationCode;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "MandateReferenceNumber")
    private String mandateReferenceNo;

    @XmlElement(name = "Amount")
    private String amount;

    @XmlElement(name = "DebitAccountName")
    private String debitAccountName;

    @XmlElement(name = "DebitAccountNumber")
    private String debitAccountNo;

    @XmlElement(name = "DebitBankVerificationNumber")
    private String debitBVN;

    @XmlElement(name = "DebitKYCLevel")
    private String debitKYCLevel;

    @XmlElement(name = "BeneficiaryAccountName")
    private String beneficiaryAccountName;

    @XmlElement(name = "BeneficiaryAccountNumber")
    private String beneficiaryAccountNo;

    @XmlElement(name = "BeneficiaryBankVerificationNumber")
    private String beneficiaryBVN;

    @XmlElement(name = "BeneficiaryKYCLevel")
    private String beneficiaryKYCLevel;
}
