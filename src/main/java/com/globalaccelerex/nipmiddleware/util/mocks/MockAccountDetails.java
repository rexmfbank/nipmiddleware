package com.globalaccelerex.nipmiddleware.util.mocks;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MockAccountDetails {

    @Getter
    private static final Map<String ,String> mandateHashMap;

    @Getter
    private static final AccountDetails accountDetails;

    private static final String defaultAccountNo = "0023456782";

    private static final String defaultAccountName = "ADEYEMI TENIOLA";

    private static final String defaultBVN = "2136748372615";

    static {
        accountDetails = new AccountDetails();
        accountDetails.setAccountName(defaultAccountName);
        accountDetails.setAccountNo(defaultAccountNo);
        accountDetails.setBvn(defaultBVN);
        accountDetails.setKyclevel("2");
        accountDetails.setBalance(new BigDecimal(50000000));

        mandateHashMap= new HashMap<>();
    }

    @Getter
    @Setter
    static
    class AccountDetails{

        private  String accountName;

        private  String accountNo;

        private  String bvn;

        private  String kyclevel;

        private  BigDecimal balance;
    }


}
