package com.globalaccelerex.nipmiddleware.util.mocks;

import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryResponseVO;
import lombok.val;

import java.util.Map;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_05;

public class MockTransaction {

    private static final Map<String, String> mandateHashMap;
    static {
        mandateHashMap = MockAccountDetails.getMandateHashMap();
    }

    static BalanceEnquiryResponseVO handleBalanceEnquiry(BalanceEnquiryRequestVO balanceEnquiryRequestVO){
        val balanceEnquiryResponseVO = BalanceEnquiryResponseVO.builder()
                .authorizationCode(balanceEnquiryRequestVO.getAuthorizationCode())
                .channelCode(balanceEnquiryRequestVO.getChannelCode())
                .destinationInstitutionCode(balanceEnquiryRequestVO.getDestinationInstitutionCode())
                .build();
        if(mandateHashMap.containsKey(balanceEnquiryRequestVO.getAuthorizationCode())){
            val accountDetails = MockAccountDetails.getAccountDetails();
            balanceEnquiryResponseVO.setAvailableBalance(String.valueOf(accountDetails.getBalance()));
            balanceEnquiryResponseVO.setResponseCode(NIP_00.getCode());
            balanceEnquiryResponseVO.setTargetAccountName(balanceEnquiryRequestVO.getTargetAccountName());
            balanceEnquiryResponseVO.setTargetAccountNo(balanceEnquiryRequestVO.getTargetAccountNo());
            balanceEnquiryResponseVO.setTargetBankVerificationNo(balanceEnquiryRequestVO.getTargetBankVerificationNo());
        }else {
            balanceEnquiryResponseVO.setResponseCode(NIP_05.getCode());
        }
        return balanceEnquiryResponseVO;
    }
}
