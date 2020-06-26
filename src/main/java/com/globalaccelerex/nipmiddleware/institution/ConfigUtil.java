package com.globalaccelerex.nipmiddleware.institution;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigUtil {

    List<BankConfig> bankConfigList = new ArrayList<BankConfig>();

    @Autowired
    public void setBankConfigs(List<BankConfig> bankConfigList){
     this.bankConfigList.addAll(bankConfigList);
    }

     public BankConfig getBankConfig(String originatingInstitutionCode){
        for(BankConfig bankConfig : bankConfigList){
            if(StringUtils.equalsIgnoreCase(originatingInstitutionCode,BankCodeEnum.GA.getDevEnv()) || StringUtils.equalsIgnoreCase(originatingInstitutionCode,BankCodeEnum.GA.getProdEnv())){
                return bankConfig;
            }

        }
        return null;
     }
}
