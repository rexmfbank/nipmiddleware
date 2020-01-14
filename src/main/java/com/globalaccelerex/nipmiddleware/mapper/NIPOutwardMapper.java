package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleResponse;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.util.SessionIdUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static com.globalaccelerex.nipmiddleware.enums.ChannelCodesEnum.CC_1;

@Slf4j
@Service
public class NIPOutwardMapper {

    @Autowired
    private SessionIdUtil sessionIdUtil;

    @Autowired
    private NipConfig nipConfig;


    public Function<NESingleRequest, NESingleRequestVO> mapNESingleRequestVO = neSingleRequest -> {
        NESingleRequestVO requestVO = new NESingleRequestVO();
        requestVO.setAccountNo(neSingleRequest.getAccountNo());
        requestVO.setChannelCode(String.valueOf(CC_1.getCode()));
        requestVO.setDestinationInstitutionCode(neSingleRequest.getDestinationInstitutionCode());
        requestVO.setSessionId(sessionIdUtil.generateSessionId());
        return requestVO;
    };

    public Function<NESingleResponseVO, NESingleResponse> mapNESingleResponseVO = neSingleResponseVO -> {
        final val neSingleResponse = NESingleResponse.builder()
                .accountName(neSingleResponseVO.getAccountName())
                .accountNo(neSingleResponseVO.getAccountNo())
                .bankVerificationNo(neSingleResponseVO.getBvn())
                .kycLevel(neSingleResponseVO.getKycLevel())
                .build();
        neSingleResponse.setResponseCode(neSingleResponseVO.getResponseCode());
        neSingleResponse.setSessionId(neSingleResponseVO.getSessionId());
        return neSingleResponse;
    };
}
