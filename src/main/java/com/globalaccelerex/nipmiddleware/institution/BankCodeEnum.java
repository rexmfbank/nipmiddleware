package com.globalaccelerex.nipmiddleware.institution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@AllArgsConstructor
public enum  BankCodeEnum {

    GA("999157","090202"),

    SLS("999186","");

    @Getter
    private final String devEnv;

    @Getter
    private final String prodEnv;

    public static boolean isGA(String originatingInstitutionCode){
        return (StringUtils.equalsIgnoreCase(originatingInstitutionCode,GA.getDevEnv()) || StringUtils.equalsIgnoreCase(originatingInstitutionCode,GA.getProdEnv()));
    }

    public static boolean isSLS(String originatingInstitutionCode){
        return (StringUtils.equalsIgnoreCase(originatingInstitutionCode,SLS.getDevEnv()) || StringUtils.equalsIgnoreCase(originatingInstitutionCode,SLS.getProdEnv()));
    }
}
