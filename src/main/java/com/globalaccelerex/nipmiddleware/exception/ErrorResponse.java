package com.globalaccelerex.nipmiddleware.exception;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String responseMessage;

    private String responseCode;



}

