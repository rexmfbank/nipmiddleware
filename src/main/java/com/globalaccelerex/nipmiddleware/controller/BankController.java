package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectCreditRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.nameenquiry.NESingleRequestVO;
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

}
