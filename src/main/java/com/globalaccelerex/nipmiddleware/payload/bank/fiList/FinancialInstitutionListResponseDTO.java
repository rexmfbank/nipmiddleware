package com.globalaccelerex.nipmiddleware.payload.bank.fiList;

import lombok.Data;

@Data
public class FinancialInstitutionListResponseDTO {

    private String batchNumber;

    private String destinationInstitutionCode;

    private String channelCode;

    private String numberOfRecords;

    private String responseCode;
}
