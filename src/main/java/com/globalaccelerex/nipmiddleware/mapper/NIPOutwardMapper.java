package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import com.globalaccelerex.nipmiddleware.payload.outward.fundstransfer.FTSingleCreditRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry.NESingleResponse;
import com.globalaccelerex.nipmiddleware.payload.outward.tsq.TsqResponse;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.fundtransfer.FTSingleCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemRequestVO;
import com.globalaccelerex.nipmiddleware.util.SessionIdUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static com.globalaccelerex.nipmiddleware.enums.ChannelCodesEnum.CC_1;

@Slf4j
@Service
public class NIPOutwardMapper {

    @Autowired
    private SessionIdUtil sessionIdUtil;

    public Function<NESingleRequest, NESingleRequestVO> mapNESingleRequestVO = neSingleRequest -> {
        NESingleRequestVO requestVO = new NESingleRequestVO();
        requestVO.setAccountNo(neSingleRequest.getAccountNo());
        requestVO.setChannelCode(String.valueOf(CC_1.getCode()));
        requestVO.setDestinationInstitutionCode(neSingleRequest.getDestinationBankCode());
        requestVO.setSessionId(sessionIdUtil.generateSessionId(neSingleRequest.getOriginatorBankCode()));
        return requestVO;
    };

    public Function<NESingleResponseVO, NESingleResponse> mapNESingleResponseVO = neSingleResponseVO -> {
        final val neSingleResponse = NESingleResponse.builder()
                .accountName(neSingleResponseVO.getAccountName())
                .accountNo(neSingleResponseVO.getAccountNo())
                .bankVerificationNo(neSingleResponseVO.getBvn())
                .kycLevel(neSingleResponseVO.getKycLevel())
                .destinationInstitutionCode(neSingleResponseVO.getDestinationInstitutionCode())
                .nameEnquiryReference(neSingleResponseVO.getSessionId())
                .build();
        neSingleResponse.setResponseCode(neSingleResponseVO.getResponseCode());
        return neSingleResponse;
    };

    public Function<FTSingleCreditRequest, NESingleRequest> mapNESingleRequest = ftSingleCreditRequest ->{
        final val neSingleRequest = NESingleRequest.builder()
                .accountNo(ftSingleCreditRequest.getDestinationAccountNo())
                .destinationBankCode(ftSingleCreditRequest.getDestinationBankCode())
                .originatorBankCode(ftSingleCreditRequest.getOriginatorBankCode())
                .build();
        return neSingleRequest;
    };

    public TsqSingleItemRequestVO buildTsqSingleItemRequestVO(String sessionId , String originatorBankCode){
        return TsqSingleItemRequestVO.builder()
                .channelCode(String.valueOf(CC_1.getCode()))
                .sessionId(sessionId)
                .sourceInstitutionCode(originatorBankCode)
                .build();
    }

    public Function<FTSingleCreditRequest , FTSingleCreditRequestVO> mapFTSingleCreditRequestVO = (FTSingleCreditRequest ftSingleCreditRequest) -> {
        return FTSingleCreditRequestVO.builder()
                .amount(ftSingleCreditRequest.getAmount().toPlainString())
                .beneficiaryAccountName(ftSingleCreditRequest.getBeneficiaryAccountName())
                .beneficiaryAccountNo(ftSingleCreditRequest.getDestinationAccountNo())
                .beneficiaryBVN(ftSingleCreditRequest.getBeneficiaryBVN())
                .beneficiaryKYCLevel(ftSingleCreditRequest.getBeneficiaryKYCLevel())
                .channelCode(String.valueOf(CC_1.getCode()))
                .destinationInstitutionCode(ftSingleCreditRequest.getDestinationBankCode())
                .narration(StringUtils.substring(ftSingleCreditRequest.getNarration() ,0 ,100))
                .originatorAccountName(ftSingleCreditRequest.getOriginatorAccountName())
                .originatorAccountNo(ftSingleCreditRequest.getOriginatorAccountNo())
                .originatorBVN(ftSingleCreditRequest.getOriginatorBVN())
                .originatorKYCLevel(ftSingleCreditRequest.getOriginatorKYCLevel())
                .paymentReference(ftSingleCreditRequest.getPaymentReference())
                .transactionLocation(ftSingleCreditRequest.getTransactionLocation())

                .build();
    };

    public Function<FTSingleCreditRequest, FundsTransferEntity> mapFundsTransferEntity = ftSingleCreditRequest -> {
        final val fundsTransferEntity = FundsTransferEntity.builder()
                .amount(ftSingleCreditRequest.getAmount())
                .beneficiaryAccountName(ftSingleCreditRequest.getBeneficiaryAccountName())
                .beneficiaryAccountNo(ftSingleCreditRequest.getDestinationAccountNo())
                .beneficiaryBVN(ftSingleCreditRequest.getBeneficiaryBVN())
                .beneficiaryKYCLevel(ftSingleCreditRequest.getBeneficiaryKYCLevel())
                .clientId(ftSingleCreditRequest.getClientId())
                .channelCode(String.valueOf(CC_1.getCode()))
                .destinationInstitutionCode(ftSingleCreditRequest.getDestinationBankCode())
                .nameEnquiryReference(ftSingleCreditRequest.getNameEnquiryReference())
                .narration(ftSingleCreditRequest.getNarration())
                .originatorAccountName(ftSingleCreditRequest.getOriginatorAccountName())
                .originatorAccountNo(ftSingleCreditRequest.getOriginatorAccountNo())
                .originatorBVN(ftSingleCreditRequest.getOriginatorBVN())
                .originatorKYCLevel(ftSingleCreditRequest.getOriginatorKYCLevel())
                .originatorInstitutionCode(ftSingleCreditRequest.getOriginatorBankCode())
                .paymentReference(ftSingleCreditRequest.getPaymentReference())
                .build();
        return fundsTransferEntity;
    };

    public Function<FundsTransferEntity , TsqResponse> mapTsqResponse = fundsTransferEntity -> {
        final val tsqResponse = TsqResponse.builder()
                .amount(fundsTransferEntity.getAmount())
                .beneficiaryAccountName(fundsTransferEntity.getBeneficiaryAccountName())
                .destinationAccountNo(fundsTransferEntity.getBeneficiaryAccountNo())
                .beneficiaryBVN(fundsTransferEntity.getBeneficiaryBVN())
                .beneficiaryKYCLevel(fundsTransferEntity.getBeneficiaryKYCLevel())
                .destinationBankCode(fundsTransferEntity.getDestinationInstitutionCode())
                .narration(fundsTransferEntity.getNarration())
                .nameEnquiryReference(fundsTransferEntity.getNameEnquiryReference())
                .originatorAccountName(fundsTransferEntity.getOriginatorAccountName())
                .originatorAccountNo(fundsTransferEntity.getOriginatorAccountNo())
                .originatorBVN(fundsTransferEntity.getOriginatorBVN())
                .originatorKYCLevel(fundsTransferEntity.getOriginatorKYCLevel())
                .originatorBankCode(fundsTransferEntity.getOriginatorInstitutionCode())
                .paymentReference(fundsTransferEntity.getPaymentReference())
                .transactionLocation(fundsTransferEntity.getTransactionLocation())
                .build();
        tsqResponse.setResponseCode(fundsTransferEntity.getResponseCode());
        tsqResponse.setSessionId(fundsTransferEntity.getSessionId());
        return tsqResponse;
    };

}
