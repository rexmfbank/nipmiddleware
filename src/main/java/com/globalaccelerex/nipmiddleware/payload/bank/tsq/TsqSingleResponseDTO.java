package com.globalaccelerex.nipmiddleware.payload.bank.tsq;

import lombok.Data;

@Data
public class TsqSingleResponseDTO {

    private String sessionId;

    private String responseCode;
}
