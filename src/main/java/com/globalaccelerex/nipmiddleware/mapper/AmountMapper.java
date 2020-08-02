package com.globalaccelerex.nipmiddleware.mapper;


import com.globalaccelerex.nipmiddleware.payload.bank.amountblock.AmountBlockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.amountblock.AmountBlockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.amountunblock.AmountUnblockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.amountunblock.AmountUnblockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountunblock.AmountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountunblock.AmountUnblockResponseVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class AmountMapper {

    public Function<AmountBlockRequestVO, AmountBlockRequestDTO> mapAmountBlockRequestDTO = amountBlockRequestVO -> {
        final val amountBlockRequestDTO = new AmountBlockRequestDTO();
        amountBlockRequestDTO.setDestinationAccountName(amountBlockRequestVO.getTargetAccountName());
        amountBlockRequestDTO.setDestinationAccountNo(amountBlockRequestVO.getTargetAccountNo());
        amountBlockRequestDTO.setDestinationBVN(amountBlockRequestVO.getTargetBVN());
        amountBlockRequestDTO.setDestinationInstitutionCode(amountBlockRequestVO.getDestinationInstitutionCode());
        amountBlockRequestDTO.setNarration(amountBlockRequestVO.getNarration());
        amountBlockRequestDTO.setReasonCode(amountBlockRequestVO.getReasonCode());
        amountBlockRequestDTO.setReferenceCode(amountBlockRequestVO.getReferenceCode());
        amountBlockRequestDTO.setSessionId(amountBlockRequestVO.getSessionId());
        return amountBlockRequestDTO;
    };

    public Function<AmountBlockResponseDTO, AmountBlockResponseVO> mapAmountBlockResponseVO = amountBlockResponseDTO -> {
        final val amountBlockResponseVO = new AmountBlockResponseVO();
        amountBlockResponseVO.setTargetAccountName(amountBlockResponseDTO.getDestinationAccountName());
        amountBlockResponseVO.setTargetAccountNo(amountBlockResponseDTO.getDestinationAccountNo());
        amountBlockResponseVO.setTargetBVN(amountBlockResponseDTO.getDestinationBVN());
        amountBlockResponseVO.setDestinationInstitutionCode(amountBlockResponseDTO.getDestinationInstitutionCode());
        amountBlockResponseVO.setNarration(amountBlockResponseDTO.getNarration());
        amountBlockResponseVO.setReasonCode(amountBlockResponseDTO.getReasonCode());
        amountBlockResponseVO.setReferenceCode(amountBlockResponseDTO.getReferenceCode());
        amountBlockResponseVO.setSessionId(amountBlockResponseDTO.getSessionId());
        amountBlockResponseVO.setResponseCode(amountBlockResponseDTO.getResponseCode());
        return amountBlockResponseVO;
    };

    public Function<AmountUnblockRequestVO, AmountUnblockRequestDTO> mapAmountUnblockRequestDTO = amountUnblockRequestVO -> {
        final val amountUnblockRequestDTO = new AmountUnblockRequestDTO();
        amountUnblockRequestDTO.setDestinationAccountName(amountUnblockRequestVO.getTargetAccountName());
        amountUnblockRequestDTO.setDestinationAccountNo(amountUnblockRequestVO.getTargetAccountNo());
        amountUnblockRequestDTO.setDestinationBVN(amountUnblockRequestVO.getTargetBVN());
        amountUnblockRequestDTO.setDestinationInstitutionCode(amountUnblockRequestVO.getDestinationInstitutionCode());
        amountUnblockRequestDTO.setNarration(amountUnblockRequestVO.getNarration());
        amountUnblockRequestDTO.setReasonCode(amountUnblockRequestVO.getReasonCode());
        amountUnblockRequestDTO.setReferenceCode(amountUnblockRequestVO.getReferenceCode());
        amountUnblockRequestDTO.setSessionId(amountUnblockRequestVO.getSessionId());
        return amountUnblockRequestDTO;
    };

    public Function<AmountUnblockResponseDTO, AmountUnblockResponseVO> mapAmountUnblockResponseVO = amountUnblockResponseDTO -> {
        final val amountUnblockResponseVO = new AmountUnblockResponseVO();
        amountUnblockResponseVO.setTargetAccountName(amountUnblockResponseDTO.getDestinationAccountName());
        amountUnblockResponseVO.setTargetAccountNo(amountUnblockResponseDTO.getDestinationAccountNo());
        amountUnblockResponseVO.setTargetBVN(amountUnblockResponseDTO.getDestinationBVN());
        amountUnblockResponseVO.setDestinationInstitutionCode(amountUnblockResponseDTO.getDestinationInstitutionCode());
        amountUnblockResponseVO.setNarration(amountUnblockResponseDTO.getNarration());
        amountUnblockResponseVO.setReasonCode(amountUnblockResponseDTO.getReasonCode());
        amountUnblockResponseVO.setReferenceCode(amountUnblockResponseDTO.getReferenceCode());
        amountUnblockResponseVO.setSessionId(amountUnblockResponseDTO.getSessionId());
        amountUnblockResponseVO.setResponseCode(amountUnblockResponseDTO.getResponseCode());
        return amountUnblockResponseVO;
    };
}
