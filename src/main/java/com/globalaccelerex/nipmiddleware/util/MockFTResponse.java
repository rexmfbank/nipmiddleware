package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleResponse;
import lombok.val;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;


public class MockFTResponse {

    public static NESingleResponse buildNESingleResponseVO(NESingleRequest neSingleRequest){
        val neSingleResponse = NESingleResponse.builder()
                .accountName("Ayodeji Ilori")
                .accountNo(neSingleRequest.getAccountNo())
                .bankVerificationNo("2136748372615")
                .destinationInstitutionCode(neSingleRequest.getDestinationBankCode())
                .kycLevel("1")
                .nameEnquiryReference(String.valueOf(System.currentTimeMillis()))
                .build();
        neSingleResponse.setResponseCode(NIP_00.getCode());
        return neSingleResponse;
    }
}
