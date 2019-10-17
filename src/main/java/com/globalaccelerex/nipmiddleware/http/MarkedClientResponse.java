/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.http;


import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;


public class MarkedClientResponse implements ClientHttpResponse {

        private final IMarker marker;
        private final ClientHttpResponse delegate;

        public MarkedClientResponse(IMarker marker, ClientHttpResponse delegate) {
            this.marker = marker;
            this.delegate = delegate;
        }

        public IMarker getMarker() {
            return marker;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return delegate.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return delegate.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return delegate.getStatusText();
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public InputStream getBody() throws IOException {
            return delegate.getBody();
        }

        @Override
        public HttpHeaders getHeaders() {
            return delegate.getHeaders();
        }

    }
