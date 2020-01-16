package com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry;

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
@XmlRootElement(name = "BalanceEnquiryResponse")
public class BalanceEnquiryResponseVO {

    @XmlElement(name = "SessionID")
    private String sessionID;

    @XmlElement(name = "DestinationInstitutionCode")
    private String destinationInstitutionCode;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "AuthorizationCode")
    private String authorizationCode;

    @XmlElement(name = "TargetAccountName")
    private String targetAccountName;

    @XmlElement(name = "TargetBankVerificationNumber")
    private String targetBankVerificationNo;

    @XmlElement(name = "TargetAccountNumber")
    private String targetAccountNo;

    @XmlElement(name = "AvailableBalance")
    private String availableBalance;

    @XmlElement(name = "ResponseCode")
    private String responseCode;
}
