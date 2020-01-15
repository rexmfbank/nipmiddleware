package com.globalaccelerex.nipmiddleware.service;

import com.globalaccelerex.nipmiddleware.mapper.NIPInwardMapper;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
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
}
