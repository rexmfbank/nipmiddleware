package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.institution.BankConfig;
import com.globalaccelerex.nipmiddleware.institution.ConfigUtil;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nfp.ssm.core.SSMLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.ORIGINATING_BANK_CODE_NOT_FOUND_MSG;

@Slf4j
@Component
public class SSMUtil {

    @Autowired
    private ConfigUtil configUtil;

    public String encryptRequest(String dataToEncrypt, String originatingInstitutionCode, IMarker marker){
        val bankConfig = retrieveBankConfig(originatingInstitutionCode,marker);
        SSMLib ssmLib = new SSMLib(bankConfig.getPublicKeyPath(),bankConfig.getPrivateKeyPath());
        return ssmLib.encryptMessage(dataToEncrypt);
    }

    public String decryptResponse(String dataToDecrypt,String originatingInstitutionCode, IMarker marker){
        val bankConfig = retrieveBankConfig(originatingInstitutionCode,marker);
        SSMLib ssmLib = new SSMLib(bankConfig.getPublicKeyPath(),bankConfig.getPrivateKeyPath());
        return ssmLib.decryptFile(dataToDecrypt, bankConfig.getPasswordKey());
    }

    private BankConfig retrieveBankConfig(String originatingInstitutionCode,IMarker marker){
        val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
        if(bankConfig == null){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(ORIGINATING_BANK_CODE_NOT_FOUND_MSG,marker);
            throw nipMiddleWareAPIException;
        }
        return bankConfig;
    }
}
