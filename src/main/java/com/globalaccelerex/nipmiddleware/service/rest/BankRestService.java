package com.globalaccelerex.nipmiddleware.service.rest;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.http.HTTPRestTemplate;
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
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.tsq.TsqSingleItemResponseVO;
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
        final val nameEnquiryUrl = nipConfig.getBankUrl() + CBA_API + NAME_ENQUIRY_API;
        final val restTemplate = httpRestTemplate.getClient();
        return restTemplate.postForObject(nameEnquiryUrl, neSingleRequestVO, NESingleResponseVO.class);
    }

    public FinancialInstitutionListResponseVO doFIList(FinancialInstitutionListRequestVO financialInstitutionListRequestVO){
        final val fiListUrl = nipConfig.getBankUrl() + CBA_API + FI_LIST_API;
        return httpRestTemplate.getClient().postForObject(fiListUrl, financialInstitutionListRequestVO, FinancialInstitutionListResponseVO.class);
    }

    public FTDirectDebitResponseVO doFTDirectDebit(FTDirectDebitRequestVO ftDirectDebitRequestVO){
        final val ftDirectDebitUrl = nipConfig.getBankUrl() + CBA_API + FT_DIRECT_DEBIT_API;
        return httpRestTemplate.getClient().postForObject(ftDirectDebitUrl, ftDirectDebitRequestVO, FTDirectDebitResponseVO.class);
    }

    public FTDirectCreditResponseVO doFTDirectCredit(FTDirectCreditRequestVO ftDirectCreditRequestVO){
        final val ftDirectCreditUrl = nipConfig.getBankUrl() + CBA_API + FT_DIRECT_CREDIT_API;
        return httpRestTemplate.getClient().postForObject(ftDirectCreditUrl, ftDirectCreditRequestVO, FTDirectCreditResponseVO.class);
    }

    public TSQuerySingleResponseVO doTsq(TSQuerySingleRequestVO tsQuerySingleRequestVO){
        final val tsqUrl = nipConfig.getBankUrl() + CBA_API + TSQ_API;
        return httpRestTemplate.getClient().postForObject(tsqUrl, tsQuerySingleRequestVO, TSQuerySingleResponseVO.class);
    }

    public FTAdviceDirectCreditResponseVO doFTAdviceDirectCredit(FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO){
        final val ftAdviceDirectCreditUrl = nipConfig.getBankUrl() + CBA_API + FT_ADVICE_DIRECT_CREDIT_API;
        return httpRestTemplate.getClient().postForObject(ftAdviceDirectCreditUrl, ftAdviceDirectCreditRequestVO, FTAdviceDirectCreditResponseVO.class);
    }

    public FTAdviceDirectDebitResponseVO doFTAdviceDirectDebit(FTAdviceDirectDebitRequestVO ftAdviceDirectDebitRequestVO){
        final val ftAdviceDirectDebitUrl = nipConfig.getBankUrl() + CBA_API + FT_ADVICE_DIRECT_DEBIT_API;
        return httpRestTemplate.getClient().postForObject(ftAdviceDirectDebitUrl, ftAdviceDirectDebitRequestVO, FTAdviceDirectDebitResponseVO.class);
    }

    public MandateAdviceResponseVO doMandateAdvice(MandateAdviceRequestVO mandateAdviceRequestVO){
        final val mandateAdviceUrl = nipConfig.getBankUrl() + CBA_API + MANDATE_ADVICE_API;
        return httpRestTemplate.getClient().postForObject(mandateAdviceUrl, mandateAdviceRequestVO, MandateAdviceResponseVO.class);
    }

    public AccountBlockResponseVO doAccountBlock(AccountBlockRequestVO accountBlockRequestVO){
        final val accountBlockUrl = nipConfig.getBankUrl() + CBA_API + ACCOUNT_BLOCK_API;
        return httpRestTemplate.getClient().postForObject(accountBlockUrl, accountBlockRequestVO, AccountBlockResponseVO.class);
    }

    public AccountUnblockResponseVO doAccountUnblock(AccountUnblockRequestVO accountUnblockRequestVO){
        final val accountUnblockUrl = nipConfig.getBankUrl() + CBA_API + ACCOUNT_UNBLOCK_API;
        return httpRestTemplate.getClient().postForObject(accountUnblockUrl, accountUnblockRequestVO, AccountUnblockResponseVO.class);
    }

    public AmountBlockResponseVO doAmountBlock(AmountBlockRequestVO amountBlockRequestVO){
        final val amountBlockUrl = nipConfig.getBankUrl() + CBA_API + AMOUNT_BLOCK_API;
        return httpRestTemplate.getClient().postForObject(amountBlockUrl, amountBlockRequestVO, AmountBlockResponseVO.class);
    }
}
