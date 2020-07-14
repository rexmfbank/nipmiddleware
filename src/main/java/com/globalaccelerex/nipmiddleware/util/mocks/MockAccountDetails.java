package com.globalaccelerex.nipmiddleware.util.mocks;

import lombok.Builder;
import lombok.Getter;

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
        accountDetails = AccountDetails.builder()
                .accountName(defaultAccountName)
                .accountNo(defaultAccountNo)
                .bvn(defaultBVN)
                .kyclevel("2")
                .balance(new BigDecimal(50000000))
                .build();

        mandateHashMap= new HashMap<>();

    }

    @Getter
    @Builder
    class AccountDetails{

        private final String accountName;

        private final String accountNo;

        private final String bvn;

        private final String kyclevel;

        private final BigDecimal balance;
    }


}
