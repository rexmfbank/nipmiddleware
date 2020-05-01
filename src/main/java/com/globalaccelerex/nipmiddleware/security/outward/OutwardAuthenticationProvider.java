package com.globalaccelerex.nipmiddleware.security.outward;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.security.accesscontrol.AccessControlException;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

@Slf4j
@Component
public class OutwardAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ClientDbService clientDbService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        final val outwardAuthenticationToken = (OutwardAuthenticationToken) authentication;
        final val outwardAuthenticationData = outwardAuthenticationToken.getOutwardAuthenticationData();

        if (StringUtils.isNotBlank(outwardAuthenticationData.getAuthorization())){
            outwardAuthenticationData.decrypt();
        }

        validate(outwardAuthenticationData);
        outwardAuthenticationToken.setAuthenticationData(outwardAuthenticationData);
        SecurityContextHolder.getContext().setAuthentication(outwardAuthenticationToken);
        return authentication;
    }


    private void validate(OutwardAuthenticationData data) {

        // get client
        ClientEntity client = clientDbService.findClientByClientId(data.getUsername());
        if (client == null) {
            throw new AccessControlException(new ErrorResponse(NIP_124));
        }

        /// validate password
        if ( !bCryptPasswordEncoder.matches(StringUtils.defaultString(data.getPassword(),""), client.getPassword()) ) {
            throw new AccessControlException(new ErrorResponse(NIP_125));
        }


        // save client in session
        data.setClient(client);
    }




    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(OutwardAuthenticationToken.class);
    }

}
