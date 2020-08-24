package com.globalaccelerex.nipmiddleware.payload.bank.fiList;

import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.Header;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.Record;
import lombok.Data;

import java.util.List;

@Data
public class FinancialInstitutionListRequestDTO {

    private Header header;

    private List<Record> recordList;
}
