package com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FinancialInstitutionListRequest")
public class FinancialInstitutionListRequestVO {

    @XmlElement(name = "Header")
    private Header header;

    @XmlElement(name = "Record")
    private List<Record> recordList;
}
