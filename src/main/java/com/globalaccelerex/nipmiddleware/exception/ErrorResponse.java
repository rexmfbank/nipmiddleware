package com.globalaccelerex.nipmiddleware.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String responseMessage;

    private String responseCode;

    private boolean retry;

}

