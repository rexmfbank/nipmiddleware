package com.globalaccelerex.nipmiddleware.security.client;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

public class ClientAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private ClientAuthenticationData clientAuthenticationData;

    public ClientAuthenticationToken(ClientAuthenticationData clientAuthenticationData){
        super(null);
        this.clientAuthenticationData = clientAuthenticationData;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.clientAuthenticationData;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject); //To change body of generated methods, choose Tools | Templates.
    }

    public void setAuthenticationData(ClientAuthenticationData data){
        this.clientAuthenticationData = data;
    }

}
