package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountblock.AccountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.accountunblock.AccountUnblockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.amountblock.AmountBlockRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTAdviceDirectCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTAdviceDirectDebitRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.mandateadvice.MandateAdviceRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.tsq.TSQuerySingleRequestVO;
import com.globalaccelerex.nipmiddleware.util.MockResponseUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.globalaccelerex.nipmiddleware.api.BankAPI.*;

@Slf4j
@RestController
@RequestMapping(CBA_API)
public class BankController {

    @Autowired
    private MockResponseUtil mockResponseUtil;

    @PostMapping(NAME_ENQUIRY_API)
    public ResponseEntity<?> doNameEnquiry(@Valid @RequestBody NESingleRequestVO neSingleRequestVO){
        final val neSingleResponseVO = mockResponseUtil.buildNESingleResponseVO(neSingleRequestVO);
        return new ResponseEntity(neSingleResponseVO, HttpStatus.OK);
    }

    @PostMapping(FI_LIST_API)
    public ResponseEntity<?> doFIList(@Valid @RequestBody FinancialInstitutionListRequestVO financialInstitutionListRequest){
        final val financialInstitutionListResponseVO = mockResponseUtil.buildFIListResponse(financialInstitutionListRequest);
        return new ResponseEntity(financialInstitutionListResponseVO, HttpStatus.OK);
    }

    @PostMapping(FT_DIRECT_DEBIT_API)
    public ResponseEntity<?> doFT_DirectDebit(@Valid @RequestBody FTDirectDebitRequestVO ftDirectDebitRequestVO){
        final val ftDirectDebitResponseVO = mockResponseUtil.buildFTDirectDebitResponseVO(ftDirectDebitRequestVO);
        return new ResponseEntity(ftDirectDebitResponseVO, HttpStatus.OK);
    }

    @PostMapping(FT_DIRECT_CREDIT_API)
    public ResponseEntity<?> doFT_DirectCredit(@Valid @RequestBody FTDirectCreditRequestVO ftDirectCreditRequestVO){
        final val ftDirectCreditResponseVO = mockResponseUtil.buildFtDirectCreditResponseVO(ftDirectCreditRequestVO);
        return new ResponseEntity(ftDirectCreditResponseVO, HttpStatus.OK);
    }

    @PostMapping(TSQ_API)
    public ResponseEntity<?> doTSQ(@Valid @RequestBody TSQuerySingleRequestVO tsQuerySingleRequestVO){
        final val tsqSingleItemResponseVO = mockResponseUtil.buildTsqSingleItemResponseVO(tsQuerySingleRequestVO.getSessionId(), tsQuerySingleRequestVO.getSourceInstitutionCode());
        return new ResponseEntity(tsqSingleItemResponseVO, HttpStatus.OK);
    }

    @PostMapping(FT_ADVICE_DIRECT_DEBIT_API)
    public ResponseEntity<?> doFT_Advice_DirectDebit(@Valid @RequestBody FTAdviceDirectDebitRequestVO ftAdviceDirectDebitRequestVO){
        final val ftDirectDebitResponseVO = mockResponseUtil.mapFTAdviceDirectDebitResponseVO.apply(ftAdviceDirectDebitRequestVO);
        return new ResponseEntity(ftDirectDebitResponseVO, HttpStatus.OK);
    }

    @PostMapping(FT_ADVICE_DIRECT_CREDIT_API)
    public ResponseEntity<?> doFT_Active_DirectCredit(@Valid @RequestBody FTAdviceDirectCreditRequestVO ftAdviceDirectCreditRequestVO){
        final val ftDirectCreditResponseVO = mockResponseUtil.mapFTAdviceDirectCreditResponseVO.apply(ftAdviceDirectCreditRequestVO);
        return new ResponseEntity(ftDirectCreditResponseVO, HttpStatus.OK);
    }

    @PostMapping(MANDATE_ADVICE_API)
    public ResponseEntity<?> doMandateAdvice(@Valid @RequestBody MandateAdviceRequestVO mandateAdviceRequestVO){
        final val mandateAdviceResponseVO = mockResponseUtil.mapMandateAdviceResponseVO.apply(mandateAdviceRequestVO);
        return new ResponseEntity(mandateAdviceResponseVO, HttpStatus.OK);
    }

    @PostMapping(ACCOUNT_BLOCK_API)
    public ResponseEntity<?> doAccountBlock(@Valid @RequestBody AccountBlockRequestVO accountBlockRequestVO){
        final val accountBlockResponseVO = mockResponseUtil.mapAccountBlockResponseVO.apply(accountBlockRequestVO);
        return new ResponseEntity(accountBlockResponseVO, HttpStatus.OK);
    }

    @PostMapping(ACCOUNT_UNBLOCK_API)
    public ResponseEntity<?> doAccountUnBlock(@Valid @RequestBody AccountUnblockRequestVO accountUnblockRequestVO){
        final val accountUnblockResponseVO = mockResponseUtil.mapAccountUnblockResponseVO.apply(accountUnblockRequestVO);
        return new ResponseEntity(accountUnblockResponseVO, HttpStatus.OK);
    }

    @PostMapping(AMOUNT_BLOCK_API)
    public ResponseEntity<?> doAmountBlock(@Valid @RequestBody AmountBlockRequestVO amountBlockRequestVO){
        final val accountUnblockResponseVO = mockResponseUtil.mapAmountBlockResponseVO.apply(amountBlockRequestVO);
        return new ResponseEntity(accountUnblockResponseVO, HttpStatus.OK);
    }

}
