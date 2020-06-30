package com.globalaccelerex.nipmiddleware.institution;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConfigUtil {

    List<BankConfig> bankConfigList = new ArrayList<BankConfig>();

    @Autowired
    public void setBankConfigs(List<BankConfig> bankConfigList){
     this.bankConfigList.addAll(bankConfigList);
    }

     public BankConfig getBankConfig(String originatingInstitutionCode){
        for(BankConfig bankConfig : bankConfigList){
            if(StringUtils.equalsIgnoreCase(bankConfig.getInstitutionCode(),originatingInstitutionCode)){
                if(BankCodeEnum.isGA(originatingInstitutionCode)){
                    GAConfig gaConfig = (GAConfig) bankConfig;
                    log.info("\n\nGA ::::::::: {}\n\n" , gaConfig.toString());
                    return gaConfig;
                }
                if(BankCodeEnum.isSLS(originatingInstitutionCode)){
                    SLSConfig slsConfig = (SLSConfig) bankConfig;
                    log.info("\n\nSLS ::::::::: {}\n\n" , slsConfig.toString());
                    return slsConfig;
                }
            }
        }
        return null;
     }
}
