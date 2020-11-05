package com.globalaccelerex.nipmiddleware.service;

import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.*;
import com.globalaccelerex.nipmiddleware.payload.bank.fiList.FinancialInstitutionListRequestDTO;
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
import com.globalaccelerex.nipmiddleware.service.rest.BankRestService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NIPInwardService {


    private BankRestService bankRestService;

    private BankMapper bankMapper;

    private FtAdviceMapper ftAdviceMapper;

    private FtMapper ftMapper;

    private AccountMapper accountMapper;

    private AmountMapper amountMapper;

    public NESingleResponseVO handleNameEnquiry(NESingleRequestVO neSingleRequestVO , String originatingInstitutionCode, IMarker marker){
        val neSingleRequestDTO = bankMapper.mapNESingleRequestDTO.apply(neSingleRequestVO);
        val neSingleResponseDTO = bankRestService.doNameEnquiry(neSingleRequestDTO, originatingInstitutionCode,marker);
        val neSingleResponseVO = bankMapper.mapNESingleResponseVO.apply(neSingleResponseDTO);
        neSingleResponseVO.setChannelCode(neSingleRequestVO.getChannelCode());
        return neSingleResponseVO;
    }

    public FinancialInstitutionListResponseVO handleFIList(FinancialInstitutionListRequestVO financialInstitutionListRequest, String originatingInstitutionCode, IMarker marker){
        final val financialInstitutionListRequestDTO = new FinancialInstitutionListRequestDTO();
        financialInstitutionListRequestDTO.setHeader(financialInstitutionListRequest.getHeader());
        financialInstitutionListRequestDTO.setRecordList(financialInstitutionListRequest.getRecordList());
        final val financialInstitutionListResponseDTO = bankRestService.doFIList(financialInstitutionListRequestDTO, originatingInstitutionCode, marker);
        final val financialInstitutionListResponseVO = new FinancialInstitutionListResponseVO();
        financialInstitutionListResponseVO.setBatchNumber(financialInstitutionListResponseDTO.getBatchNumber());
        financialInstitutionListResponseVO.setChannelCode(financialInstitutionListResponseDTO.getChannelCode());
        financialInstitutionListResponseVO.setDestinationInstitutionCode(financialInstitutionListResponseDTO.getDestinationInstitutionCode());
        financialInstitutionListResponseVO.setNumberOfRecords(financialInstitutionListResponseDTO.getNumberOfRecords());
        financialInstitutionListResponseVO.setResponseCode(financialInstitutionListResponseDTO.getResponseCode());
        return financialInstitutionListResponseVO;
    }

    public FTDirectDebitResponseVO handleFTDirectDebit(FTDirectDebitRequestVO ftDirectDebitRequestVO, String originatingInstitutionCode, IMarker marker){
        final val ftDirectDebitRequestDTO = ftMapper.mapFTDirectDebitRequestDTO.apply(ftDirectDebitRequestVO);
        final val ftDirectDebitResponseDTO = bankRestService.doFTDirectDebit(ftDirectDebitRequestDTO, originatingInstitutionCode, marker);
        final val ftDirectDebitResponseVO = ftMapper.mapFTDirectDebitResponseVO.apply(ftDirectDebitResponseDTO);
        ftDirectDebitResponseVO.setChannelCode(ftDirectDebitRequestVO.getChannelCode());
        return ftDirectDebitResponseVO;
    }

    public FTAdviceDirectDebitResponseVO handleFTAdviceDirectDebit(FTAdviceDirectDebitRequestVO ftAdviceDirectDebitRequestVO, String originatingInstitutionCode, IMarker marker){
        final val ftAdviceDirectDebitRequestDTO = ftAdviceMapper.mapFTAdviceDirectDebitRequestDTO.apply(ftAdviceDirectDebitRequestVO);
        final val ftAdviceDirectDebitResponseDTO = bankRestService.doFTAdviceDirectDebit(ftAdviceDirectDebitRequestDTO, originatingInstitutionCode, marker);
        final val ftAdviceDirectDebitResponseVO = ftAdviceMapper.mapFTAdviceDirectDebitResponseVO.apply(ftAdviceDirectDebitResponseDTO);
        ftAdviceDirectDebitResponseVO.setChannelCode(ftAdviceDirectDebitRequestVO.getChannelCode());
        return ftAdviceDirectDebitResponseVO;
    }

    public FTDirectCreditResponseVO handleFTDirectCredit(FTDirectCreditRequestVO ftDirectCreditRequestVO, String originatingInstitutionCode, IMarker marker){
        final val ftDirectCreditRequestDTO = ftMapper.mapFTDirectCreditRequestDTO.apply(ftDirectCreditRequestVO);
        final val ftDirectCreditResponseDTO = bankRestService.doFTDirectCredit(ftDirectCreditRequestDTO, originatingInstitutionCode, marker);
        final val ftDirectCreditResponseVO = ftMapper.mapFTDirectCreditResponseVO.apply(ftDirectCreditResponseDTO);
        ftDirectCreditResponseVO.setChannelCode(ftDirectCreditRequestVO.getChannelCode());
        return ftDirectCreditResponseVO;
    }

    public FTAdviceDirectCreditResponseVO handleFTAdviceDirectCredit(FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO, String originatingInstitutionCode, IMarker marker){
        final val ftAdviceDirectCreditRequestDTO = ftAdviceMapper.mapFTAdviceDirectCreditRequestDTO.apply(ftAdviceDirectCreditRequestVO);
        final val ftAdviceDirectCreditResponseDTO = bankRestService.doFTAdviceDirectCredit(ftAdviceDirectCreditRequestDTO, originatingInstitutionCode, marker);
        final val ftAdviceDirectCreditResponseVO = ftAdviceMapper.mapFTAdviceDirectCreditResponseVO.apply(ftAdviceDirectCreditResponseDTO);
        ftAdviceDirectCreditResponseVO.setChannelCode(ftAdviceDirectCreditRequestVO.getChannelCode());
        return ftAdviceDirectCreditResponseVO;
    }

    public TSQuerySingleResponseVO handleTSQ(TSQuerySingleRequestVO tsQuerySingleRequestVO, String originatingInstitutionCode, IMarker marker){
        final val tsqSingleResponseDTO = bankRestService.doTsq(tsQuerySingleRequestVO.getSessionId(), originatingInstitutionCode, marker);
        final val tsQuerySingleResponseVO = new TSQuerySingleResponseVO();
        tsQuerySingleResponseVO.setChannelCode(tsQuerySingleRequestVO.getChannelCode());
        return tsQuerySingleResponseVO;
    }

    public MandateAdviceResponseVO handleMandateAdvice(MandateAdviceRequestVO mandateAdviceRequestVO, String originatingInstitutionCode, IMarker marker){
        final val mandateAdviceRequestDTO = bankMapper.mapMandateAdviceRequestDTO.apply(mandateAdviceRequestVO);
        final val mandateAdviceResponseDTO = bankRestService.doMandateAdvice(mandateAdviceRequestDTO, originatingInstitutionCode, marker);
        final val mandateAdviceResponseVO = bankMapper.mapMandateAdviceResponseVO.apply(mandateAdviceResponseDTO);
        mandateAdviceResponseVO.setChannelCode(mandateAdviceRequestVO.getChannelCode());
        return mandateAdviceResponseVO;
    }

    public AccountBlockResponseVO handleAccountBlock(AccountBlockRequestVO accountBlockRequestVO, String originatingInstitutionCode, IMarker marker){
        final val accountBlockRequestDTO = accountMapper.mapAccountBlockRequestDTO.apply(accountBlockRequestVO);
        final val accountBlockResponseDTO = bankRestService.doAccountBlock(accountBlockRequestDTO, originatingInstitutionCode, marker);
        final val accountBlockResponseVO = accountMapper.mapAccountBlockResponseVO.apply(accountBlockResponseDTO);
        accountBlockResponseVO.setChannelCode(accountBlockRequestVO.getChannelCode());
        return accountBlockResponseVO;
    }

    public AccountUnblockResponseVO handleAccountUnblock(AccountUnblockRequestVO accountUnblockRequestVO, String originatingInstitutionCode, IMarker marker){
        final val accountUnblockRequestDTO = accountMapper.mapAccountUnblockRequestDTO.apply(accountUnblockRequestVO);
        final val accountUnblockResponseDTO = bankRestService.doAccountUnblock(accountUnblockRequestDTO, originatingInstitutionCode, marker);
        final val accountUnblockResponseVO = accountMapper.mapAccountUnblockResponseVO.apply(accountUnblockResponseDTO);
        accountUnblockResponseVO.setChannelCode(accountUnblockRequestVO.getChannelCode());
        return accountUnblockResponseVO;
    }

    public AmountBlockResponseVO handleAmountBlock(AmountBlockRequestVO amountBlockRequestVO, String originatingInstitutionCode, IMarker marker){
        final val amountBlockRequestDTO = amountMapper.mapAmountBlockRequestDTO.apply(amountBlockRequestVO);
        final val amountBlockResponseDTO = bankRestService.doAmountBlock(amountBlockRequestDTO, originatingInstitutionCode, marker);
        final val amountBlockResponseVO = amountMapper.mapAmountBlockResponseVO.apply(amountBlockResponseDTO);
        amountBlockResponseVO.setChannelCode(amountBlockRequestVO.getChannelCode());
        return amountBlockResponseVO;
    }

    public AmountUnblockResponseVO handleAmountUnblock(AmountUnblockRequestVO amountUnblockRequestVO, String originatingInstitutionCode, IMarker marker){
        final val amountUnblockRequestDTO = amountMapper.mapAmountUnblockRequestDTO.apply(amountUnblockRequestVO);
        final val amountUnblockResponseDTO = bankRestService.doAmountUnblock(amountUnblockRequestDTO, originatingInstitutionCode, marker);
        final val amountUnblockResponseVO = amountMapper.mapAmountUnblockResponseVO.apply(amountUnblockResponseDTO);
        amountUnblockResponseVO.setChannelCode(amountUnblockRequestVO.getChannelCode());
        return amountUnblockResponseVO;
    }

    public BalanceEnquiryResponseVO handleBalanceEnquiry(BalanceEnquiryRequestVO balanceEnquiryRequestVO, String originatingInstitutionCode, IMarker marker){
        final val balanceEnquiryRequestDTO = bankMapper.mapBalanceEnquiryRequestDTO.apply(balanceEnquiryRequestVO);
        final val balanceEnquiryResponseDTO = bankRestService.doBalanceEnquiry(balanceEnquiryRequestDTO, originatingInstitutionCode, marker);
        final val balanceEnquiryResponseVO = bankMapper.mapBalanceEnquiryResponseVO.apply(balanceEnquiryResponseDTO);
        balanceEnquiryResponseVO.setChannelCode(balanceEnquiryRequestVO.getChannelCode());
        return balanceEnquiryResponseVO;
    }


    @Autowired
    public void setFtAdviceMapper(FtAdviceMapper ftAdviceMapper) {
        this.ftAdviceMapper = ftAdviceMapper;
    }

    @Autowired
    public void setFtMapper(FtMapper ftMapper) {
        this.ftMapper = ftMapper;
    }

    @Autowired
    public void setAccountMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Autowired
    public void setAmountMapper(AmountMapper amountMapper) {
        this.amountMapper = amountMapper;
    }

    @Autowired
    public void setBankRestService(BankRestService bankRestService) {
        this.bankRestService = bankRestService;
    }

    @Autowired
    public void setBankMapper(BankMapper bankMapper) {
        this.bankMapper = bankMapper;
    }


}
