/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.http;


import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 *
 * @author HP
 */
public class RequestInterceptor  implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            final IMarker marker = getMarker(request);
            if (marker != null) {
                marker.trace("Headers: " + request.getHeaders());
                marker.trace(String.format("Request URL: %s ; Request Body: %s ", request.getURI(), new String(body)));
                marker.setRequest(request.getURI().toString(), new String(body));
            }

            return new MarkedClientResponse(marker, execution.execute(request, body));
        }

        public IMarker getMarker(HttpRequest request) {
            if (request.getHeaders() != null && request.getHeaders().containsKey(IMarker.IDENTIFIER)) {
                return Marker.fromMap(request.getHeaders().getFirst(IMarker.IDENTIFIER));
            }
            return null;
        }
    }
