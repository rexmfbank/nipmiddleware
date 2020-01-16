package com.globalaccelerex.nipmiddleware.service;

import com.globalaccelerex.nipmiddleware.mapper.NIPInwardMapper;
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
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleResponseVO;
import com.globalaccelerex.nipmiddleware.service.db.FinancialInstitutionDbService;
import com.globalaccelerex.nipmiddleware.service.rest.BankRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NIPInwardService {

    private final NIPInwardMapper nipInwardMapper;

    private final FinancialInstitutionDbService financialInstitutionDbService;

    private final BankRestService bankRestService;

    @Autowired
    public NIPInwardService(NIPInwardMapper nipInwardMapper, FinancialInstitutionDbService financialInstitutionDbService, BankRestService bankRestService) {
        this.nipInwardMapper = nipInwardMapper;
        this.financialInstitutionDbService = financialInstitutionDbService;
        this.bankRestService = bankRestService;
    }

    public NESingleResponseVO handleNameEnquiry(NESingleRequestVO neSingleRequestVO){
        return bankRestService.doNameEnquiry(neSingleRequestVO);
    }

    public FinancialInstitutionListResponseVO handleFIList(FinancialInstitutionListRequestVO financialInstitutionListRequest){
        return bankRestService.doFIList(financialInstitutionListRequest);
    }

    public FTDirectDebitResponseVO handleFT_DirectDebit(FTDirectDebitRequestVO ftDirectDebitRequestVO){
        return bankRestService.doFTDirectDebit(ftDirectDebitRequestVO);
    }

    public FTDirectCreditResponseVO handleFT_DirectCredit(FTDirectCreditRequestVO ftDirectCreditRequestVO){
        return bankRestService.doFTDirectCredit(ftDirectCreditRequestVO);
    }

    public TSQuerySingleResponseVO handleTSQ(TSQuerySingleRequestVO tsQuerySingleRequestVO){
        return bankRestService.doTsq(tsQuerySingleRequestVO);
    }

    public FTAdviceDirectCreditResponseVO handleFTAdviceDirectCredit(FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO){
        return bankRestService.doFTAdviceDirectCredit(ftAdviceDirectCreditRequestVO);
    }

    public FTAdviceDirectDebitResponseVO handleFTAdviceDirectDebit(FTAdviceDirectDebitRequestVO ftAdviceDirectDebitRequestVO){
        return bankRestService.doFTAdviceDirectDebit(ftAdviceDirectDebitRequestVO);
    }

    public MandateAdviceResponseVO handleMandateAdvice(MandateAdviceRequestVO mandateAdviceRequestVO){
        return bankRestService.doMandateAdvice(mandateAdviceRequestVO);
    }

    public AccountBlockResponseVO handleAccountBlock(AccountBlockRequestVO accountBlockRequestVO){
        return bankRestService.doAccountBlock(accountBlockRequestVO);
    }

    public AccountUnblockResponseVO handleAccountUnblock(AccountUnblockRequestVO accountUnblockRequestVO){
        return bankRestService.doAccountUnblock(accountUnblockRequestVO);
    }

    public AmountBlockResponseVO handleAmountBlock(AmountBlockRequestVO amountBlockRequestVO){
        return bankRestService.doAmountBlock(amountBlockRequestVO);
    }

    public AmountUnblockResponseVO handleAmountUnblock(AmountUnblockRequestVO amountUnblockRequestVO){
        return bankRestService.doAmountUnblock(amountUnblockRequestVO);
    }

    public BalanceEnquiryResponseVO handleBalanceEnquiry(BalanceEnquiryRequestVO balanceEnquiryRequestVO){
        return bankRestService.doBalanceEnquiry(balanceEnquiryRequestVO);
    }
}
