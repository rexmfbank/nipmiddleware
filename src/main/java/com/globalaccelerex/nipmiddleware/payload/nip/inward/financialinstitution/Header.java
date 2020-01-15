package com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution;

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
@XmlRootElement(name = "Header")
public class Header {

    @XmlElement(name = "BatchNumber")
    private String batchNumber ;

    @XmlElement(name = "NumberOfRecords")
    private String numberOfRecords;

    @XmlElement(name = "ChannelCode")
    private String channelCode;

    @XmlElement(name = "TransactionLocation")
    private String transactionLocation;
}