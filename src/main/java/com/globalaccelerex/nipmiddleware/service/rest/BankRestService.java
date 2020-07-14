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
import org.apache.commons.lang3.StringUtils;
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

    public NESingleResponseVO doNameEnquiry(NESingleRequestVO neSingleRequestVO, String originatingInstitutionCode){
        final val nameEnquiryUrl =  new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(NAME_ENQUIRY_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        final val restTemplate = httpRestTemplate.getClient();
        return restTemplate.postForObject(nameEnquiryUrl, neSingleRequestVO, NESingleResponseVO.class);
    }

    public FinancialInstitutionListResponseVO doFIList(FinancialInstitutionListRequestVO financialInstitutionListRequestVO, String originatingInstitutionCode){
        final val fiListUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(FI_LIST_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(fiListUrl, financialInstitutionListRequestVO, FinancialInstitutionListResponseVO.class);
    }

    public FTDirectDebitResponseVO doFTDirectDebit(FTDirectDebitRequestVO ftDirectDebitRequestVO, String originatingInstitutionCode){
        final val ftDirectDebitUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(FT_DIRECT_DEBIT_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(ftDirectDebitUrl, ftDirectDebitRequestVO, FTDirectDebitResponseVO.class);
    }

    public FTDirectCreditResponseVO doFTDirectCredit(FTDirectCreditRequestVO ftDirectCreditRequestVO, String originatingInstitutionCode){
        final val ftDirectCreditUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(FT_DIRECT_CREDIT_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(ftDirectCreditUrl, ftDirectCreditRequestVO, FTDirectCreditResponseVO.class);
    }

    public TSQuerySingleResponseVO doTsq(TSQuerySingleRequestVO tsQuerySingleRequestVO, String originatingInstitutionCode){
        final val tsqUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(TSQ_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(tsqUrl, tsQuerySingleRequestVO, TSQuerySingleResponseVO.class);
    }

    public FTAdviceDirectCreditResponseVO doFTAdviceDirectCredit(FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO, String originatingInstitutionCode){

        final val ftAdviceDirectCreditUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(FT_ADVICE_DIRECT_CREDIT_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(ftAdviceDirectCreditUrl, ftAdviceDirectCreditRequestVO, FTAdviceDirectCreditResponseVO.class);
    }

    public FTAdviceDirectDebitResponseVO doFTAdviceDirectDebit(FTAdviceDirectDebitRequestVO ftAdviceDirectDebitRequestVO, String originatingInstitutionCode){
        final val ftAdviceDirectDebitUrl =new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(FT_ADVICE_DIRECT_DEBIT_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(ftAdviceDirectDebitUrl, ftAdviceDirectDebitRequestVO, FTAdviceDirectDebitResponseVO.class);
    }

    public MandateAdviceResponseVO doMandateAdvice(MandateAdviceRequestVO mandateAdviceRequestVO, String originatingInstitutionCode){
        final val mandateAdviceUrl =new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(MANDATE_ADVICE_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(mandateAdviceUrl, mandateAdviceRequestVO, MandateAdviceResponseVO.class);
    }

    public AccountBlockResponseVO doAccountBlock(AccountBlockRequestVO accountBlockRequestVO, String originatingInstitutionCode){
        final val accountBlockUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(ACCOUNT_BLOCK_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(accountBlockUrl, accountBlockRequestVO, AccountBlockResponseVO.class);
    }

    public AccountUnblockResponseVO doAccountUnblock(AccountUnblockRequestVO accountUnblockRequestVO, String originatingInstitutionCode){
        final val accountUnblockUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(ACCOUNT_UNBLOCK_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(accountUnblockUrl, accountUnblockRequestVO, AccountUnblockResponseVO.class);
    }

    public AmountBlockResponseVO doAmountBlock(AmountBlockRequestVO amountBlockRequestVO, String originatingInstitutionCode){

        final val amountBlockUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(AMOUNT_BLOCK_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(amountBlockUrl, amountBlockRequestVO, AmountBlockResponseVO.class);
    }

    public AmountUnblockResponseVO doAmountUnblock(AmountUnblockRequestVO amountUnblockRequestVO, String originatingInstitutionCode){

        final val amountUnblockUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(AMOUNT_UNBLOCK_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(amountUnblockUrl, amountUnblockRequestVO, AmountUnblockResponseVO.class);
    }

    public BalanceEnquiryResponseVO doBalanceEnquiry(BalanceEnquiryRequestVO balanceEnquiryRequestVO, String originatingInstitutionCode){

        final val balanceEnquiryUrl = new StringBuilder().append(nipConfig.getBankUrl())
                .append(MOCK_CBA_API)
                .append(StringUtils.replace(BALANCE_ENQUIRY_API,"{originatingInstitutionCode}",originatingInstitutionCode))
                .toString();
        return httpRestTemplate.getClient().postForObject(balanceEnquiryUrl, balanceEnquiryRequestVO, BalanceEnquiryResponseVO.class);
    }
}
