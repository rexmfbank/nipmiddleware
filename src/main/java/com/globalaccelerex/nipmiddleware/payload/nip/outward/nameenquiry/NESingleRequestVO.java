package com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@ToString
@XmlRootElement(name = "NESingleRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class NESingleRequestVO {

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "DestinationInstitutionCode")
    private String destinationInstitutionCode;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "AccountNumber")
    private String accountNo;
}
