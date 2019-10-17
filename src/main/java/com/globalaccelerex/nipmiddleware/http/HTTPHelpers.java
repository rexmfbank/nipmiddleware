/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globalaccelerex.nipmiddleware.http;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 *
 * @author olanrewaju
 */
public class HTTPHelpers {

    public static String buildURIString(final String baseUrl, final String path, final Map<String, String> requestParameters, final Map<String, String> pathParameters) throws URISyntaxException {
        final URIBuilder uriBuilder = pathParameters == null ? new URIBuilder(baseUrl + path) : new URIBuilder(baseUrl + resolvePath(path, pathParameters));
        if (requestParameters != null) {
            requestParameters.forEach((key, value) -> {
                if (StringUtils.isNotEmpty(value)) {
                    uriBuilder.setParameter(key, value);
                }
            });
        }

        return uriBuilder.build().toString();
    }

    public static String buildURIString(final String baseUrl, String path, final Map<String, String> requestParameters) throws URISyntaxException {
        return buildURIString(baseUrl, path, requestParameters, null);
    }


    public static URI buildURI(final String baseUrl, final String path, final Map<String, String> requestParameters) throws URISyntaxException {
        final URIBuilder uriBuilder = new URIBuilder(baseUrl + path);
        if (requestParameters != null) {
            requestParameters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    uriBuilder.setParameter(key, value);
                }
            });
        }
        return uriBuilder.build();
    }

    public static URI buildURI(final String baseUrl, final String path) throws URISyntaxException {
        return buildURI(baseUrl, path, null);
    }

    public static String resolvePath(final String path, final Map<String, String> pathParameters) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path);
        return builder.buildAndExpand(pathParameters).toUriString();
    }

    public static String resolvePath(final String path, final Map<String, String> pathParameters, final Map<String, String> requestParameters) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path);
        if (requestParameters != null) {
            requestParameters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    builder.queryParam(key, value);
                }
            });
        }
        return builder.buildAndExpand(pathParameters).toUriString();
    }
}
