package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import org.springframework.beans.factory.annotation.Autowired;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleResponseVO;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;

@Service
public class MockResponseUtil {

    private final NipConfig nipConfig;

    private final SessionIdUtil sessionIdUtil;

    @Autowired
    public MockResponseUtil(NipConfig nipConfig, SessionIdUtil sessionIdUtil) {
        this.nipConfig = nipConfig;
        this.sessionIdUtil = sessionIdUtil;
    }

    public NESingleResponseVO buildNESingleResponseVO(NESingleRequestVO neSingleRequestVO){
        return NESingleResponseVO.builder()
                .accountName("Ayodeji Ilori")
                .accountNo("0023456782")
                .bvn("2136748372615")
                .channelCode(neSingleRequestVO.getChannelCode())
                .destinationInstitutionCode(neSingleRequestVO.getDestinationInstitutionCode())
                .kycLevel("1")
                .responseCode(NIP_00.getCode())
                .sessionId(sessionIdUtil.generateSessionId())
                .build();
    }

}
