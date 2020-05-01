package com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry;

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
@XmlRootElement(name = "NESingleResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class NESingleResponseVO {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "DestinationInstitutionCode")
    private String destinationInstitutionCode;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "AccountNumber")
    private String accountNo;

    @XmlElement(name = "AccountName")
    private String accountName;

    @XmlElement(name = "BankVerificationNumber")
    private String bvn;

    @XmlElement(name = "KYCLevel")
    private String kycLevel;

    @XmlElement(name = "ResponseCode")
    private String responseCode;
}
