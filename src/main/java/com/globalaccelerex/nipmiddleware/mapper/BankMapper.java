package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry.NESingleRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry.NESingleResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleResponseVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class BankMapper {

    public Function<NESingleRequestVO, NESingleRequestDTO>  mapNESingleRequestDTO = neSingleRequestVO -> {
        final val neSingleRequestDTO = new NESingleRequestDTO();
        neSingleRequestDTO.setAccountNo(neSingleRequestVO.getAccountNo());
        neSingleRequestDTO.setDestinationInstitutionCode(neSingleRequestVO.getDestinationInstitutionCode());
        neSingleRequestDTO.setSessionId(neSingleRequestVO.getSessionId());
        return neSingleRequestDTO;
    };

    public Function<NESingleResponseDTO , NESingleResponseVO> mapNESingleResponseVO = neSingleResponseDTO -> {
        final val neSingleResponseVO = new NESingleResponseVO();
        neSingleResponseDTO.setAccountName(neSingleResponseDTO.getAccountName());
        neSingleResponseDTO.setAccountNo(neSingleResponseVO.getAccountNo());
        neSingleResponseDTO.setBvn(neSingleResponseVO.getBvn());
        neSingleResponseDTO.setDestinationInstitutionCode(neSingleResponseVO.getDestinationInstitutionCode());
        neSingleResponseDTO.setKycLevel(neSingleResponseVO.getKycLevel());
        neSingleResponseDTO.setResponseCode(neSingleResponseVO.getResponseCode());
        neSingleResponseDTO.setSessionId(neSingleResponseVO.getSessionId());
        return neSingleResponseVO;
    };
}
