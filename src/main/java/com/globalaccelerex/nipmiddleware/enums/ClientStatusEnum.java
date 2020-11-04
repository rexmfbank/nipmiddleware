package com.globalaccelerex.nipmiddleware.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public enum ClientStatusEnum {

    ACTIVE("Active") , DISABLED("Disabled");

    @Getter
    private final String status;
}
