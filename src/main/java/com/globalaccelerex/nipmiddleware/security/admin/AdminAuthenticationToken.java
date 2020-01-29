package com.globalaccelerex.nipmiddleware.security.admin;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

public class AdminAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private AdminAuthenticationData adminAuthenticationData;

    public AdminAuthenticationToken(AdminAuthenticationData adminAuthenticationData) {
        super(null);
        this.adminAuthenticationData = adminAuthenticationData;
        setAuthenticated(false);
    }




    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.adminAuthenticationData;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject); //To change body of generated methods, choose Tools | Templates.
    }
}
