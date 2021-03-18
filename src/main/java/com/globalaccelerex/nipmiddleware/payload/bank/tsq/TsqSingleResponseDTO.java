package com.globalaccelerex.nipmiddleware.payload.bank.tsq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TsqSingleResponseDTO {

    private String sessionId;

    private String responseCode;
}
