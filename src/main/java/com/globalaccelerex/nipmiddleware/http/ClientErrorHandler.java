/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.http;



import com.globalaccelerex.nipmiddleware.exception.HttpException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.Scanner;


public class ClientErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if (response instanceof MarkedClientResponse) {
            IMarker marker = ((MarkedClientResponse) response).getMarker();
            if (marker != null) {
                marker.trace("Response Status:" + response.getStatusText());
                marker.trace("Response Code:" + response.getRawStatusCode());
            }
        }
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Scanner sc = new Scanner(response.getBody());
        sc.useDelimiter("\\A");
        String rawBody = sc.next();
        if (response instanceof MarkedClientResponse) {
            IMarker marker = ((MarkedClientResponse) response).getMarker();
            if (marker != null) {
                marker.trace("Response Body:" + rawBody);
            }
            throw new HttpException(marker, rawBody);
        }
        throw new HttpException(rawBody);
    }

}
