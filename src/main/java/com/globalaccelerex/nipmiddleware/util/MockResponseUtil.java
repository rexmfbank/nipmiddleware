package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.mapper.NIPInwardMapper;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.*;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemResponseVO;
import com.globalaccelerex.nipmiddleware.service.db.FinancialInstitutionDbService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleResponseVO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

import static com.globalaccelerex.nipmiddleware.enums.ChannelCodesEnum.CC_1;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;

@Service
public class MockResponseUtil {

    private final NipConfig nipConfig;

    private final SessionIdUtil sessionIdUtil;

    private final NIPInwardMapper nipInwardMapper;

    private final FinancialInstitutionDbService financialInstitutionDbService;

    @Autowired
    public MockResponseUtil(NipConfig nipConfig, SessionIdUtil sessionIdUtil, NIPInwardMapper nipInwardMapper, FinancialInstitutionDbService financialInstitutionDbService) {
        this.nipConfig = nipConfig;
        this.sessionIdUtil = sessionIdUtil;
        this.nipInwardMapper = nipInwardMapper;
        this.financialInstitutionDbService = financialInstitutionDbService;
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

    public FinancialInstitutionListResponseVO buildFIListResponse(FinancialInstitutionListRequestVO financialInstitutionListRequest){
        final val financialInstitutionEntityList =financialInstitutionListRequest.getRecordList().stream()
                .map(nipInwardMapper.mapFIEntity)
                .collect(Collectors.toList());
        financialInstitutionDbService.saveAll(financialInstitutionEntityList);

        return FinancialInstitutionListResponseVO.builder()
                .batchNumber(financialInstitutionListRequest.getHeader().getBatchNumber())
                .channelCode(financialInstitutionListRequest.getHeader().getChannelCode())
                .destinationInstitutionCode(nipConfig.getSenderBankCode())
                .numberOfRecords(String.valueOf(financialInstitutionListRequest.getRecordList().size()))
                .responseCode(NIP_00.getCode())
                .build();
    }

    public FTDirectDebitResponseVO buildFTDirectDebitResponseVO(FTDirectDebitRequestVO ftDirectDebitRequestVO){
        return FTDirectDebitResponseVO.builder()
                .amount(ftDirectDebitRequestVO.getAmount())
                .beneficiaryAccountName(ftDirectDebitRequestVO.getBeneficiaryAccountName())
                .beneficiaryBVN(ftDirectDebitRequestVO.getBeneficiaryBVN())
                .beneficiaryKYCLevel(ftDirectDebitRequestVO.getBeneficiaryKYCLevel())
                .beneficiaryAccountNo(ftDirectDebitRequestVO.getBeneficiaryAccountNo())
                .channelCode(ftDirectDebitRequestVO.getChannelCode())
                .debitAccountName(ftDirectDebitRequestVO.getDebitAccountName())
                .debitAccountNo(ftDirectDebitRequestVO.getDebitAccountNo())
                .debitBVN(ftDirectDebitRequestVO.getDebitBVN())
                .debitKYCLevel(ftDirectDebitRequestVO.getDebitKYCLevel())
                .destinationInstitutionCode(ftDirectDebitRequestVO.getDestinationInstitutionCode())
                .mandateReferenceNo(ftDirectDebitRequestVO.getMandateReferenceNo())
                .nameEnquiryRef(ftDirectDebitRequestVO.getNameEnquiryRef())
                .narration(ftDirectDebitRequestVO.getNarration())
                .paymentReference(ftDirectDebitRequestVO.getPaymentReference())
                .responseCode(NIP_00.getCode())
                .sessionId(sessionIdUtil.generateSessionId())
                .transactionFee(ftDirectDebitRequestVO.getTransactionFee())
                .transactionLocation(ftDirectDebitRequestVO.getTransactionLocation())
                .build();
    }

    public FTDirectCreditResponseVO buildFtDirectCreditResponseVO(FTDirectCreditRequestVO ftDirectCreditRequestVO){
        return FTDirectCreditResponseVO.builder()
                .amount(ftDirectCreditRequestVO.getAmount())
                .beneficiaryAccountName(ftDirectCreditRequestVO.getBeneficiaryAccountName())
                .beneficiaryAccountNo(ftDirectCreditRequestVO.getBeneficiaryAccountNo())
                .beneficiaryBVN(ftDirectCreditRequestVO.getBeneficiaryBVN())
                .beneficiaryKYCLevel(ftDirectCreditRequestVO.getBeneficiaryKYCLevel())
                .channelCode(ftDirectCreditRequestVO.getChannelCode())
                .destinationInstitutionCode(ftDirectCreditRequestVO.getDestinationInstitutionCode())
                .nameEnquiryRef(ftDirectCreditRequestVO.getNameEnquiryRef())
                .narration(ftDirectCreditRequestVO.getNarration())
                .originatorAccountName(ftDirectCreditRequestVO.getOriginatorAccountName())
                .originatorAccountNo(ftDirectCreditRequestVO.getOriginatorAccountNo())
                .originatorBVN(ftDirectCreditRequestVO.getOriginatorBVN())
                .originatorKYCLevel(ftDirectCreditRequestVO.getOriginatorKYCLevel())
                .paymentReference(ftDirectCreditRequestVO.getPaymentReference())
                .responseCode(NIP_00.getCode())
                .sessionId(sessionIdUtil.generateSessionId())
                .transactionLocation(ftDirectCreditRequestVO.getTransactionLocation())
                .build();
    }

    public TsqSingleItemResponseVO buildTsqSingleItemResponseVO(String sessionId , String sourceInstitutionCode){
        final val tsqSingleItemResponseVO = new TsqSingleItemResponseVO();
        tsqSingleItemResponseVO.setChannelCode(String.valueOf(CC_1.getCode()));
        tsqSingleItemResponseVO.setResponseCode(NIP_00.getCode());
        tsqSingleItemResponseVO.setSessionId(sessionId);
        tsqSingleItemResponseVO.setSourceInstitutionCode(sourceInstitutionCode);
        return tsqSingleItemResponseVO;
    }

    public FTAdviceDirectCreditResponseVO buildFTAdviceDirectCreditResponseVO(FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO){
        return FTAdviceDirectCreditResponseVO.builder()
                .amount(ftAdviceDirectCreditRequestVO.getAmount())
                .beneficiaryAccountName(ftAdviceDirectCreditRequestVO.getBeneficiaryAccountName())
                .beneficiaryAccountNo(ftAdviceDirectCreditRequestVO.getBeneficiaryAccountNo())
                .beneficiaryBVN(ftAdviceDirectCreditRequestVO.getBeneficiaryBVN())
                .beneficiaryKYCLevel(ftAdviceDirectCreditRequestVO.getBeneficiaryKYCLevel())
                .channelCode(ftAdviceDirectCreditRequestVO.getChannelCode())
                .destinationInstitutionCode(ftAdviceDirectCreditRequestVO.getDestinationInstitutionCode())
                .nameEnquiryRef(ftAdviceDirectCreditRequestVO.getNameEnquiryRef())
                .narration(ftAdviceDirectCreditRequestVO.getNarration())
                .originatorAccountName(ftAdviceDirectCreditRequestVO.getOriginatorAccountName())
                .originatorAccountNo(ftAdviceDirectCreditRequestVO.getOriginatorAccountNo())
                .originatorBVN(ftAdviceDirectCreditRequestVO.getOriginatorBVN())
                .originatorKYCLevel(ftAdviceDirectCreditRequestVO.getOriginatorKYCLevel())
                .paymentReference(ftAdviceDirectCreditRequestVO.getPaymentReference())
                .responseCode(NIP_00.getCode())
                .sessionId(ftAdviceDirectCreditRequestVO.getSessionId())
                .transactionLocation(ftAdviceDirectCreditRequestVO.getTransactionLocation())
                .build();
    }

    public Function<FTAdviceDirectCreditRequestVO,FTAdviceDirectCreditResponseVO> mapFTAdviceDirectCreditResponseVO =
            ftAdviceDirectCreditRequestVO -> FTAdviceDirectCreditResponseVO.builder()
                    .amount(ftAdviceDirectCreditRequestVO.getAmount())
                    .beneficiaryAccountName(ftAdviceDirectCreditRequestVO.getBeneficiaryAccountName())
                    .beneficiaryAccountNo(ftAdviceDirectCreditRequestVO.getBeneficiaryAccountNo())
                    .beneficiaryBVN(ftAdviceDirectCreditRequestVO.getBeneficiaryBVN())
                    .beneficiaryKYCLevel(ftAdviceDirectCreditRequestVO.getBeneficiaryKYCLevel())
                    .channelCode(ftAdviceDirectCreditRequestVO.getChannelCode())
                    .destinationInstitutionCode(ftAdviceDirectCreditRequestVO.getDestinationInstitutionCode())
                    .nameEnquiryRef(ftAdviceDirectCreditRequestVO.getNameEnquiryRef())
                    .narration(ftAdviceDirectCreditRequestVO.getNarration())
                    .originatorAccountName(ftAdviceDirectCreditRequestVO.getOriginatorAccountName())
                    .originatorAccountNo(ftAdviceDirectCreditRequestVO.getOriginatorAccountNo())
                    .originatorBVN(ftAdviceDirectCreditRequestVO.getOriginatorBVN())
                    .originatorKYCLevel(ftAdviceDirectCreditRequestVO.getOriginatorKYCLevel())
                    .paymentReference(ftAdviceDirectCreditRequestVO.getPaymentReference())
                    .responseCode(NIP_00.getCode())
                    .sessionId(ftAdviceDirectCreditRequestVO.getSessionId())
                    .transactionLocation(ftAdviceDirectCreditRequestVO.getTransactionLocation())
                    .build();

    public Function<FTAdviceDirectDebitRequestVO,FTAdviceDirectDebitResponseVO> mapFTAdviceDirectDebitResponseVO =
            ftAdviceDirectDebitRequestVO -> FTAdviceDirectDebitResponseVO.builder()
                    .amount(ftAdviceDirectDebitRequestVO.getAmount())
                    .beneficiaryAccountName(ftAdviceDirectDebitRequestVO.getBeneficiaryAccountName())
                    .beneficiaryAccountNo(ftAdviceDirectDebitRequestVO.getBeneficiaryAccountNo())
                    .beneficiaryBVN(ftAdviceDirectDebitRequestVO.getBeneficiaryBVN())
                    .beneficiaryKYCLevel(ftAdviceDirectDebitRequestVO.getBeneficiaryKYCLevel())
                    .channelCode(ftAdviceDirectDebitRequestVO.getChannelCode())
                    .debitAccountName(ftAdviceDirectDebitRequestVO.getDebitAccountName())
                    .debitAccountNo(ftAdviceDirectDebitRequestVO.getDebitAccountNo())
                    .debitBVN(ftAdviceDirectDebitRequestVO.getDebitBVN())
                    .debitKYCLevel(ftAdviceDirectDebitRequestVO.getDebitKYCLevel())
                    .destinationInstitutionCode(ftAdviceDirectDebitRequestVO.getDestinationInstitutionCode())
                    .mandateReferenceNo(ftAdviceDirectDebitRequestVO.getMandateReferenceNo())
                    .nameEnquiryRef(ftAdviceDirectDebitRequestVO.getNameEnquiryRef())
                    .narration(ftAdviceDirectDebitRequestVO.getNarration())
                    .paymentReference(ftAdviceDirectDebitRequestVO.getPaymentReference())
                    .responseCode(NIP_00.getCode())
                    .sessionId(ftAdviceDirectDebitRequestVO.getSessionId())
                    .transactionFee(ftAdviceDirectDebitRequestVO.getTransactionFee())
                    .transactionLocation(ftAdviceDirectDebitRequestVO.getTransactionLocation())
                    .build();

    public Function<MandateAdviceRequestVO, MandateAdviceResponseVO> mapMandateAdviceResponseVO =
            mandateAdviceRequestVO -> MandateAdviceResponseVO.builder()
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
                    .responseCode(NIP_00.getCode())
                    .sessionId(mandateAdviceRequestVO.getSessionId())
                    .build();

    public Function<AccountBlockRequestVO, AccountBlockResponseVO> mapAccountBlockResponseVO =
            accountBlockRequestVO -> AccountBlockResponseVO.builder()
            .channelCode(accountBlockRequestVO.getChannelCode())
            .destinationInstitutionCode(accountBlockRequestVO.getDestinationInstitutionCode())
            .narration(accountBlockRequestVO.getNarration())
            .reasonCode(accountBlockRequestVO.getReasonCode())
            .referenceCode(accountBlockRequestVO.getReferenceCode())
            .responseCode(NIP_00.getCode())
            .sessionId(accountBlockRequestVO.getSessionId())
            .targetAccountName(accountBlockRequestVO.getTargetAccountName())
            .targetAccountNo(accountBlockRequestVO.getTargetAccountNo())
            .targetBVN(accountBlockRequestVO.getTargetBVN())
            .build();

    public Function<AccountUnblockRequestVO , AccountUnblockResponseVO> mapAccountUnblockResponseVO =
            accountUnblockRequestVO -> AccountUnblockResponseVO.builder()
            .channelCode(accountUnblockRequestVO.getChannelCode())
            .destinationInstitutionCode(accountUnblockRequestVO.getDestinationInstitutionCode())
            .narration(accountUnblockRequestVO.getNarration())
            .responseCode(NIP_00.getCode())
            .referenceCode(accountUnblockRequestVO.getReferenceCode())
            .reasonCode(accountUnblockRequestVO.getReasonCode())
            .sessionId(accountUnblockRequestVO.getSessionId())
            .targetAccountName(accountUnblockRequestVO.getTargetAccountName())
            .targetAccountNo(accountUnblockRequestVO.getTargetAccountNo())
            .targetBVN(accountUnblockRequestVO.getTargetBVN())
            .build();

    public Function<AmountBlockRequestVO , AmountBlockResponseVO> mapAmountBlockResponseVO =
            amountBlockRequestVO -> AmountBlockResponseVO.builder()
            .amount(amountBlockRequestVO.getAmount())
            .channelCode(amountBlockRequestVO.getChannelCode())
            .destinationInstitutionCode(amountBlockRequestVO.getDestinationInstitutionCode())
            .narration(amountBlockRequestVO.getNarration())
            .reasonCode(amountBlockRequestVO.getReasonCode())
            .referenceCode(amountBlockRequestVO.getReferenceCode())
            .responseCode(NIP_00.getCode())
            .sessionId(amountBlockRequestVO.getSessionId())
            .targetAccountName(amountBlockRequestVO.getTargetAccountName())
            .targetAccountNo(amountBlockRequestVO.getTargetAccountNo())
            .targetBVN(amountBlockRequestVO.getTargetBVN())
            .build();
}
