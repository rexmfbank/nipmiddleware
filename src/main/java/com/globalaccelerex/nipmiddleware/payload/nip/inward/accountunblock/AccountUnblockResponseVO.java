package com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock;

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
@XmlRootElement(name = "AccountUnblockResponse")
public class AccountUnblockResponseVO {

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

    @XmlElement(name = "ResponseCode")
    private String responseCode;

}
