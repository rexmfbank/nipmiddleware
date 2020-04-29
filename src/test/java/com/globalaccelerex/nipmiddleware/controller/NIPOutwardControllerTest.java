package com.globalaccelerex.nipmiddleware.controller;


import com.globalaccelerex.nipmiddleware.payload.client.outward.fundstransfer.FTPendingResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.FUNDS_TRANSFER;
import static com.globalaccelerex.nipmiddleware.api.ClientAPI.NIP_OUTWARD_API;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
public class NIPOutwardControllerTest extends AbstractControllerTest {

    private AuthHeaderUtil authHeaderUtil;

    @Test
    public void testFTWithAmountEqualToZeroMustFail() {
        authHeaderUtil = new AuthHeaderUtil();
        val path = NIP_OUTWARD_API + FUNDS_TRANSFER;
        val url = createUrlWithPort(path);
        final val ftSingleCreditRequest = FTUtil.buildAmountEqualToZero();

        try {
            final val httpHeaders = authHeaderUtil.buildHttpAuthHeader();
            final val requestEntity = new HttpEntity<>(ftSingleCreditRequest, httpHeaders);
            final val responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, FTPendingResponse.class);
            assertThat(400 ,is(responseEntity.getStatusCodeValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendFTWithExistingPaymentReferenceShouldReturn400ErrorCode(){
        authHeaderUtil = new AuthHeaderUtil();
        val path = NIP_OUTWARD_API + FUNDS_TRANSFER;
        val url = createUrlWithPort(path);

        final val ftSingleCreditRequest = FTUtil.buildExisitingPaymentReference();
        try {
            final val httpHeaders = authHeaderUtil.buildHttpAuthHeader();
            final val requestEntity = new HttpEntity<>(ftSingleCreditRequest, httpHeaders);
            final val responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, FTPendingResponse.class);
            assertThat(400 ,is(responseEntity.getStatusCodeValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}