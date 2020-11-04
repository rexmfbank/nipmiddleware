package com.globalaccelerex.nipmiddleware.security.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

import static com.globalaccelerex.nipmiddleware.api.ClientAPI.INWARD_API;

@Slf4j
@Order(3)
@Configuration
@EnableWebSecurity
public class InwardSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientAuthenticationProvider clientAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(INWARD_API + "/**").
                csrf().disable().cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
        http.addFilterBefore(new ClientAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(clientAuthenticationProvider);
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}