package com.globalaccelerex.nipmiddleware.security.outward;

import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.security.AccessControlException;
import com.globalaccelerex.nipmiddleware.security.AccessControlHttpClient;
import com.globalaccelerex.nipmiddleware.security.AccessControlResponse;
import com.globalaccelerex.nipmiddleware.security.admin.AdminAuthenticationData;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.globalaccelerex.nipmiddleware.api.AccessControlAPI.VALIDATE_TOKEN_API;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

@Slf4j
@Component
public class OutwardAuthenticationProvider implements AuthenticationProvider {

    private static final String TOKEN_IDENTIFIER = "X_TOKEN";

    @Autowired
    private AccessControlHttpClient accessControlHttpClient;

    @Autowired
    private ClientDbService clientDbService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        final val outwardAuthenticationToken = (OutwardAuthenticationToken) authentication;
        final val outwardAuthenticationData = outwardAuthenticationToken.getOutwardAuthenticationData();
        validateAccessToken(outwardAuthenticationData);
        validateSignature(outwardAuthenticationData);
        validateClientId(outwardAuthenticationData);
        validateJwtToken(outwardAuthenticationData);
        SecurityContextHolder.getContext().setAuthentication(outwardAuthenticationToken);
        return authentication;
    }

    private void validateAccessToken(OutwardAuthenticationData outwardAuthenticationData){
        try{
            if(StringUtils.isBlank(outwardAuthenticationData.getAccessToken())){
                final val errorResponse = new ErrorResponse(NIP_111);
                throw new AccessControlException(errorResponse);
            }
            final val accessControlResponse = cache.getUnchecked(outwardAuthenticationData.getAccessToken());
        }catch (UncheckedExecutionException exception) {
            if (AccessControlException.class.isInstance(exception.getCause())) {
                throw (AccessControlException) exception.getCause();
            }
        }
    }

    private void validateJwtToken(OutwardAuthenticationData outwardAuthenticationData){
        if(StringUtils.isBlank(outwardAuthenticationData.getUserToken())){
            final val errorResponse = new ErrorResponse(NIP_117);
            throw new AccessControlException(errorResponse);
        }
        try{
            final val claims = jwtTokenUtil.parseJWT(outwardAuthenticationData.getUserToken());
        }catch(ExpiredJwtException ex){
            log.error("Expired Token Exception {}", ex);
            final val errorResponse = new ErrorResponse(NIP_118);
            throw new AccessControlException(errorResponse);
        }catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            log.error(" Exception {}", ex);
            final val errorResponse = new ErrorResponse(NIP_119);
            throw new AccessControlException(errorResponse);
        }
    }

    private void validateClientId(OutwardAuthenticationData outwardAuthenticationData){
        if(StringUtils.isBlank(outwardAuthenticationData.getClientId())){
            final val errorResponse = new ErrorResponse(NIP_115);
            throw new AccessControlException(errorResponse);
        }
        final val clientEntity = clientDbService.findClientByClientId(outwardAuthenticationData.getClientId());
        if(clientEntity == null){
            final val errorResponse = new ErrorResponse(NIP_116);
            throw new AccessControlException(errorResponse);
        }

    }

    private void validateSignature(OutwardAuthenticationData outwardAuthenticationData) {
        if (!outwardAuthenticationData.isValidSignature()) {
            val errorResponse = new ErrorResponse(NIP_112);
            throw new AccessControlException(errorResponse);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
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
