package com.globalaccelerex.nipmiddleware.payload.bank.fiList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialInstitutionListResponseDTO {

    private String batchNumber;

    private String destinationInstitutionCode;

    private String channelCode;

    private String numberOfRecords;

    private String responseCode;
}
