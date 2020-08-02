package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.payload.bank.accountblock.AccountBlockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.accountblock.AccountBlockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.accountunblock.AccountUnblockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.accountunblock.AccountUnblockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockResponseVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class AccountMapper {

    public Function<AccountBlockRequestVO, AccountBlockRequestDTO> mapAccountBlockRequestDTO = accountBlockRequestVO -> {
        final val accountBlockRequestDTO = new AccountBlockRequestDTO();
        accountBlockRequestDTO.setDestinationAccountName(accountBlockRequestVO.getTargetAccountName());
        accountBlockRequestDTO.setDestinationAccountNo(accountBlockRequestVO.getTargetAccountNo());
        accountBlockRequestDTO.setDestinationBVN(accountBlockRequestVO.getTargetBVN());
        accountBlockRequestDTO.setDestinationInstitutionCode(accountBlockRequestVO.getDestinationInstitutionCode());
        accountBlockRequestDTO.setNarration(accountBlockRequestVO.getNarration());
        accountBlockRequestDTO.setReasonCode(accountBlockRequestVO.getReasonCode());
        accountBlockRequestDTO.setReferenceCode(accountBlockRequestVO.getReferenceCode());
        accountBlockRequestDTO.setSessionId(accountBlockRequestVO.getSessionId());
        return accountBlockRequestDTO;
    };

    public Function<AccountBlockResponseDTO, AccountBlockResponseVO> mapAccountBlockResponseVO = accountBlockResponseDTO -> {
        final val accountBlockResponseVO = new AccountBlockResponseVO();
        accountBlockResponseVO.setTargetAccountName(accountBlockResponseDTO.getDestinationAccountName());
        accountBlockResponseVO.setTargetAccountNo(accountBlockResponseDTO.getDestinationAccountNo());
        accountBlockResponseVO.setTargetBVN(accountBlockResponseDTO.getDestinationBVN());
        accountBlockResponseVO.setDestinationInstitutionCode(accountBlockResponseDTO.getDestinationInstitutionCode());
        accountBlockResponseVO.setNarration(accountBlockResponseDTO.getNarration());
        accountBlockResponseVO.setReasonCode(accountBlockResponseDTO.getReasonCode());
        accountBlockResponseVO.setReferenceCode(accountBlockResponseDTO.getReferenceCode());
        accountBlockResponseVO.setSessionId(accountBlockResponseDTO.getSessionId());
        accountBlockResponseVO.setResponseCode(accountBlockResponseDTO.getResponseCode());
        return accountBlockResponseVO;
    };

    public Function<AccountUnblockRequestVO, AccountUnblockRequestDTO> mapAccountUnblockRequestDTO = accountUnblockRequestVO -> {
        final val accountUnblockRequestDTO = new AccountUnblockRequestDTO();
        accountUnblockRequestDTO.setDestinationAccountName(accountUnblockRequestVO.getTargetAccountName());
        accountUnblockRequestDTO.setDestinationAccountNo(accountUnblockRequestVO.getTargetAccountNo());
        accountUnblockRequestDTO.setDestinationBVN(accountUnblockRequestVO.getTargetBVN());
        accountUnblockRequestDTO.setDestinationInstitutionCode(accountUnblockRequestVO.getDestinationInstitutionCode());
        accountUnblockRequestDTO.setNarration(accountUnblockRequestVO.getNarration());
        accountUnblockRequestDTO.setReasonCode(accountUnblockRequestVO.getReasonCode());
        accountUnblockRequestDTO.setReferenceCode(accountUnblockRequestVO.getReferenceCode());
        accountUnblockRequestDTO.setSessionId(accountUnblockRequestVO.getSessionId());
        return accountUnblockRequestDTO;
    };

    public Function<AccountUnblockResponseDTO, AccountUnblockResponseVO> mapAccountUnblockResponseVO = accountUnblockResponseDTO -> {
        final val accountUnblockResponseVO = new AccountUnblockResponseVO();
        accountUnblockResponseVO.setTargetAccountName(accountUnblockResponseDTO.getDestinationAccountName());
        accountUnblockResponseVO.setTargetAccountNo(accountUnblockResponseDTO.getDestinationAccountNo());
        accountUnblockResponseVO.setTargetBVN(accountUnblockResponseDTO.getDestinationBVN());
        accountUnblockResponseVO.setDestinationInstitutionCode(accountUnblockResponseDTO.getDestinationInstitutionCode());
        accountUnblockResponseVO.setNarration(accountUnblockResponseDTO.getNarration());
        accountUnblockResponseVO.setReasonCode(accountUnblockResponseDTO.getReasonCode());
        accountUnblockResponseVO.setReferenceCode(accountUnblockResponseDTO.getReferenceCode());
        accountUnblockResponseVO.setSessionId(accountUnblockResponseDTO.getSessionId());
        accountUnblockResponseVO.setResponseCode(accountUnblockResponseDTO.getResponseCode());
        return accountUnblockResponseVO;
    };
}
