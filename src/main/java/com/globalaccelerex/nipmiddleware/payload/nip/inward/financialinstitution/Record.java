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
@XmlRootElement(name = "Record")
public class Record {

    @XmlElement(name = "InstitutionCode")
    private String institutionCode;

    @XmlElement(name = "InstitutionName")
    private String institutionName;

    @XmlElement(name = "Category")
    private String category;
}