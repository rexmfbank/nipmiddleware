package com.globalaccelerex.nipmiddleware.util.mocks;

import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleResponseVO;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;

public class MockNE {

    static Map<String,NESingleResponseVO> accountHashMap = new HashMap<>();

    static {
        val accountDetails = MockAccountDetails.getAccountDetails();
        val neSingleResponseVO = NESingleResponseVO.builder()
                .accountName(accountDetails.getAccountName())
                .accountNo(accountDetails.getAccountNo())
                .bvn(accountDetails.getBvn())
                .kycLevel(accountDetails.getKyclevel())
                .responseCode(NIP_00.getCode())
                .build();
        accountHashMap.put(neSingleResponseVO.getAccountNo(),neSingleResponseVO);
    }

    public static NESingleResponseVO handleNameEnquiry(NESingleRequestVO neSingleRequestVO){
        val neSingleResponseVO = accountHashMap.get(neSingleRequestVO.getAccountNo());
        if(neSingleResponseVO == null){
            return NESingleResponseVO.builder()
                    .accountNo(neSingleRequestVO.getAccountNo())
                    .responseCode(NIP_201.getCode())
                    .destinationInstitutionCode(neSingleRequestVO.getDestinationInstitutionCode())
                    .build();
        }else{
            neSingleResponseVO.setChannelCode(neSingleRequestVO.getChannelCode());
            neSingleResponseVO.setDestinationInstitutionCode(neSingleRequestVO.getDestinationInstitutionCode());
            return neSingleResponseVO;
        }
    }
}
