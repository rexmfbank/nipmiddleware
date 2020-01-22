package com.globalaccelerex.nipmiddleware.service.rest;

import com.globalaccelerex.nipmiddleware.config.NipConfig;
import com.globalaccelerex.nipmiddleware.http.HTTPRestTemplate;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleRequestVO;
import com.globalaccelerex.nipmiddleware.payload.nip.outward.nameenquiry.NESingleResponseVO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.api.BankAPI.CBA_API;
import static com.globalaccelerex.nipmiddleware.api.BankAPI.NAME_ENQUIRY_API;

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
}
