package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleResponse;


public class MockFTResponse {

    public static NESingleResponse buildNESingleResponseVO(NESingleRequest neSingleRequest){
        return NESingleResponse.builder()
                .accountName("Ayodeji Ilori")
                .accountNo(neSingleRequest.getAccountNo())
                .bankVerificationNo("2136748372615")
                .destinationInstitutionCode(neSingleRequest.getDestinationBankCode())
                .kycLevel("1")
                .nameEnquiryReference(String.valueOf(System.currentTimeMillis()))
                .build();
    }
}
