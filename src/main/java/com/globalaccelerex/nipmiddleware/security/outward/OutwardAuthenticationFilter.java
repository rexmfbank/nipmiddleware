package com.globalaccelerex.nipmiddleware.security.outward;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.TimeZone;

@Slf4j
public class OutwardAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }


    public OutwardAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


    }

    private void attemptAuthentication(HttpServletRequest request,
                                       HttpServletResponse response) throws IOException, AuthenticationException {
        val accessToken = request.getHeader("X_TOKEN");
        val userToken = request.getHeader("X_USER_TOKEN");
        val timestamp = request.getHeader("Timestamp");
        val nonce = request.getHeader("Nonce");
        val signature = request.getHeader("Signature");
        val clientId = request.getHeader("clientId");
        val httpMethod = request.getMethod().toUpperCase();
        val queryString = StringUtils.defaultIfBlank(new UrlPathHelper().getOriginatingQueryString(request), "");

        String encodedURL = null;
        if (StringUtils.isBlank(queryString)) {
            encodedURL = URLEncoder.encode(new UrlPathHelper().getPathWithinApplication(request), "UTF-8");
        } else {
            encodedURL = URLEncoder.encode(new UrlPathHelper().getPathWithinApplication(request) + "?" + queryString, "UTF-8");
        }

        final val outwardAuthenticationData = OutwardAuthenticationData.builder()
                .accessToken(accessToken)
                .clientId(clientId)
                .encodedURL(encodedURL)
                .httpMethod(httpMethod)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .userToken(userToken)
                .build();
        log.info("\n Outward Authentication Data :::::: {}" , outwardAuthenticationData.toString());
        log.info("\n Outward Authentication Data Signature :::::: {}" , outwardAuthenticationData.isValidSignature());
        final val outwardAuthenticationToken = new OutwardAuthenticationToken(outwardAuthenticationData);
        authenticationManager.authenticate(outwardAuthenticationToken);
    }
}
