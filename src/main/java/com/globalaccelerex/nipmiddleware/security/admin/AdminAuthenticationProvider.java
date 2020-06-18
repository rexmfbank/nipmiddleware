package com.globalaccelerex.nipmiddleware.security.admin;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.security.accesscontrol.AccessControlException;
import com.globalaccelerex.nipmiddleware.security.accesscontrol.AccessControlHttpClient;
import com.globalaccelerex.nipmiddleware.security.accesscontrol.AccessControlResponse;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.globalaccelerex.nipmiddleware.api.AccessControlAPI.VALIDATE_TOKEN_API;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.*;

@Slf4j
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private static final String TOKEN_IDENTIFIER = "X_TOKEN";
    private static final String ALLOWED_SERVICE = "nip-service";

    @Autowired
    private AccessControlHttpClient accessControlHttpClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        val authenticationToken = (AdminAuthenticationToken) authentication;
        validate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return authentication;
    }

    private void validate(AdminAuthenticationToken authenticationToken) {
        if (!(authenticationToken.getPrincipal() instanceof AdminAuthenticationData)) {
            throw new AuthenticationCredentialsNotFoundException("Unable to authenticate invalid principal");
        }
        val authenticationData = (AdminAuthenticationData) authenticationToken.getPrincipal();
        validateAccessToken(authenticationData);
        validateSignature(authenticationData);
    }

    private void validateSignature(AdminAuthenticationData data) {
        if (!data.isValidSignature()) {
            val errorResponse = new ErrorResponse(INVALID_SIGNATURE_MSG ,NIP_201.getCode());
            throw new AccessControlException(errorResponse);
        }
    }
    private void validateAccessToken(AdminAuthenticationData authenticationData) {
        try{
            if(StringUtils.isBlank(authenticationData.getAccessToken())){
                final val errorResponse = new ErrorResponse(ACCESS_TOKEN_NOT_SENT_MSG,NIP_201.getCode());
                throw new AccessControlException(errorResponse);
            }

            final val accessControlResponse = cache.getUnchecked(authenticationData.getAccessToken());
            if(StringUtils.isNotBlank(accessControlResponse.getAllowedServices()) && !accessControlResponse.getAllowedServices().contains(ALLOWED_SERVICE)){
                throw new AccessControlException(new ErrorResponse(USER_ACCESS_FORBIDDEN_MSG,NIP_201.getCode()));
            }
            if (StringUtils.isNotBlank(accessControlResponse.getAccessSecret())) {
                authenticationData.setAccessSecret(accessControlResponse.getAccessSecret());
            }
        }catch (UncheckedExecutionException exception) {
            if (exception.getCause() instanceof AccessControlException) {
                throw (AccessControlException) exception.getCause();
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AdminAuthenticationToken.class);
    }

    private final LoadingCache<String, AccessControlResponse> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .recordStats()
            .build(new CacheLoader<String, AccessControlResponse>() {
                @Override
                public AccessControlResponse load(String accessToken) {
                    return validateAccessToken(accessToken);
                }
            });

    private AccessControlResponse validateAccessToken(String accessToken) {
        log.info("performing validate access token toward access control service  ");
        val accessControlResponse = accessControlHttpClient.
                getRequest(VALIDATE_TOKEN_API, null, AccessControlResponse.class, null, Collections.singletonMap(TOKEN_IDENTIFIER, accessToken));
        log.info("done performing validate access token toward access control service ");
        return accessControlResponse;
    }
}
