package com.globalaccelerex.nipmiddleware.security.outward;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

public class OutwardAuthenticationToken  extends AbstractAuthenticationToken {

    @Getter
    private OutwardAuthenticationData outwardAuthenticationData;

    public OutwardAuthenticationToken(OutwardAuthenticationData outwardAuthenticationData){
        super(null);
        this.outwardAuthenticationData = outwardAuthenticationData;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.outwardAuthenticationData;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject); //To change body of generated methods, choose Tools | Templates.
    }
}
