package com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq;

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
@XmlRootElement(name = "TSQuerySingleRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class TsqSingleItemRequestVO {

    @XmlElement(name = "SourceInstitutionCode")
    private String sourceInstitutionCode;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "SessionID")
    private String sessionId;




}

