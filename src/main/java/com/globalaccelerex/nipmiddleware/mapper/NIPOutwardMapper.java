package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.nameenquiry.NESingleResponse;
import com.globalaccelerex.nipmiddleware.payload.client.outward.tsq.TsqResponse;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.fundtransfer.FTSingleCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemRequestVO;
import com.globalaccelerex.nipmiddleware.util.SessionIdUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Function<FTSingleCreditRequest, NESingleRequest> mapNESingleRequest = ftSingleCreditRequest ->{
        final val neSingleRequest = NESingleRequest.builder()
                .accountNo(ftSingleCreditRequest.getBeneficiaryAccountNo())
                .destinationInstitutionCode(ftSingleCreditRequest.getDestinationInstitutionCode())
                .build();
        neSingleRequest.setRequestId(RandomStringUtils.randomNumeric(6));
        return neSingleRequest;
    };

    public TsqSingleItemRequestVO buildTsqSingleItemRequestVO(String sessionId){
        return TsqSingleItemRequestVO.builder()
                .channelCode(String.valueOf(CC_1.getCode()))
                .sessionId(sessionId)
                .sourceInstitutionCode(nipConfig.getSenderBankCode())
                .build();
    }

    public Function<FTSingleCreditRequest , FTSingleCreditRequestVO> mapFTSingleCreditRequestVO = (FTSingleCreditRequest ftSingleCreditRequest) -> {
        return FTSingleCreditRequestVO.builder()
                .amount(ftSingleCreditRequest.getAmount())
                .beneficiaryAccountName(ftSingleCreditRequest.getBeneficiaryAccountName())
                .beneficiaryAccountNo(ftSingleCreditRequest.getBeneficiaryAccountNo())
                .beneficiaryBVN(ftSingleCreditRequest.getBeneficiaryBVN())
                .beneficiaryKYCLevel(ftSingleCreditRequest.getBeneficiaryKYCLevel())
                .channelCode(String.valueOf(CC_1.getCode()))
                .destinationInstitutionCode(ftSingleCreditRequest.getDestinationInstitutionCode())
                .narration(StringUtils.substring(ftSingleCreditRequest.getNarration() ,0 ,100))
                //.narration(ftSingleCreditRequest.getNarration())
                //.narration("This is FROM NIBSS | Plc !@#$%^&*(GROUP)_-+={[LIMITED]}|Outward, to this account/><”")
                .originatorAccountName(ftSingleCreditRequest.getOriginatorAccountName())
                .originatorAccountNo(ftSingleCreditRequest.getOriginatorAccountNo())
                .originatorBVN(ftSingleCreditRequest.getOriginatorBVN())
                .originatorKYCLevel(ftSingleCreditRequest.getOriginatorKYCLevel())
                .paymentReference("")
                //.sessionId(sessionIdUtil.generateSessionId())
                .transactionLocation("")
                .build();
    };

    public Function<FTSingleCreditRequest, FundsTransferEntity> mapFundsTransferEntity = ftSingleCreditRequest -> {
        final val fundsTransferEntity = FundsTransferEntity.builder()
                .amount(new BigDecimal(ftSingleCreditRequest.getAmount()))
                .beneficiaryAccountName(ftSingleCreditRequest.getBeneficiaryAccountName())
                .beneficiaryAccountNo(ftSingleCreditRequest.getBeneficiaryAccountNo())
                .beneficiaryBVN(ftSingleCreditRequest.getBeneficiaryBVN())
                .beneficiaryKYCLevel(ftSingleCreditRequest.getBeneficiaryKYCLevel())
                .channelCode(String.valueOf(CC_1.getCode()))
                .destinationInstitutionCode(ftSingleCreditRequest.getDestinationInstitutionCode())
                .nameEnquiryReference(ftSingleCreditRequest.getNameEnquiryReference())
                .narration(ftSingleCreditRequest.getNarration())
                .originatorAccountName(ftSingleCreditRequest.getOriginatorAccountName())
                .originatorAccountNo(ftSingleCreditRequest.getOriginatorAccountNo())
                .originatorBVN(ftSingleCreditRequest.getOriginatorBVN())
                .originatorInstitutionCode(nipConfig.getSenderBankCode())
                .originatorKYCLevel(ftSingleCreditRequest.getOriginatorKYCLevel())
                .build();
        return fundsTransferEntity;
    };

    public Function<FundsTransferEntity , TsqResponse> mapTsqResponse = fundsTransferEntity -> {
        final val tsqResponse = TsqResponse.builder()
                .amount(fundsTransferEntity.getAmount().toPlainString())
                .beneficiaryAccountName(fundsTransferEntity.getBeneficiaryAccountName())
                .beneficiaryAccountNo(fundsTransferEntity.getBeneficiaryAccountNo())
                .beneficiaryBVN(fundsTransferEntity.getBeneficiaryBVN())
                .beneficiaryKYCLevel(fundsTransferEntity.getBeneficiaryKYCLevel())
                .destinationInstitutionCode(fundsTransferEntity.getDestinationInstitutionCode())
                .narration(fundsTransferEntity.getNarration())
                .nameEnquiryReference(fundsTransferEntity.getNameEnquiryReference())
                .originatorAccountName(fundsTransferEntity.getOriginatorAccountName())
                .originatorAccountNo(fundsTransferEntity.getOriginatorAccountNo())
                .originatorBVN(fundsTransferEntity.getOriginatorBVN())
                .originatorKYCLevel(fundsTransferEntity.getOriginatorKYCLevel())
                .originatorInstitutionCode(fundsTransferEntity.getOriginatorInstitutionCode())
                .paymentReference(fundsTransferEntity.getPaymentReference())
                .transactionLocation(fundsTransferEntity.getTransactionLocation())
                .build();
        tsqResponse.setResponseCode(fundsTransferEntity.getResponseCode());
        tsqResponse.setSessionId(fundsTransferEntity.getSessionId());
        return tsqResponse;
    };

}
