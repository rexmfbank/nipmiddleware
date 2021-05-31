package com.globalaccelerex.nipmiddleware.security.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.security.accesscontrol.AccessControlException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.UNAUTHORIZED_ACCESS_MSG;


@Slf4j
public class AdminAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    public AdminAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try{
            log.info("verifying admin authentication ");
            attemptAuthentication(request, response);
            filterChain.doFilter(req, res);
        }catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            log.error("Admin  authentication service exception => " +authenticationException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            if (authenticationException instanceof AccessControlException) {
                AccessControlException ex = (AccessControlException) authenticationException;
                response.addHeader("Content-Type", "application/json");

                response.getWriter().print(OBJECT_MAPPER.writeValueAsString(ex.getErrorResponse()));
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
            }
        }catch (Exception ex){
            SecurityContextHolder.clearContext();
            log.error("Admin authentication exception "+ ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader("Content-Type", "application/json");

            final val errorResponse = new ErrorResponse();
            errorResponse.setResponseCode(NIP_201.getCode());
            errorResponse.setResponseMessage(UNAUTHORIZED_ACCESS_MSG);
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(errorResponse));
        }
    }

    private void attemptAuthentication(HttpServletRequest request,
                                       HttpServletResponse response) throws IOException, AuthenticationException {
        val accessToken = request.getHeader("X_TOKEN");
        val timestamp = request.getHeader("Timestamp");
        val nonce = request.getHeader("Nonce");
        val signature = request.getHeader("Signature");
        val httpMethod = request.getMethod().toUpperCase();
        val queryString = StringUtils.defaultIfBlank(new UrlPathHelper().getOriginatingQueryString(request), "");
        String encodedURL = null;
        if (StringUtils.isBlank(queryString)) {
            encodedURL = URLEncoder.encode(new UrlPathHelper().getPathWithinApplication(request), "UTF-8");
        } else {
            encodedURL = URLEncoder.encode(new UrlPathHelper().getPathWithinApplication(request) + "?" + queryString, "UTF-8");
        }

        val authenticationData = AdminAuthenticationData.builder()
                .accessToken(accessToken)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .httpMethod(httpMethod)
                .encodedURL(encodedURL)
                .build();
        val authenticationToken = new AdminAuthenticationToken(authenticationData);
        log.info("verifying authentication for "+authenticationData.getAccessToken());
        authenticationManager.authenticate(authenticationToken);
    }
}
