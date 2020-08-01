package com.globalaccelerex.nipmiddleware.service;

import com.globalaccelerex.nipmiddleware.institution.ConfigUtil;
import com.globalaccelerex.nipmiddleware.mapper.BankMapper;
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
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleResponseVO;
import com.globalaccelerex.nipmiddleware.service.db.FinancialInstitutionDbService;
import com.globalaccelerex.nipmiddleware.service.rest.BankRestService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NIPInwardService {

    private FinancialInstitutionDbService financialInstitutionDbService;

    private BankRestService bankRestService;

    private BankMapper bankMapper;




    public NESingleResponseVO handleNameEnquiry(NESingleRequestVO neSingleRequestVO , String originatingInstitutionCode){
        final val neSingleRequestDTO = bankMapper.mapNESingleRequestDTO.apply(neSingleRequestVO);
        final val neSingleResponseDTO = bankRestService.doNameEnquiry(neSingleRequestDTO, originatingInstitutionCode);
        final val neSingleResponseVO = bankMapper.mapNESingleResponseVO.apply(neSingleResponseDTO);
        return neSingleResponseVO;
    }

    public FinancialInstitutionListResponseVO handleFIList(FinancialInstitutionListRequestVO financialInstitutionListRequest, String originatingInstitutionCode){
        return bankRestService.doFIList(financialInstitutionListRequest,originatingInstitutionCode);
    }

    public FTDirectDebitResponseVO handleFT_DirectDebit(FTDirectDebitRequestVO ftDirectDebitRequestVO, String originatingInstitutionCode){
        return bankRestService.doFTDirectDebit(ftDirectDebitRequestVO,originatingInstitutionCode);
    }

    public FTDirectCreditResponseVO handleFT_DirectCredit(FTDirectCreditRequestVO ftDirectCreditRequestVO, String originatingInstitutionCode){
        return bankRestService.doFTDirectCredit(ftDirectCreditRequestVO,originatingInstitutionCode);
    }

    public TSQuerySingleResponseVO handleTSQ(TSQuerySingleRequestVO tsQuerySingleRequestVO, String originatingInstitutionCode){
        return bankRestService.doTsq(tsQuerySingleRequestVO,originatingInstitutionCode);
    }

    public FTAdviceDirectCreditResponseVO handleFTAdviceDirectCredit(FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO, String originatingInstitutionCode){
        return bankRestService.doFTAdviceDirectCredit(ftAdviceDirectCreditRequestVO,originatingInstitutionCode);
    }

    public FTAdviceDirectDebitResponseVO handleFTAdviceDirectDebit(FTAdviceDirectDebitRequestVO ftAdviceDirectDebitRequestVO, String originatingInstitutionCode){
        return bankRestService.doFTAdviceDirectDebit(ftAdviceDirectDebitRequestVO,originatingInstitutionCode);
    }

    public MandateAdviceResponseVO handleMandateAdvice(MandateAdviceRequestVO mandateAdviceRequestVO, String originatingInstitutionCode){
        return bankRestService.doMandateAdvice(mandateAdviceRequestVO,originatingInstitutionCode);
    }

    public AccountBlockResponseVO handleAccountBlock(AccountBlockRequestVO accountBlockRequestVO, String originatingInstitutionCode){
        return bankRestService.doAccountBlock(accountBlockRequestVO,originatingInstitutionCode);
    }

    public AccountUnblockResponseVO handleAccountUnblock(AccountUnblockRequestVO accountUnblockRequestVO, String originatingInstitutionCode){
        return bankRestService.doAccountUnblock(accountUnblockRequestVO,originatingInstitutionCode);
    }

    public AmountBlockResponseVO handleAmountBlock(AmountBlockRequestVO amountBlockRequestVO, String originatingInstitutionCode){
        return bankRestService.doAmountBlock(amountBlockRequestVO,originatingInstitutionCode);
    }

    public AmountUnblockResponseVO handleAmountUnblock(AmountUnblockRequestVO amountUnblockRequestVO, String originatingInstitutionCode){
        return bankRestService.doAmountUnblock(amountUnblockRequestVO,originatingInstitutionCode);
    }

    public BalanceEnquiryResponseVO handleBalanceEnquiry(BalanceEnquiryRequestVO balanceEnquiryRequestVO, String originatingInstitutionCode){
        return bankRestService.doBalanceEnquiry(balanceEnquiryRequestVO,originatingInstitutionCode);
    }

    @Autowired
    public void setFinancialInstitutionDbService(FinancialInstitutionDbService financialInstitutionDbService) {
        this.financialInstitutionDbService = financialInstitutionDbService;
    }

    @Autowired
    public void setBankRestService(BankRestService bankRestService) {
        this.bankRestService = bankRestService;
    }

    @Autowired
    public void setBankMapper(BankMapper bankMapper) {
        this.bankMapper = bankMapper;
    }

    @Autowired
    public void setConfigUtil(ConfigUtil configUtil) {
        this.configUtil = configUtil;
    }
}
