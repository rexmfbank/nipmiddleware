package com.globalaccelerex.nipmiddleware.util;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.mapper.NIPInwardMapper;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitResponseVO;
import com.globalaccelerex.nipmiddleware.service.db.FinancialInstitutionDbService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleResponseVO;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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

}
