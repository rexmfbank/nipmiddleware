package com.globalaccelerex.nipmiddleware.security.outward;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.security.AccessControlException;
import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_109;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_124;

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

        try{
            attemptAuthentication(request, response);
            filterChain.doFilter(request, response);
        }catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            log.error("Internal authentication service exception => " +authenticationException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            if (AccessControlException.class.isInstance(authenticationException)) {
                AccessControlException ex = (AccessControlException) authenticationException;
                response.addHeader("Content-Type", "application/json");

                response.getWriter().print(OBJECT_MAPPER.writeValueAsString(ex.getErrorResponse()));
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
            }
        }catch (CacheLoader.InvalidCacheLoadException i ){
            SecurityContextHolder.clearContext();
            log.error("Client not found"+ i.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("Content-Type", "application/json");

            final val errorResponse = new ErrorResponse();
            errorResponse.setResponseCode(NIP_124.getCode());
            errorResponse.setResponseMessage(NIP_124.getDescription());
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(errorResponse));
        }
        catch (Exception ex){
            SecurityContextHolder.clearContext();
            log.error("Unknown client service exception "+ ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("Content-Type", "application/json");

            final val errorResponse = new ErrorResponse();
            errorResponse.setResponseCode(NIP_109.getCode());
            errorResponse.setResponseMessage(NIP_109.getDescription());
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(errorResponse));
        }

    }

    private void attemptAuthentication(HttpServletRequest request,
                                       HttpServletResponse response) throws IOException, AuthenticationException {
        String authorization = request.getHeader("Authorization");

        final val outwardAuthenticationData = OutwardAuthenticationData.builder()
                .authorization(authorization)
                .build();

        final val outwardAuthenticationToken = new OutwardAuthenticationToken(outwardAuthenticationData);
        authenticationManager.authenticate(outwardAuthenticationToken);
    }
}
