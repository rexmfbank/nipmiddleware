package com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "TSQuerySingleResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TsqSingleItemResponseVO {

    @XmlElement(name = "SourceInstitutionCode")
    private String sourceInstitutionCode;

    @XmlElement(name = "SessionID")
    private String sessionId;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "ResponseCode")
    private String responseCode;

}
