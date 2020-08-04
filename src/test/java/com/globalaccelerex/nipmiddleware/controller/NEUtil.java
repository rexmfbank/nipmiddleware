package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.payload.client.nameenquiry.NESingleRequest;
import lombok.val;

public class NEUtil {

    public static NESingleRequest buildNESingleRequestWithAlphaNumericAccountNo(){
        val neSingleRequest = NESingleRequest.builder()
                .accountNo("abcd000000")
                .destinationBankCode("00324")
                .originatorBankCode("23455")
                .build();
        neSingleRequest.setClientId("nip01");
        return neSingleRequest;
    }

    public static NESingleRequest buildNESingleRequestWith12DigitsAccountNo(){
        val neSingleRequest = NESingleRequest.builder()
                .accountNo("002345637281")
                .destinationBankCode("00324")
                .originatorBankCode("23455")
                .build();
        neSingleRequest.setClientId("nip01");
        return neSingleRequest;
    }

    public static NESingleRequest buildAphaNumericBankCode(){
        val neSingleRequest = NESingleRequest.builder()
                .accountNo("0036722182")
                .destinationBankCode("0224A")
                .originatorBankCode("23455")
                .build();
        neSingleRequest.setClientId("nip01");
        return neSingleRequest;
    }
}
