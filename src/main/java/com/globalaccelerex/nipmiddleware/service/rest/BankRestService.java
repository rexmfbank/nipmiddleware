package com.globalaccelerex.nipmiddleware.service.rest;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.http.HTTPRestTemplate;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountunblock.AmountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountunblock.AmountUnblockResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.balanceenquiry.BalanceEnquiryResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.*;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.api.BankAPI.*;

@Service
public class BankRestService {

    private final HTTPRestTemplate httpRestTemplate;

    private final NipConfig nipConfig;

    @Autowired
    public BankRestService(HTTPRestTemplate httpRestTemplate, NipConfig nipConfig) {
        this.httpRestTemplate = httpRestTemplate;
        this.nipConfig = nipConfig;
    }

    public NESingleResponseVO doNameEnquiry(NESingleRequestVO neSingleRequestVO){
        final val nameEnquiryUrl = nipConfig.getBankUrl() + MOCK_CBA_API + NAME_ENQUIRY_API;
        final val restTemplate = httpRestTemplate.getClient();
        return restTemplate.postForObject(nameEnquiryUrl, neSingleRequestVO, NESingleResponseVO.class);
    }

    public FinancialInstitutionListResponseVO doFIList(FinancialInstitutionListRequestVO financialInstitutionListRequestVO){
        final val fiListUrl = nipConfig.getBankUrl() + MOCK_CBA_API + FI_LIST_API;
        return httpRestTemplate.getClient().postForObject(fiListUrl, financialInstitutionListRequestVO, FinancialInstitutionListResponseVO.class);
    }

    public FTDirectDebitResponseVO doFTDirectDebit(FTDirectDebitRequestVO ftDirectDebitRequestVO){
        final val ftDirectDebitUrl = nipConfig.getBankUrl() + MOCK_CBA_API + FT_DIRECT_DEBIT_API;
        return httpRestTemplate.getClient().postForObject(ftDirectDebitUrl, ftDirectDebitRequestVO, FTDirectDebitResponseVO.class);
    }

    public FTDirectCreditResponseVO doFTDirectCredit(FTDirectCreditRequestVO ftDirectCreditRequestVO){
        final val ftDirectCreditUrl = nipConfig.getBankUrl() + MOCK_CBA_API + FT_DIRECT_CREDIT_API;
        return httpRestTemplate.getClient().postForObject(ftDirectCreditUrl, ftDirectCreditRequestVO, FTDirectCreditResponseVO.class);
    }

    public TSQuerySingleResponseVO doTsq(TSQuerySingleRequestVO tsQuerySingleRequestVO){
        final val tsqUrl = nipConfig.getBankUrl() + MOCK_CBA_API + TSQ_API;
        return httpRestTemplate.getClient().postForObject(tsqUrl, tsQuerySingleRequestVO, TSQuerySingleResponseVO.class);
    }

    public FTAdviceDirectCreditResponseVO doFTAdviceDirectCredit(FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO){
        final val ftAdviceDirectCreditUrl = nipConfig.getBankUrl() + MOCK_CBA_API + FT_ADVICE_DIRECT_CREDIT_API;
        return httpRestTemplate.getClient().postForObject(ftAdviceDirectCreditUrl, ftAdviceDirectCreditRequestVO, FTAdviceDirectCreditResponseVO.class);
    }

    public FTAdviceDirectDebitResponseVO doFTAdviceDirectDebit(FTAdviceDirectDebitRequestVO ftAdviceDirectDebitRequestVO){
        final val ftAdviceDirectDebitUrl = nipConfig.getBankUrl() + MOCK_CBA_API + FT_ADVICE_DIRECT_DEBIT_API;
        return httpRestTemplate.getClient().postForObject(ftAdviceDirectDebitUrl, ftAdviceDirectDebitRequestVO, FTAdviceDirectDebitResponseVO.class);
    }

    public MandateAdviceResponseVO doMandateAdvice(MandateAdviceRequestVO mandateAdviceRequestVO){
        final val mandateAdviceUrl = nipConfig.getBankUrl() + MOCK_CBA_API + MANDATE_ADVICE_API;
        return httpRestTemplate.getClient().postForObject(mandateAdviceUrl, mandateAdviceRequestVO, MandateAdviceResponseVO.class);
    }

    public AccountBlockResponseVO doAccountBlock(AccountBlockRequestVO accountBlockRequestVO){
        final val accountBlockUrl = nipConfig.getBankUrl() + MOCK_CBA_API + ACCOUNT_BLOCK_API;
        return httpRestTemplate.getClient().postForObject(accountBlockUrl, accountBlockRequestVO, AccountBlockResponseVO.class);
    }

    public AccountUnblockResponseVO doAccountUnblock(AccountUnblockRequestVO accountUnblockRequestVO){
        final val accountUnblockUrl = nipConfig.getBankUrl() + MOCK_CBA_API + ACCOUNT_UNBLOCK_API;
        return httpRestTemplate.getClient().postForObject(accountUnblockUrl, accountUnblockRequestVO, AccountUnblockResponseVO.class);
    }

    public AmountBlockResponseVO doAmountBlock(AmountBlockRequestVO amountBlockRequestVO){
        final val amountBlockUrl = nipConfig.getBankUrl() + MOCK_CBA_API + AMOUNT_BLOCK_API;
        return httpRestTemplate.getClient().postForObject(amountBlockUrl, amountBlockRequestVO, AmountBlockResponseVO.class);
    }

    public AmountUnblockResponseVO doAmountUnblock(AmountUnblockRequestVO amountUnblockRequestVO){
        final val amountUnblockUrl = nipConfig.getBankUrl() + MOCK_CBA_API + AMOUNT_UNBLOCK_API;
        return httpRestTemplate.getClient().postForObject(amountUnblockUrl, amountUnblockRequestVO, AmountUnblockResponseVO.class);
    }

    public BalanceEnquiryResponseVO doBalanceEnquiry(BalanceEnquiryRequestVO balanceEnquiryRequestVO){
        final val balanceEnquiryUrl = nipConfig.getBankUrl() + MOCK_CBA_API + BALANCE_ENQUIRY_API;
        return httpRestTemplate.getClient().postForObject(balanceEnquiryUrl, balanceEnquiryRequestVO, BalanceEnquiryResponseVO.class);
    }
}
