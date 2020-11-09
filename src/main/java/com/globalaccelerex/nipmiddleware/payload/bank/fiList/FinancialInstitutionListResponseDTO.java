package com.globalaccelerex.nipmiddleware.payload.bank.fiList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialInstitutionListResponseDTO {

    private String batchNumber;

    private String destinationInstitutionCode;

    private String channelCode;

    private String numberOfRecords;

    private String responseCode;
}
