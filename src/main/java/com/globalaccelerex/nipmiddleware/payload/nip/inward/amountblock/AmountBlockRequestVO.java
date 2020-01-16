package com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock;

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
@XmlRootElement(name = "AmountBlockRequest")
public class AmountBlockRequestVO {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "DestinationInstitutionCode")
    private String destinationInstitutionCode;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "ReferenceCode")
    private String referenceCode;

    @XmlElement(name = "TargetAccountName")
    private String targetAccountName;

    @XmlElement(name = "TargetBankVerificationNumber")
    private String targetBVN;

    @XmlElement(name = "TargetAccountNumber")
    private String targetAccountNo;

    @XmlElement(name = "ReasonCode")
    private String reasonCode;

    @XmlElement(name = "Narration")
    private String narration;

    @XmlElement(name = "Amount")
    private String amount;
}
