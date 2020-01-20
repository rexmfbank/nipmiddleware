package com.globalaccelerex.nipmiddleware.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private AuthenticationData data;

    public AuthenticationToken(AuthenticationData data) {
        super(null);
        this.data = data;
        setAuthenticated(false);
    }


    public AuthenticationData getAuthenticationData(){
        return data;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.data;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject); //To change body of generated methods, choose Tools | Templates.
    }
}
