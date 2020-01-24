package com.globalaccelerex.nipmiddleware.security;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
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
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_111;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_112;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final String TOKEN_IDENTIFIER = "X_TOKEN";

    @Autowired
    private AccessControlHttpClient accessControlHttpClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("============ authenticate ===================");
        authentication.setAuthenticated(true);
        val authenticationToken = (AuthenticationToken) authentication;
        validate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return authentication;
    }

    private void validate(AuthenticationToken authenticationToken) {
        if (!AuthenticationData.class.isInstance(authenticationToken.getPrincipal())) {
            throw new AuthenticationCredentialsNotFoundException("Unable to authenticate invalid principal");
        }
        val authenticationData = (AuthenticationData) authenticationToken.getPrincipal();
        validateAccessToken(authenticationData);
        validateSignature(authenticationData);
    }

    private void validateSignature(AuthenticationData data) {
        if (!data.isValidSignature()) {
            val errorResponse = new ErrorResponse(NIP_112);
            throw new AccessControlException(errorResponse);
        }
    }
    private void validateAccessToken(AuthenticationData authenticationData) {
        try{
            if(StringUtils.isBlank(authenticationData.getAccessToken())){
                final val errorResponse = new ErrorResponse(NIP_111);
                throw new AccessControlException(errorResponse);
            }

            final val accessControlResponse = cache.getUnchecked(authenticationData.getAccessToken());
            if(StringUtils.isNotBlank(accessControlResponse.getAccessSecret())){
                authenticationData.setAccessSecret(accessControlResponse.getAccessSecret());
            }
        }catch (UncheckedExecutionException exception) {
            if (AccessControlException.class.isInstance(exception.getCause())) {
                throw (AccessControlException) exception.getCause();
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AuthenticationToken.class);
    }

    private LoadingCache<String, AccessControlResponse> cache = CacheBuilder.newBuilder()
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
