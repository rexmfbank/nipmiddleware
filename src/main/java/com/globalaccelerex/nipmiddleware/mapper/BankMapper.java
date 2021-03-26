package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.payload.bank.balanceenquiry.BalanceEnquiryRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.balanceenquiry.BalanceEnquiryResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.mandateadvice.MandateAdviceRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.mandateadvice.MandateAdviceResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry.NESingleRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry.NESingleResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceResponseVO;
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

    public Function<NESingleResponseDTO , NESingleResponseVO> mapNESingleResponseVO = neSingleResponseDTO -> NESingleResponseVO.builder()
            .accountName(neSingleResponseDTO.getAccountName())
            .accountNo(neSingleResponseDTO.getAccountNo())
            .bvn(neSingleResponseDTO.getBvn())
            .destinationInstitutionCode(neSingleResponseDTO.getDestinationInstitutionCode())
            .kycLevel(neSingleResponseDTO.getKycLevel())
            .responseCode(neSingleResponseDTO.getResponseCode())
            .sessionId(neSingleResponseDTO.getSessionId())
            .build();

    public Function<MandateAdviceRequestVO, MandateAdviceRequestDTO> mapMandateAdviceRequestDTO = mandateAdviceRequestVO -> {
        final val mandateAdviceRequestDTO = new MandateAdviceRequestDTO();
        mandateAdviceRequestDTO.setAmount(mandateAdviceRequestVO.getAmount());
        mandateAdviceRequestDTO.setBeneficiaryAccountName(mandateAdviceRequestVO.getBeneficiaryAccountName());
        mandateAdviceRequestDTO.setBeneficiaryAccountNo(mandateAdviceRequestVO.getBeneficiaryAccountNo());
        mandateAdviceRequestDTO.setBeneficiaryBVN(mandateAdviceRequestVO.getBeneficiaryBVN());
        mandateAdviceRequestDTO.setBeneficiaryKYCLevel(mandateAdviceRequestVO.getBeneficiaryKYCLevel());
        mandateAdviceRequestDTO.setDebitAccountName(mandateAdviceRequestVO.getDebitAccountName());
        mandateAdviceRequestDTO.setDebitAccountNo(mandateAdviceRequestVO.getDebitAccountNo());
        mandateAdviceRequestDTO.setDebitBVN(mandateAdviceRequestVO.getDebitBVN());
        mandateAdviceRequestDTO.setDebitKYCLevel(mandateAdviceRequestVO.getDebitKYCLevel());
        mandateAdviceRequestDTO.setDestinationCode(mandateAdviceRequestVO.getDestinationCode());
        mandateAdviceRequestDTO.setMandateReferenceNo(mandateAdviceRequestVO.getMandateReferenceNo());
        mandateAdviceRequestDTO.setSessionId(mandateAdviceRequestVO.getSessionId());
        return mandateAdviceRequestDTO;
    };

    public Function<MandateAdviceResponseDTO, MandateAdviceResponseVO> mapMandateAdviceResponseVO = mandateAdviceResponseDTO -> {
        final val mandateAdviceResponseVO = new MandateAdviceResponseVO();
        mandateAdviceResponseVO.setAmount(mandateAdviceResponseDTO.getAmount());
        mandateAdviceResponseVO.setBeneficiaryAccountName(mandateAdviceResponseDTO.getBeneficiaryAccountName());
        mandateAdviceResponseVO.setBeneficiaryAccountNo(mandateAdviceResponseDTO.getBeneficiaryAccountNo());
        mandateAdviceResponseVO.setBeneficiaryBVN(mandateAdviceResponseDTO.getBeneficiaryBVN());
        mandateAdviceResponseVO.setBeneficiaryKYCLevel(mandateAdviceResponseDTO.getBeneficiaryKYCLevel());
        mandateAdviceResponseVO.setDebitAccountName(mandateAdviceResponseDTO.getDebitAccountName());
        mandateAdviceResponseVO.setDebitAccountNo(mandateAdviceResponseDTO.getDebitAccountNo());
        mandateAdviceResponseVO.setDebitBVN(mandateAdviceResponseDTO.getDebitBVN());
        mandateAdviceResponseVO.setDebitKYCLevel(mandateAdviceResponseDTO.getDebitKYCLevel());
        mandateAdviceResponseVO.setDestinationCode(mandateAdviceResponseDTO.getDestinationCode());
        mandateAdviceResponseVO.setMandateReferenceNo(mandateAdviceResponseDTO.getMandateReferenceNo());
        mandateAdviceResponseVO.setSessionId(mandateAdviceResponseDTO.getSessionId());
        return mandateAdviceResponseVO;
    };

    public Function<BalanceEnquiryRequestVO, BalanceEnquiryRequestDTO> mapBalanceEnquiryRequestDTO = balanceEnquiryRequestVO -> {
        final val balanceEnquiryRequestDTO = new BalanceEnquiryRequestDTO();
        balanceEnquiryRequestDTO.setAccountName(balanceEnquiryRequestVO.getTargetAccountName());
        balanceEnquiryRequestDTO.setAccountNo(balanceEnquiryRequestVO.getTargetAccountNo());
        balanceEnquiryRequestDTO.setAuthorizationCode(balanceEnquiryRequestVO.getAuthorizationCode());
        balanceEnquiryRequestDTO.setBvn(balanceEnquiryRequestVO.getTargetBankVerificationNo());
        balanceEnquiryRequestDTO.setDestinationInstitutionCode(balanceEnquiryRequestVO.getDestinationInstitutionCode());
        balanceEnquiryRequestDTO.setSessionId(balanceEnquiryRequestVO.getSessionID());
        return balanceEnquiryRequestDTO;
    };

    public Function<BalanceEnquiryResponseDTO, BalanceEnquiryResponseVO> mapBalanceEnquiryResponseVO = balanceEnquiryResponseDTO -> {
        final val balanceEnquiryResponseVO = new BalanceEnquiryResponseVO();
        balanceEnquiryResponseVO.setTargetAccountName(balanceEnquiryResponseDTO.getAccountName());
        balanceEnquiryResponseVO.setTargetAccountNo(balanceEnquiryResponseDTO.getAccountNo());
        balanceEnquiryResponseVO.setAuthorizationCode(balanceEnquiryResponseDTO.getAuthorizationCode());
        balanceEnquiryResponseVO.setTargetBankVerificationNo(balanceEnquiryResponseDTO.getBvn());
        balanceEnquiryResponseVO.setDestinationInstitutionCode(balanceEnquiryResponseDTO.getDestinationInstitutionCode());
        balanceEnquiryResponseVO.setSessionID(balanceEnquiryResponseDTO.getSessionId());
        balanceEnquiryResponseVO.setAvailableBalance(balanceEnquiryResponseDTO.getAvailableBalance());
        balanceEnquiryResponseVO.setResponseCode(balanceEnquiryResponseDTO.getResponseCode());
        return balanceEnquiryResponseVO;
    };


}
