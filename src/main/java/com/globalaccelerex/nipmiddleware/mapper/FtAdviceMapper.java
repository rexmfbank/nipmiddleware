package com.globalaccelerex.nipmiddleware.mapper;


import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectcredit.FTAdviceDirectCreditRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectcredit.FTAdviceDirectCreditResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectdebit.FTAdviceDirectDebitRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectdebit.FTAdviceDirectDebitResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTAdviceDirectCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTAdviceDirectCreditResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTAdviceDirectDebitRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTAdviceDirectDebitResponseVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class FtAdviceMapper {



    public Function<FTAdviceDirectDebitRequestVO, FTAdviceDirectDebitRequestDTO> mapFTAdviceDirectDebitRequestDTO = ftAdviceDirectDebitRequestVO -> {
        final val ftAdviceDirectDebitRequestDTO = new FTAdviceDirectDebitRequestDTO();
        ftAdviceDirectDebitRequestDTO.setAmount(ftAdviceDirectDebitRequestVO.getAmount());
        ftAdviceDirectDebitRequestDTO.setBeneficiaryAccountName(ftAdviceDirectDebitRequestVO.getBeneficiaryAccountName());
        ftAdviceDirectDebitRequestDTO.setBeneficiaryAccountNo(ftAdviceDirectDebitRequestVO.getBeneficiaryAccountNo());
        ftAdviceDirectDebitRequestDTO.setBeneficiaryBVN(ftAdviceDirectDebitRequestVO.getBeneficiaryBVN());
        ftAdviceDirectDebitRequestDTO.setBeneficiaryKYCLevel(ftAdviceDirectDebitRequestVO.getBeneficiaryKYCLevel());
        ftAdviceDirectDebitRequestDTO.setDebitAccountName(ftAdviceDirectDebitRequestVO.getDebitAccountName());
        ftAdviceDirectDebitRequestDTO.setDebitAccountNo(ftAdviceDirectDebitRequestVO.getDebitAccountNo());
        ftAdviceDirectDebitRequestDTO.setDebitBVN(ftAdviceDirectDebitRequestVO.getDebitBVN());
        ftAdviceDirectDebitRequestDTO.setDebitKYCLevel(ftAdviceDirectDebitRequestVO.getDebitKYCLevel());
        ftAdviceDirectDebitRequestDTO.setDestinationInstitutionCode(ftAdviceDirectDebitRequestVO.getDestinationInstitutionCode());
        ftAdviceDirectDebitRequestDTO.setMandateReferenceNo(ftAdviceDirectDebitRequestVO.getMandateReferenceNo());
        ftAdviceDirectDebitRequestDTO.setNameEnquiryRef(ftAdviceDirectDebitRequestVO.getNameEnquiryRef());
        ftAdviceDirectDebitRequestDTO.setNarration(ftAdviceDirectDebitRequestVO.getNarration());
        ftAdviceDirectDebitRequestDTO.setPaymentReference(ftAdviceDirectDebitRequestVO.getPaymentReference());
        ftAdviceDirectDebitRequestDTO.setSessionId(ftAdviceDirectDebitRequestVO.getSessionId());
        ftAdviceDirectDebitRequestDTO.setTransactionFee(ftAdviceDirectDebitRequestVO.getTransactionFee());
        ftAdviceDirectDebitRequestDTO.setTransactionLocation(ftAdviceDirectDebitRequestVO.getTransactionLocation());
        return ftAdviceDirectDebitRequestDTO;
    };



    public Function<FTAdviceDirectDebitResponseDTO, FTAdviceDirectDebitResponseVO> mapFTAdviceDirectDebitResponseVO = ftAdviceDirectDebitResponseDTO -> {
        final val ftAdviceDirectDebitResponseVO = new FTAdviceDirectDebitResponseVO();
        ftAdviceDirectDebitResponseVO.setAmount(ftAdviceDirectDebitResponseDTO.getAmount());
        ftAdviceDirectDebitResponseVO.setBeneficiaryAccountName(ftAdviceDirectDebitResponseDTO.getBeneficiaryAccountName());
        ftAdviceDirectDebitResponseVO.setBeneficiaryAccountNo(ftAdviceDirectDebitResponseDTO.getBeneficiaryAccountNo());
        ftAdviceDirectDebitResponseVO.setBeneficiaryBVN(ftAdviceDirectDebitResponseDTO.getBeneficiaryBVN());
        ftAdviceDirectDebitResponseVO.setBeneficiaryKYCLevel(ftAdviceDirectDebitResponseDTO.getBeneficiaryKYCLevel());
        ftAdviceDirectDebitResponseVO.setDebitAccountName(ftAdviceDirectDebitResponseDTO.getDebitAccountName());
        ftAdviceDirectDebitResponseVO.setDebitAccountNo(ftAdviceDirectDebitResponseDTO.getDebitAccountNo());
        ftAdviceDirectDebitResponseVO.setDebitBVN(ftAdviceDirectDebitResponseDTO.getDebitBVN());
        ftAdviceDirectDebitResponseVO.setDebitKYCLevel(ftAdviceDirectDebitResponseDTO.getDebitKYCLevel());
        ftAdviceDirectDebitResponseVO.setDestinationInstitutionCode(ftAdviceDirectDebitResponseDTO.getDestinationInstitutionCode());
        ftAdviceDirectDebitResponseVO.setMandateReferenceNo(ftAdviceDirectDebitResponseDTO.getMandateReferenceNo());
        ftAdviceDirectDebitResponseVO.setNameEnquiryRef(ftAdviceDirectDebitResponseDTO.getNameEnquiryRef());
        ftAdviceDirectDebitResponseVO.setNarration(ftAdviceDirectDebitResponseDTO.getNarration());
        ftAdviceDirectDebitResponseVO.setPaymentReference(ftAdviceDirectDebitResponseDTO.getPaymentReference());
        ftAdviceDirectDebitResponseVO.setSessionId(ftAdviceDirectDebitResponseDTO.getSessionId());
        ftAdviceDirectDebitResponseVO.setTransactionFee(ftAdviceDirectDebitResponseDTO.getTransactionFee());
        ftAdviceDirectDebitResponseVO.setTransactionLocation(ftAdviceDirectDebitResponseDTO.getTransactionLocation());
        ftAdviceDirectDebitResponseVO.setResponseCode(ftAdviceDirectDebitResponseDTO.getResponseCode());
        return ftAdviceDirectDebitResponseVO;
    };

    public Function<FTAdviceDirectCreditRequestVO, FTAdviceDirectCreditRequestDTO> mapFTAdviceDirectCreditRequestDTO = ftAdviceDirectCreditRequestVO -> {
        final val ftAdviceDirectCreditRequestDTO = new FTAdviceDirectCreditRequestDTO();
        ftAdviceDirectCreditRequestDTO.setAmount(ftAdviceDirectCreditRequestVO.getAmount());
        ftAdviceDirectCreditRequestDTO.setBeneficiaryAccountName(ftAdviceDirectCreditRequestVO.getBeneficiaryAccountName());
        ftAdviceDirectCreditRequestDTO.setBeneficiaryAccountNo(ftAdviceDirectCreditRequestVO.getBeneficiaryAccountNo());
        ftAdviceDirectCreditRequestDTO.setBeneficiaryBVN(ftAdviceDirectCreditRequestVO.getBeneficiaryBVN());
        ftAdviceDirectCreditRequestDTO.setBeneficiaryKYCLevel(ftAdviceDirectCreditRequestVO.getBeneficiaryKYCLevel());
        ftAdviceDirectCreditRequestDTO.setOriginatorAccountName(ftAdviceDirectCreditRequestVO.getOriginatorAccountName());
        ftAdviceDirectCreditRequestDTO.setOriginatorAccountNo(ftAdviceDirectCreditRequestVO.getOriginatorAccountNo());
        ftAdviceDirectCreditRequestDTO.setOriginatorBVN(ftAdviceDirectCreditRequestVO.getOriginatorBVN());
        ftAdviceDirectCreditRequestDTO.setOriginatorKYCLevel(ftAdviceDirectCreditRequestVO.getOriginatorKYCLevel());
        ftAdviceDirectCreditRequestDTO.setDestinationInstitutionCode(ftAdviceDirectCreditRequestVO.getDestinationInstitutionCode());
        ftAdviceDirectCreditRequestDTO.setNameEnquiryRef(ftAdviceDirectCreditRequestVO.getNameEnquiryRef());
        ftAdviceDirectCreditRequestDTO.setNarration(ftAdviceDirectCreditRequestVO.getNarration());
        ftAdviceDirectCreditRequestDTO.setPaymentReference(ftAdviceDirectCreditRequestVO.getPaymentReference());
        ftAdviceDirectCreditRequestDTO.setSessionId(ftAdviceDirectCreditRequestVO.getSessionId());
        ftAdviceDirectCreditRequestDTO.setTransactionLocation(ftAdviceDirectCreditRequestVO.getTransactionLocation());
        return ftAdviceDirectCreditRequestDTO;
    };

    public Function<FTAdviceDirectCreditResponseDTO, FTAdviceDirectCreditResponseVO> mapFTAdviceDirectCreditResponseVO = ftAdviceDirectCreditResponseDTO -> {
        final val ftAdviceDirectCreditResponseVO = new FTAdviceDirectCreditResponseVO();
        ftAdviceDirectCreditResponseVO.setAmount(ftAdviceDirectCreditResponseDTO.getAmount());
        ftAdviceDirectCreditResponseVO.setBeneficiaryAccountName(ftAdviceDirectCreditResponseDTO.getBeneficiaryAccountName());
        ftAdviceDirectCreditResponseVO.setBeneficiaryAccountNo(ftAdviceDirectCreditResponseDTO.getBeneficiaryAccountNo());
        ftAdviceDirectCreditResponseVO.setBeneficiaryBVN(ftAdviceDirectCreditResponseDTO.getBeneficiaryBVN());
        ftAdviceDirectCreditResponseVO.setBeneficiaryKYCLevel(ftAdviceDirectCreditResponseDTO.getBeneficiaryKYCLevel());
        ftAdviceDirectCreditResponseVO.setOriginatorAccountName(ftAdviceDirectCreditResponseDTO.getOriginatorAccountName());
        ftAdviceDirectCreditResponseVO.setOriginatorAccountNo(ftAdviceDirectCreditResponseDTO.getOriginatorAccountNo());
        ftAdviceDirectCreditResponseVO.setOriginatorBVN(ftAdviceDirectCreditResponseDTO.getOriginatorBVN());
        ftAdviceDirectCreditResponseVO.setOriginatorKYCLevel(ftAdviceDirectCreditResponseDTO.getOriginatorKYCLevel());
        ftAdviceDirectCreditResponseVO.setDestinationInstitutionCode(ftAdviceDirectCreditResponseDTO.getDestinationInstitutionCode());
        ftAdviceDirectCreditResponseVO.setNameEnquiryRef(ftAdviceDirectCreditResponseDTO.getNameEnquiryRef());
        ftAdviceDirectCreditResponseVO.setNarration(ftAdviceDirectCreditResponseDTO.getNarration());
        ftAdviceDirectCreditResponseVO.setPaymentReference(ftAdviceDirectCreditResponseDTO.getPaymentReference());
        ftAdviceDirectCreditResponseVO.setSessionId(ftAdviceDirectCreditResponseDTO.getSessionId());
        ftAdviceDirectCreditResponseVO.setTransactionLocation(ftAdviceDirectCreditResponseDTO.getTransactionLocation());
        ftAdviceDirectCreditResponseVO.setResponseCode(ftAdviceDirectCreditResponseDTO.getResponseCode());
        return ftAdviceDirectCreditResponseVO;
    };
}
