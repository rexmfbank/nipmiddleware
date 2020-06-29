package com.globalaccelerex.nipmiddleware.institution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public enum  BankCodeEnum {

    GA("999157","090202");

    @Getter
    private final String devEnv;

    @Getter
    private final String prodEnv;



}
