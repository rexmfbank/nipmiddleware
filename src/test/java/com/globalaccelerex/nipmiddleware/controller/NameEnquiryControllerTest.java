package com.globalaccelerex.nipmiddleware.controller;

import com.globalaccelerex.nipmiddleware.payload.client.nameenquiry.NESingleResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.NAME_ENQUIRY;
import static com.globalaccelerex.nipmiddleware.api.ClientAPI.OUTWARD_API;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
public class NameEnquiryControllerTest extends AbstractControllerTest {

    private AuthHeaderUtil authHeaderUtil;

    @Test
    public void testInvalidNUBANAccountNoMustFail(){
        authHeaderUtil = new AuthHeaderUtil();
        val path = OUTWARD_API + NAME_ENQUIRY;
        val url = createUrlWithPort(path);
        val neSingleRequest = NEUtil.buildNESingleRequestWithAlphaNumericAccountNo();
        try {
            final val httpHeaders = authHeaderUtil.buildHttpAuthHeader();
            final val requestEntity = new HttpEntity<>(neSingleRequest, httpHeaders);
            final val responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, NESingleResponse.class);
            assertThat(400 ,is(responseEntity.getStatusCodeValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test12DigitsAccountNoMustFail(){
        authHeaderUtil = new AuthHeaderUtil();
        val path = OUTWARD_API + NAME_ENQUIRY;
        val url = createUrlWithPort(path);
        val neSingleRequest = NEUtil.buildNESingleRequestWith12DigitsAccountNo();
        try {
            final val httpHeaders = authHeaderUtil.buildHttpAuthHeader();
            final val requestEntity = new HttpEntity<>(neSingleRequest, httpHeaders);
            final val responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, NESingleResponse.class);
            assertThat(400 ,is(responseEntity.getStatusCodeValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAlphaNumericBankCodeMustFail(){
        authHeaderUtil = new AuthHeaderUtil();
        val path = OUTWARD_API + NAME_ENQUIRY;
        val url = createUrlWithPort(path);
        val neSingleRequest = NEUtil.buildAphaNumericBankCode();
        try {
            final val httpHeaders = authHeaderUtil.buildHttpAuthHeader();
            final val requestEntity = new HttpEntity<>(neSingleRequest, httpHeaders);
            final val responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, NESingleResponse.class);
            assertThat(400 ,is(responseEntity.getStatusCodeValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
