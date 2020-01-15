package com.globalaccelerex.nipmiddleware.service.rest;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.http.HTTPRestTemplate;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.FinancialInstitutionListResponseVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.fundtransfer.FTDirectDebitResponseVO;
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
}
