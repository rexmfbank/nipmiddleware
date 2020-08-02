package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectcredit.FTDirectCreditRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectcredit.FTDirectCreditResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectdebit.FTDirectDebitRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectdebit.FTDirectDebitResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectCreditResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitResponseVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class FtMapper {

    public Function<FTDirectDebitRequestVO, FTDirectDebitRequestDTO> mapFTDirectDebitRequestDTO = ftDirectDebitRequestVO -> {
        final val ftDirectDebitRequestDTO = new FTDirectDebitRequestDTO();
        ftDirectDebitRequestDTO.setAmount(ftDirectDebitRequestVO.getAmount());
        ftDirectDebitRequestDTO.setBeneficiaryAccountName(ftDirectDebitRequestVO.getBeneficiaryAccountName());
        ftDirectDebitRequestDTO.setBeneficiaryAccountNo(ftDirectDebitRequestVO.getBeneficiaryAccountNo());
        ftDirectDebitRequestDTO.setBeneficiaryBVN(ftDirectDebitRequestVO.getBeneficiaryBVN());
        ftDirectDebitRequestDTO.setBeneficiaryKYCLevel(ftDirectDebitRequestVO.getBeneficiaryKYCLevel());
        ftDirectDebitRequestDTO.setDebitAccountName(ftDirectDebitRequestVO.getDebitAccountName());
        ftDirectDebitRequestDTO.setDebitAccountNo(ftDirectDebitRequestVO.getDebitAccountNo());
        ftDirectDebitRequestDTO.setDebitBVN(ftDirectDebitRequestVO.getDebitBVN());
        ftDirectDebitRequestDTO.setDebitKYCLevel(ftDirectDebitRequestVO.getDebitKYCLevel());
        ftDirectDebitRequestDTO.setDestinationInstitutionCode(ftDirectDebitRequestVO.getDestinationInstitutionCode());
        ftDirectDebitRequestDTO.setMandateReferenceNo(ftDirectDebitRequestVO.getMandateReferenceNo());
        ftDirectDebitRequestDTO.setNameEnquiryRef(ftDirectDebitRequestVO.getNameEnquiryRef());
        ftDirectDebitRequestDTO.setNarration(ftDirectDebitRequestVO.getNarration());
        ftDirectDebitRequestDTO.setPaymentReference(ftDirectDebitRequestVO.getPaymentReference());
        ftDirectDebitRequestDTO.setSessionId(ftDirectDebitRequestVO.getSessionId());
        ftDirectDebitRequestDTO.setTransactionFee(ftDirectDebitRequestVO.getTransactionFee());
        ftDirectDebitRequestDTO.setTransactionLocation(ftDirectDebitRequestVO.getTransactionLocation());
        return ftDirectDebitRequestDTO;
    };

    public Function<FTDirectDebitResponseDTO, FTDirectDebitResponseVO> mapFTDirectDebitResponseVO = ftDirectDebitResponseDTO -> {
        final val ftDirectDebitResponseVO = new FTDirectDebitResponseVO();
        ftDirectDebitResponseVO.setAmount(ftDirectDebitResponseDTO.getAmount());
        ftDirectDebitResponseVO.setBeneficiaryAccountName(ftDirectDebitResponseDTO.getBeneficiaryAccountName());
        ftDirectDebitResponseVO.setBeneficiaryAccountNo(ftDirectDebitResponseDTO.getBeneficiaryAccountNo());
        ftDirectDebitResponseVO.setBeneficiaryBVN(ftDirectDebitResponseDTO.getBeneficiaryBVN());
        ftDirectDebitResponseVO.setBeneficiaryKYCLevel(ftDirectDebitResponseDTO.getBeneficiaryKYCLevel());
        ftDirectDebitResponseVO.setDebitAccountName(ftDirectDebitResponseDTO.getDebitAccountName());
        ftDirectDebitResponseVO.setDebitAccountNo(ftDirectDebitResponseDTO.getDebitAccountNo());
        ftDirectDebitResponseVO.setDebitBVN(ftDirectDebitResponseDTO.getDebitBVN());
        ftDirectDebitResponseVO.setDebitKYCLevel(ftDirectDebitResponseDTO.getDebitKYCLevel());
        ftDirectDebitResponseVO.setDestinationInstitutionCode(ftDirectDebitResponseDTO.getDestinationInstitutionCode());
        ftDirectDebitResponseVO.setMandateReferenceNo(ftDirectDebitResponseDTO.getMandateReferenceNo());
        ftDirectDebitResponseVO.setNameEnquiryRef(ftDirectDebitResponseDTO.getNameEnquiryRef());
        ftDirectDebitResponseVO.setNarration(ftDirectDebitResponseDTO.getNarration());
        ftDirectDebitResponseVO.setPaymentReference(ftDirectDebitResponseDTO.getPaymentReference());
        ftDirectDebitResponseVO.setSessionId(ftDirectDebitResponseDTO.getSessionId());
        ftDirectDebitResponseVO.setTransactionFee(ftDirectDebitResponseDTO.getTransactionFee());
        ftDirectDebitResponseVO.setTransactionLocation(ftDirectDebitResponseDTO.getTransactionLocation());
        ftDirectDebitResponseVO.setResponseCode(ftDirectDebitResponseDTO.getResponseCode());
        return ftDirectDebitResponseVO;
    };


    public Function<FTDirectCreditRequestVO, FTDirectCreditRequestDTO> mapFTDirectCreditRequestDTO = ftDirectCreditRequestVO -> {
        final val ftDirectCreditRequestDTO = new FTDirectCreditRequestDTO();
        ftDirectCreditRequestDTO.setAmount(ftDirectCreditRequestVO.getAmount());
        ftDirectCreditRequestDTO.setBeneficiaryAccountName(ftDirectCreditRequestVO.getBeneficiaryAccountName());
        ftDirectCreditRequestDTO.setBeneficiaryAccountNo(ftDirectCreditRequestVO.getBeneficiaryAccountNo());
        ftDirectCreditRequestDTO.setBeneficiaryBVN(ftDirectCreditRequestVO.getBeneficiaryBVN());
        ftDirectCreditRequestDTO.setBeneficiaryKYCLevel(ftDirectCreditRequestVO.getBeneficiaryKYCLevel());
        ftDirectCreditRequestDTO.setOriginatorAccountName(ftDirectCreditRequestVO.getOriginatorAccountName());
        ftDirectCreditRequestDTO.setOriginatorAccountNo(ftDirectCreditRequestVO.getOriginatorAccountNo());
        ftDirectCreditRequestDTO.setOriginatorBVN(ftDirectCreditRequestVO.getOriginatorBVN());
        ftDirectCreditRequestDTO.setOriginatorKYCLevel(ftDirectCreditRequestVO.getOriginatorKYCLevel());
        ftDirectCreditRequestDTO.setDestinationInstitutionCode(ftDirectCreditRequestVO.getDestinationInstitutionCode());
        ftDirectCreditRequestDTO.setNameEnquiryRef(ftDirectCreditRequestVO.getNameEnquiryRef());
        ftDirectCreditRequestDTO.setNarration(ftDirectCreditRequestVO.getNarration());
        ftDirectCreditRequestDTO.setPaymentReference(ftDirectCreditRequestVO.getPaymentReference());
        ftDirectCreditRequestDTO.setSessionId(ftDirectCreditRequestVO.getSessionId());
        ftDirectCreditRequestDTO.setTransactionLocation(ftDirectCreditRequestVO.getTransactionLocation());
        return ftDirectCreditRequestDTO;
    };

    public Function<FTDirectCreditResponseDTO, FTDirectCreditResponseVO> mapFTDirectCreditResponseVO = ftDirectCreditResponseDTO -> {
        final val ftDirectCreditResponseVO = new FTDirectCreditResponseVO();
        ftDirectCreditResponseVO.setAmount(ftDirectCreditResponseDTO.getAmount());
        ftDirectCreditResponseVO.setBeneficiaryAccountName(ftDirectCreditResponseDTO.getBeneficiaryAccountName());
        ftDirectCreditResponseVO.setBeneficiaryAccountNo(ftDirectCreditResponseDTO.getBeneficiaryAccountNo());
        ftDirectCreditResponseVO.setBeneficiaryBVN(ftDirectCreditResponseDTO.getBeneficiaryBVN());
        ftDirectCreditResponseVO.setBeneficiaryKYCLevel(ftDirectCreditResponseDTO.getBeneficiaryKYCLevel());
        ftDirectCreditResponseVO.setOriginatorAccountName(ftDirectCreditResponseDTO.getOriginatorAccountName());
        ftDirectCreditResponseVO.setOriginatorAccountNo(ftDirectCreditResponseDTO.getOriginatorAccountNo());
        ftDirectCreditResponseVO.setOriginatorBVN(ftDirectCreditResponseDTO.getOriginatorBVN());
        ftDirectCreditResponseVO.setOriginatorKYCLevel(ftDirectCreditResponseDTO.getOriginatorKYCLevel());
        ftDirectCreditResponseVO.setDestinationInstitutionCode(ftDirectCreditResponseDTO.getDestinationInstitutionCode());
        ftDirectCreditResponseVO.setNameEnquiryRef(ftDirectCreditResponseDTO.getNameEnquiryRef());
        ftDirectCreditResponseVO.setNarration(ftDirectCreditResponseDTO.getNarration());
        ftDirectCreditResponseVO.setPaymentReference(ftDirectCreditResponseDTO.getPaymentReference());
        ftDirectCreditResponseVO.setSessionId(ftDirectCreditResponseDTO.getSessionId());
        ftDirectCreditResponseVO.setTransactionLocation(ftDirectCreditResponseDTO.getTransactionLocation());
        ftDirectCreditResponseVO.setResponseCode(ftDirectCreditResponseDTO.getResponseCode());
        return ftDirectCreditResponseVO;
    };
}
