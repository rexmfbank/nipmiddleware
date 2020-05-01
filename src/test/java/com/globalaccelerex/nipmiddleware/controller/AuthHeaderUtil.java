package com.globalaccelerex.nipmiddleware.controller;

import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Base64;

public class AuthHeaderUtil {

    public HttpHeaders buildHttpAuthHeader() throws Exception{
        val requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        val authHeaderValue = "nip01" + ":" + "xxxxxxxxxx";

        requestHeaders.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(authHeaderValue.getBytes("utf-8")));

        return requestHeaders;
    }
}
