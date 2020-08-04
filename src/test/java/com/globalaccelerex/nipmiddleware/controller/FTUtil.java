package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.payload.client.fundstransfer.FTSingleCreditRequest;
import lombok.val;

import java.math.BigDecimal;

public class FTUtil {

    public static FTSingleCreditRequest buildAmountEqualToZero(){
        final val ftSingleCreditRequest = new FTSingleCreditRequest();
        ftSingleCreditRequest.setDestinationBankCode("999070");
        ftSingleCreditRequest.setPaymentReference(String.valueOf(System.currentTimeMillis()));
        ftSingleCreditRequest.setBeneficiaryAccountName("DAMBATTA MARYAM LAMI");
        ftSingleCreditRequest.setDestinationAccountNo("5050104057");
        ftSingleCreditRequest.setOriginatorAccountName("Ayodeji Ilori");
        ftSingleCreditRequest.setOriginatorAccountNo("0036722182");
        ftSingleCreditRequest.setAmount(BigDecimal.valueOf(0.0));
        ftSingleCreditRequest.setClientId("nip01");
        ftSingleCreditRequest.setNarration("Testing 101");
        return ftSingleCreditRequest;
    }

    public static FTSingleCreditRequest buildExisitingPaymentReference(){
        final val ftSingleCreditRequest = new FTSingleCreditRequest();
        ftSingleCreditRequest.setDestinationBankCode("999070");
        ftSingleCreditRequest.setPaymentReference("22000114697");
        ftSingleCreditRequest.setBeneficiaryAccountName("DAMBATTA MARYAM LAMI");
        ftSingleCreditRequest.setDestinationAccountNo("5050104057");
        ftSingleCreditRequest.setOriginatorAccountName("Ayodeji Ilori");
        ftSingleCreditRequest.setOriginatorAccountNo("0036722182");
        ftSingleCreditRequest.setAmount(BigDecimal.valueOf(36.78));
        ftSingleCreditRequest.setClientId("nip01");
        ftSingleCreditRequest.setNarration("Testing 101");
        return ftSingleCreditRequest;
    }
}
