package com.globalaccelerex.nipmiddleware.util.mocks;

import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceResponseVO;
import lombok.val;

import java.util.Map;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_94;

public class MockMandateAdvice {

    private static final Map<String,String> mandateHashMap ;
    static {
        mandateHashMap = MockAccountDetails.getMandateHashMap();
    }

    public static MandateAdviceResponseVO handleMandate(MandateAdviceRequestVO mandateAdviceRequestVO){
        val mandateAdviceResponseVO = MandateAdviceResponseVO.builder()
                .amount(mandateAdviceRequestVO.getAmount())
                .beneficiaryAccountName(mandateAdviceRequestVO.getBeneficiaryAccountName())
                .beneficiaryAccountNo(mandateAdviceRequestVO.getBeneficiaryAccountNo())
                .beneficiaryBVN(mandateAdviceRequestVO.getBeneficiaryBVN())
                .beneficiaryKYCLevel(mandateAdviceRequestVO.getBeneficiaryKYCLevel())
                .channelCode(mandateAdviceRequestVO.getChannelCode())
                .debitAccountName(mandateAdviceRequestVO.getDebitAccountName())
                .debitAccountNo(mandateAdviceRequestVO.getDebitAccountNo())
                .debitBVN(mandateAdviceRequestVO.getDebitBVN())
                .debitKYCLevel(mandateAdviceRequestVO.getDebitKYCLevel())
                .destinationCode(mandateAdviceRequestVO.getDestinationCode())
                .mandateReferenceNo(mandateAdviceRequestVO.getMandateReferenceNo())
                .build();
        if(mandateHashMap.containsKey(mandateAdviceRequestVO.getMandateReferenceNo())){
            mandateAdviceResponseVO.setResponseCode(NIP_94.getCode());
        }else {
            mandateHashMap.put(mandateAdviceRequestVO.getMandateReferenceNo(),mandateAdviceRequestVO.getAmount());
            mandateAdviceResponseVO.setResponseCode(NIP_00.getCode());
        }
        return mandateAdviceResponseVO;
    }



}
