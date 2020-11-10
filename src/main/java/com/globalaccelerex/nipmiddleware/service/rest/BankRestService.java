package com.globalaccelerex.nipmiddleware.service.rest;

import com.globalaccelerex.nipmiddleware.institution.ConfigUtil;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.payload.bank.accountblock.AccountBlockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.accountblock.AccountBlockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.accountunblock.AccountUnblockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.accountunblock.AccountUnblockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.amountblock.AmountBlockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.amountblock.AmountBlockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.amountunblock.AmountUnblockRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.amountunblock.AmountUnblockResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.balanceenquiry.BalanceEnquiryRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.balanceenquiry.BalanceEnquiryResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.fiList.FinancialInstitutionListRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.fiList.FinancialInstitutionListResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectcredit.FTAdviceDirectCreditRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectcredit.FTAdviceDirectCreditResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectdebit.FTAdviceDirectDebitRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftadvicedirectdebit.FTAdviceDirectDebitResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectcredit.FTDirectCreditRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectcredit.FTDirectCreditResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectdebit.FTDirectDebitRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.ftdirectdebit.FTDirectDebitResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.mandateadvice.MandateAdviceRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.mandateadvice.MandateAdviceResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry.NESingleRequestDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.nameenquiry.NESingleResponseDTO;
import com.globalaccelerex.nipmiddleware.payload.bank.tsq.TsqSingleResponseDTO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.api.BankAPI.*;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_96;

@Slf4j
@Service
public class BankRestService {

    private  BankHttpClient bankHttpClient;

    private ConfigUtil configUtil;

    public NESingleResponseDTO doNameEnquiry(NESingleRequestDTO neSingleRequestDTO, String originatingInstitutionCode,IMarker marker){
        NESingleResponseDTO neSingleResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Name Enquiry request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", neSingleRequestDTO.toString());
            neSingleResponseDTO =  bankHttpClient.postRequest(baseUrl,NAME_ENQUIRY_API, neSingleRequestDTO, NESingleResponseDTO.class , null,null);
        }catch (Exception exception){
            neSingleResponseDTO = NESingleResponseDTO.builder()
                    .accountNo(neSingleRequestDTO.getAccountNo())
                    .destinationInstitutionCode(neSingleRequestDTO.getDestinationInstitutionCode())
                    .sessionId(neSingleRequestDTO.getSessionId())
                    .responseCode(NIP_96.getCode())
                    .build();
            logException(marker,exception);
        }
        marker.setResponse(" NameEnquiry response :::: " + neSingleResponseDTO.toString());
        return neSingleResponseDTO;
    }

    public FinancialInstitutionListResponseDTO doFIList(FinancialInstitutionListRequestDTO financialInstitutionListRequestDTO, String originatingInstitutionCode, IMarker marker){
        FinancialInstitutionListResponseDTO financialInstitutionListResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending FI List  request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", financialInstitutionListRequestDTO.toString());
            financialInstitutionListResponseDTO = bankHttpClient.postRequest(baseUrl, FI_LIST_API, financialInstitutionListRequestDTO, FinancialInstitutionListResponseDTO.class, null, null);
        }catch (Exception exception){
            financialInstitutionListResponseDTO = FinancialInstitutionListResponseDTO.builder()
            .destinationInstitutionCode(originatingInstitutionCode)
            .responseCode(NIP_96.getCode())
            .batchNumber(financialInstitutionListRequestDTO.getHeader().getBatchNumber())
            .channelCode(financialInstitutionListRequestDTO.getHeader().getChannelCode())
            .numberOfRecords(financialInstitutionListRequestDTO.getHeader().getNumberOfRecords())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("FIList Response ::: " + financialInstitutionListResponseDTO.toString());
        return financialInstitutionListResponseDTO;
    }

    public FTDirectDebitResponseDTO doFTDirectDebit(FTDirectDebitRequestDTO ftDirectDebitRequestDTO, String originatingInstitutionCode, IMarker marker){
        FTDirectDebitResponseDTO ftDirectDebitResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending FT Direct Debit request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", ftDirectDebitRequestDTO.toString());
            ftDirectDebitResponseDTO = bankHttpClient.postRequest(baseUrl, FT_DIRECT_DEBIT_API, ftDirectDebitRequestDTO, FTDirectDebitResponseDTO.class, null, null);
        }catch (Exception exception){
            ftDirectDebitResponseDTO = FTDirectDebitResponseDTO.builder()
            .debitAccountName(ftDirectDebitRequestDTO.getDebitAccountName())
            .debitBVN(ftDirectDebitRequestDTO.getDebitBVN())
            .debitAccountNo(ftDirectDebitRequestDTO.getDebitAccountNo())
            .debitKYCLevel(ftDirectDebitRequestDTO.getDebitKYCLevel())
            .responseCode(NIP_96.getCode())
            .amount(ftDirectDebitRequestDTO.getAmount())
            .beneficiaryAccountName(ftDirectDebitRequestDTO.getBeneficiaryAccountName())
            .beneficiaryAccountNo(ftDirectDebitRequestDTO.getBeneficiaryAccountNo())
            .beneficiaryBVN(ftDirectDebitRequestDTO.getBeneficiaryBVN())
            .beneficiaryKYCLevel(ftDirectDebitRequestDTO.getBeneficiaryKYCLevel())
            .destinationInstitutionCode(ftDirectDebitRequestDTO.getDestinationInstitutionCode())
            .mandateReferenceNo(ftDirectDebitRequestDTO.getMandateReferenceNo())
            .nameEnquiryRef(ftDirectDebitRequestDTO.getNameEnquiryRef())
            .narration(ftDirectDebitRequestDTO.getNarration())
            .paymentReference(ftDirectDebitRequestDTO.getPaymentReference())
            .sessionId(ftDirectDebitRequestDTO.getSessionId())
            .transactionFee(ftDirectDebitRequestDTO.getTransactionFee())
            .transactionLocation(ftDirectDebitRequestDTO.getTransactionLocation())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("FTDirectDebit Response  ::: " + ftDirectDebitResponseDTO.toString());
        return ftDirectDebitResponseDTO;
    }

    public FTDirectCreditResponseDTO doFTDirectCredit(FTDirectCreditRequestDTO ftDirectCreditRequestDTO, String originatingInstitutionCode, IMarker marker){
        FTDirectCreditResponseDTO ftDirectCreditResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending FT Direct Credit request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", ftDirectCreditRequestDTO.toString());
            ftDirectCreditResponseDTO = bankHttpClient.postRequest(baseUrl, FT_DIRECT_CREDIT_API, ftDirectCreditRequestDTO, FTDirectCreditResponseDTO.class, null, null);
        }catch (Exception exception){
            ftDirectCreditResponseDTO = FTDirectCreditResponseDTO.builder()
            .amount(ftDirectCreditRequestDTO.getAmount())
            .beneficiaryAccountName(ftDirectCreditRequestDTO.getBeneficiaryAccountName())
            .beneficiaryAccountNo(ftDirectCreditRequestDTO.getBeneficiaryAccountNo())
            .beneficiaryBVN(ftDirectCreditRequestDTO.getBeneficiaryBVN())
            .beneficiaryKYCLevel(ftDirectCreditRequestDTO.getBeneficiaryKYCLevel())
            .destinationInstitutionCode(ftDirectCreditRequestDTO.getDestinationInstitutionCode())
            .nameEnquiryRef(ftDirectCreditRequestDTO.getNameEnquiryRef())
            .narration(ftDirectCreditRequestDTO.getNarration())
            .originatorAccountName(ftDirectCreditRequestDTO.getOriginatorAccountName())
            .originatorAccountNo(ftDirectCreditRequestDTO.getOriginatorAccountNo())
            .originatorBVN(ftDirectCreditRequestDTO.getOriginatorBVN())
            .originatorKYCLevel(ftDirectCreditRequestDTO.getOriginatorKYCLevel())
            .paymentReference(ftDirectCreditRequestDTO.getPaymentReference())
            .responseCode(NIP_96.getCode())
            .sessionId(ftDirectCreditRequestDTO.getSessionId())
            .transactionLocation(ftDirectCreditRequestDTO.getTransactionLocation())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("FTDirectCredit Response  ::: " + ftDirectCreditResponseDTO.toString());
        return ftDirectCreditResponseDTO;
    }

    public TsqSingleResponseDTO doTsq(String sessionId, String originatingInstitutionCode, IMarker marker){
        TsqSingleResponseDTO tsqSingleResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Tsq to " + bankConfig.getBankName());
            marker.setRequest("request Body ", sessionId);
            val tsqUrl = StringUtils.replace(TSQ_API,"{sessionId}",sessionId);
            tsqSingleResponseDTO =  bankHttpClient.getRequest(baseUrl,tsqUrl,null ,TsqSingleResponseDTO.class ,null, null);
        }catch (Exception exception){
            tsqSingleResponseDTO = new TsqSingleResponseDTO();
            tsqSingleResponseDTO.setResponseCode(NIP_96.getCode());
            logException(marker,exception);
        }
        marker.setResponse("Tsq Response  ::: " + tsqSingleResponseDTO.toString());
        return tsqSingleResponseDTO;
    }

    public FTAdviceDirectCreditResponseDTO doFTAdviceDirectCredit(FTAdviceDirectCreditRequestDTO ftAdviceDirectCreditRequestDTO, String originatingInstitutionCode, IMarker marker){
        FTAdviceDirectCreditResponseDTO ftAdviceDirectCreditResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending FT Advice Direct Credit request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", ftAdviceDirectCreditRequestDTO.toString());
            ftAdviceDirectCreditResponseDTO = bankHttpClient.postRequest(baseUrl, FT_ADVICE_DIRECT_CREDIT_API, ftAdviceDirectCreditRequestDTO, FTAdviceDirectCreditResponseDTO.class, null, null);
        }catch (Exception exception){
            ftAdviceDirectCreditResponseDTO = FTAdviceDirectCreditResponseDTO.builder()
            .amount(ftAdviceDirectCreditRequestDTO.getAmount())
            .beneficiaryAccountName(ftAdviceDirectCreditRequestDTO.getBeneficiaryAccountName())
            .beneficiaryAccountNo(ftAdviceDirectCreditRequestDTO.getBeneficiaryAccountNo())
            .beneficiaryBVN(ftAdviceDirectCreditRequestDTO.getBeneficiaryBVN())
            .beneficiaryKYCLevel(ftAdviceDirectCreditRequestDTO.getBeneficiaryKYCLevel())
            .destinationInstitutionCode(ftAdviceDirectCreditRequestDTO.getDestinationInstitutionCode())
            .nameEnquiryRef(ftAdviceDirectCreditRequestDTO.getNameEnquiryRef())
            .narration(ftAdviceDirectCreditRequestDTO.getNarration())
            .originatorAccountName(ftAdviceDirectCreditRequestDTO.getOriginatorAccountName())
            .originatorAccountNo(ftAdviceDirectCreditRequestDTO.getOriginatorAccountNo())
            .originatorBVN(ftAdviceDirectCreditRequestDTO.getOriginatorBVN())
            .originatorKYCLevel(ftAdviceDirectCreditRequestDTO.getOriginatorKYCLevel())
            .paymentReference(ftAdviceDirectCreditRequestDTO.getPaymentReference())
            .responseCode(NIP_96.getCode())
            .sessionId(ftAdviceDirectCreditRequestDTO.getSessionId())
            .transactionLocation(ftAdviceDirectCreditRequestDTO.getTransactionLocation())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("FTAdvice Direct Credit Response  ::: " + ftAdviceDirectCreditResponseDTO.toString());
        return ftAdviceDirectCreditResponseDTO;
    }

    public FTAdviceDirectDebitResponseDTO doFTAdviceDirectDebit(FTAdviceDirectDebitRequestDTO ftAdviceDirectDebitRequestDTO, String originatingInstitutionCode, IMarker marker){
        FTAdviceDirectDebitResponseDTO ftAdviceDirectDebitResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending FT Advice Direct Debit request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", ftAdviceDirectDebitRequestDTO.toString());
            ftAdviceDirectDebitResponseDTO = bankHttpClient.postRequest(baseUrl, FT_ADVICE_DIRECT_DEBIT_API, ftAdviceDirectDebitRequestDTO, FTAdviceDirectDebitResponseDTO.class, null, null);
        }catch (Exception exception){
            ftAdviceDirectDebitResponseDTO = FTAdviceDirectDebitResponseDTO.builder()
            .amount(ftAdviceDirectDebitRequestDTO.getAmount())
            .beneficiaryAccountName(ftAdviceDirectDebitRequestDTO.getBeneficiaryAccountName())
            .beneficiaryAccountNo(ftAdviceDirectDebitRequestDTO.getBeneficiaryAccountNo())
            .beneficiaryBVN(ftAdviceDirectDebitResponseDTO.getBeneficiaryBVN())
            .beneficiaryKYCLevel(ftAdviceDirectDebitRequestDTO.getBeneficiaryKYCLevel())
            .debitAccountName(ftAdviceDirectDebitRequestDTO.getDebitAccountName())
            .debitAccountNo(ftAdviceDirectDebitRequestDTO.getDebitAccountNo())
            .debitBVN(ftAdviceDirectDebitRequestDTO.getDebitBVN())
            .debitKYCLevel(ftAdviceDirectDebitRequestDTO.getDebitKYCLevel())
            .destinationInstitutionCode(ftAdviceDirectDebitRequestDTO.getDestinationInstitutionCode())
            .mandateReferenceNo(ftAdviceDirectDebitRequestDTO.getMandateReferenceNo())
            .nameEnquiryRef(ftAdviceDirectDebitRequestDTO.getNameEnquiryRef())
            .narration(ftAdviceDirectDebitRequestDTO.getNarration())
            .paymentReference(ftAdviceDirectDebitRequestDTO.getPaymentReference())
            .responseCode(NIP_96.getCode())
            .sessionId(ftAdviceDirectDebitRequestDTO.getSessionId())
            .transactionFee(ftAdviceDirectDebitRequestDTO.getTransactionFee())
            .transactionLocation(ftAdviceDirectDebitRequestDTO.getTransactionLocation())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("FTAdvice Direct Debit Response  ::: " + ftAdviceDirectDebitResponseDTO.toString());
        return ftAdviceDirectDebitResponseDTO;
    }

    public MandateAdviceResponseDTO doMandateAdvice(MandateAdviceRequestDTO mandateAdviceRequestDTO, String originatingInstitutionCode, IMarker marker){
        MandateAdviceResponseDTO mandateAdviceResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Mandate Advice request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", mandateAdviceRequestDTO.toString());
            mandateAdviceResponseDTO = bankHttpClient.postRequest(baseUrl, MANDATE_ADVICE_API, mandateAdviceRequestDTO, MandateAdviceResponseDTO.class, null, null);
        }catch (Exception exception){
            mandateAdviceResponseDTO = MandateAdviceResponseDTO.builder()
            .amount(mandateAdviceRequestDTO.getAmount())
            .beneficiaryAccountName(mandateAdviceRequestDTO.getBeneficiaryAccountName())
            .beneficiaryAccountNo(mandateAdviceRequestDTO.getBeneficiaryAccountNo())
            .beneficiaryBVN(mandateAdviceRequestDTO.getBeneficiaryBVN())
            .beneficiaryKYCLevel(mandateAdviceRequestDTO.getBeneficiaryKYCLevel())
            .debitAccountName(mandateAdviceRequestDTO.getDebitAccountName())
            .debitAccountNo(mandateAdviceRequestDTO.getDebitAccountNo())
            .debitBVN(mandateAdviceRequestDTO.getDebitBVN())
            .debitKYCLevel(mandateAdviceRequestDTO.getDebitKYCLevel())
            .destinationCode(mandateAdviceRequestDTO.getDestinationCode())
            .mandateReferenceNo(mandateAdviceRequestDTO.getMandateReferenceNo())
            .responseCode(NIP_96.getCode())
            .sessionId(mandateAdviceRequestDTO.getSessionId())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("Mandate Advice Response  ::: " + mandateAdviceResponseDTO.toString());
        return mandateAdviceResponseDTO;
    }

    public AccountBlockResponseDTO doAccountBlock(AccountBlockRequestDTO accountBlockRequestDTO, String originatingInstitutionCode, IMarker marker){
        AccountBlockResponseDTO accountBlockResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Account Block request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", accountBlockRequestDTO.toString());
            accountBlockResponseDTO = bankHttpClient.postRequest(baseUrl, ACCOUNT_BLOCK_API, accountBlockRequestDTO, AccountBlockResponseDTO.class, null, null);
        }catch (Exception exception){
            accountBlockResponseDTO = AccountBlockResponseDTO.builder()
            .destinationAccountName(accountBlockRequestDTO.getDestinationAccountName())
            .destinationAccountNo(accountBlockRequestDTO.getDestinationAccountNo())
            .responseCode(NIP_96.getCode())
            .destinationBVN(accountBlockRequestDTO.getDestinationBVN())
            .destinationInstitutionCode(accountBlockRequestDTO.getDestinationInstitutionCode())
            .narration(accountBlockRequestDTO.getNarration())
            .reasonCode(accountBlockRequestDTO.getReasonCode())
            .referenceCode(accountBlockRequestDTO.getReferenceCode())
            .sessionId(accountBlockRequestDTO.getSessionId())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("Account Block Response  ::: " + accountBlockResponseDTO.toString());
        return accountBlockResponseDTO;
    }

    public AccountUnblockResponseDTO doAccountUnblock(AccountUnblockRequestDTO accountUnblockRequestDTO, String originatingInstitutionCode, IMarker marker){
        AccountUnblockResponseDTO accountUnblockResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Account Block request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", accountUnblockRequestDTO.toString());
            accountUnblockResponseDTO = bankHttpClient.postRequest(baseUrl, ACCOUNT_UNBLOCK_API, accountUnblockRequestDTO, AccountUnblockResponseDTO.class, null, null);
        }catch (Exception exception){
            accountUnblockResponseDTO = AccountUnblockResponseDTO.builder()
            .destinationAccountName(accountUnblockRequestDTO.getDestinationAccountName())
            .destinationAccountNo(accountUnblockRequestDTO.getDestinationAccountNo())
            .destinationBVN(accountUnblockRequestDTO.getDestinationBVN())
            .destinationInstitutionCode(accountUnblockRequestDTO.getDestinationInstitutionCode())
            .narration(accountUnblockRequestDTO.getNarration())
            .reasonCode(accountUnblockRequestDTO.getReasonCode())
            .referenceCode(accountUnblockRequestDTO.getReferenceCode())
            .responseCode(NIP_96.getCode())
            .sessionId(accountUnblockRequestDTO.getSessionId())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("Account UnBlock Response  ::: " + accountUnblockResponseDTO.toString());
        return accountUnblockResponseDTO;
    }

    public AmountBlockResponseDTO doAmountBlock(AmountBlockRequestDTO amountBlockRequestDTO, String originatingInstitutionCode, IMarker marker){
        AmountBlockResponseDTO amountBlockResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Account Block request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", amountBlockRequestDTO.toString());
            amountBlockResponseDTO = bankHttpClient.postRequest(baseUrl, AMOUNT_BLOCK_API, amountBlockRequestDTO, AmountBlockResponseDTO.class, null, null);
        }catch (Exception exception){
            amountBlockResponseDTO = AmountBlockResponseDTO.builder()
            .amount(amountBlockRequestDTO.getAmount())
            .destinationAccountName(amountBlockRequestDTO.getDestinationAccountName())
            .destinationAccountNo(amountBlockRequestDTO.getDestinationAccountNo())
            .destinationBVN(amountBlockRequestDTO.getDestinationBVN())
            .destinationInstitutionCode(amountBlockRequestDTO.getDestinationInstitutionCode())
            .narration(amountBlockRequestDTO.getNarration())
            .reasonCode(amountBlockRequestDTO.getReasonCode())
            .referenceCode(amountBlockRequestDTO.getReferenceCode())
            .responseCode(NIP_96.getCode())
            .sessionId(amountBlockRequestDTO.getSessionId())
                    .build();
            logException(marker,exception);
        }
        marker.setResponse("Amount Block Response  ::: " + amountBlockResponseDTO.toString());
        return amountBlockResponseDTO;
    }

    public AmountUnblockResponseDTO doAmountUnblock(AmountUnblockRequestDTO amountUnblockRequestDTO, String originatingInstitutionCode, IMarker marker){

        AmountUnblockResponseDTO amountUnblockResponseDTO = null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Account UnBlock request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", amountUnblockRequestDTO.toString());
            amountUnblockResponseDTO = bankHttpClient.postRequest(baseUrl, AMOUNT_UNBLOCK_API, amountUnblockRequestDTO, AmountUnblockResponseDTO.class, null, null);
        }catch (Exception exception){
            amountUnblockResponseDTO = AmountUnblockResponseDTO.builder()
            .amount(amountUnblockRequestDTO.getAmount())
            .destinationAccountName(amountUnblockRequestDTO.getDestinationAccountName())
            .destinationAccountNo(amountUnblockRequestDTO.getDestinationAccountNo())
            .destinationBVN(amountUnblockRequestDTO.getDestinationBVN())
            .destinationInstitutionCode(amountUnblockRequestDTO.getDestinationInstitutionCode())
            .narration(amountUnblockRequestDTO.getNarration())
            .reasonCode(amountUnblockRequestDTO.getReasonCode())
            .referenceCode(amountUnblockRequestDTO.getReferenceCode())
            .responseCode(NIP_96.getCode())
            .sessionId(amountUnblockRequestDTO.getSessionId())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("Amount UnBlock Response  ::: " + amountUnblockResponseDTO.toString());
        return amountUnblockResponseDTO;

    }

    public BalanceEnquiryResponseDTO doBalanceEnquiry(BalanceEnquiryRequestDTO balanceEnquiryRequestDTO, String originatingInstitutionCode, IMarker marker){
        BalanceEnquiryResponseDTO balanceEnquiryResponseDTO= null;
        try{
            final val bankConfig = configUtil.getBankConfig(originatingInstitutionCode);
            final val baseUrl = bankConfig.getBaseUrl();
            marker.info("Sending Balance Enquiry request to " + bankConfig.getBankName());
            marker.setRequest("request Body ", balanceEnquiryRequestDTO.toString());
            balanceEnquiryResponseDTO = bankHttpClient.postRequest(baseUrl, BALANCE_ENQUIRY_API, balanceEnquiryRequestDTO, BalanceEnquiryResponseDTO.class, null, null);
        }catch (Exception exception){
            balanceEnquiryResponseDTO = BalanceEnquiryResponseDTO.builder()
            .responseCode(NIP_96.getCode())
            .accountName(balanceEnquiryRequestDTO.getAccountName())
            .accountNo(balanceEnquiryRequestDTO.getAccountNo())
            .authorizationCode(balanceEnquiryRequestDTO.getAuthorizationCode())
            .bvn(balanceEnquiryRequestDTO.getBvn())
            .destinationInstitutionCode(balanceEnquiryRequestDTO.getDestinationInstitutionCode())
            .sessionId(balanceEnquiryRequestDTO.getSessionId())
            .build();
            logException(marker,exception);
        }
        marker.setResponse("Balance Enquiry Response  ::: " + balanceEnquiryResponseDTO.toString());
        return balanceEnquiryResponseDTO;
    }

    private void logException(IMarker marker , Exception exception){
        if(exception instanceof BankAPIException){
            val bankException =(BankAPIException) exception;
            marker.info(bankException.getErrorResponse().getResponseMessage(), exception);
        }else{
            marker.info(exception.getMessage(), exception);
        }
    }


    @Autowired
    public void setConfigUtil(ConfigUtil configUtil) {
        this.configUtil = configUtil;
    }

    @Autowired
    public void setBankHttpClient(BankHttpClient bankHttpClient) {
        this.bankHttpClient = bankHttpClient;
    }
}
