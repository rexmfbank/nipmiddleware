package com.globalaccelerex.nipmiddleware.security.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.security.accesscontrol.AccessControlException;
import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.TimeZone;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.CLIENT_NOT_FOUND_MSG;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.UNAUTHORIZED_ACCESS_MSG;


@Slf4j
public class ClientAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }


    public ClientAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logRequestDetails(request);

        try {
            attemptAuthentication(request, response);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            log.error("Internal authentication service exception => " + authenticationException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            if (authenticationException instanceof AccessControlException) {
                AccessControlException ex = (AccessControlException) authenticationException;
                response.addHeader("Content-Type", "application/json");

                response.getWriter().print(OBJECT_MAPPER.writeValueAsString(ex.getErrorResponse()));
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
            }
        } catch (CacheLoader.InvalidCacheLoadException i) {
            SecurityContextHolder.clearContext();
            log.error("Client not found" + i.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("Content-Type", "application/json");
            final val errorResponse = new ErrorResponse();
            errorResponse.setResponseCode(NIP_201.getCode());
            errorResponse.setResponseMessage(CLIENT_NOT_FOUND_MSG);
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(errorResponse));
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            log.error("Client Authentication exception " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("Content-Type", "application/json");

            final val errorResponse = new ErrorResponse();
            errorResponse.setResponseCode(NIP_201.getCode());
            errorResponse.setResponseMessage(UNAUTHORIZED_ACCESS_MSG);
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(errorResponse));
        }
    }

    private void logRequestDetails(HttpServletRequest request) {
        log.info("Request Method: " + request.getMethod());
        log.info("Request URL: " + request.getRequestURL().toString());
        log.info("Request Headers: ");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("Header Name: {} :: Header Value: {}", headerName, headerValue);
        }
    }

    private void attemptAuthentication(HttpServletRequest request,
                                       HttpServletResponse response) throws IOException, AuthenticationException {
        String authorization = request.getHeader("Authorization");

        final val outwardAuthenticationData = ClientAuthenticationData.builder()
                .authorization(authorization)
                .build();

        final val outwardAuthenticationToken = new ClientAuthenticationToken(outwardAuthenticationData);
        authenticationManager.authenticate(outwardAuthenticationToken);
    }
}
