package com.globalaccelerex.nipmiddleware.security.client;

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

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_124;
import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_125;

@Slf4j
@Component
public class ClientAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ClientDbService clientDbService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        final val outwardAuthenticationToken = (ClientAuthenticationToken) authentication;
        final val outwardAuthenticationData = outwardAuthenticationToken.getClientAuthenticationData();

        if (StringUtils.isNotBlank(outwardAuthenticationData.getAuthorization())){
            outwardAuthenticationData.decrypt();
        }

        validate(outwardAuthenticationData);
        outwardAuthenticationToken.setAuthenticationData(outwardAuthenticationData);
        SecurityContextHolder.getContext().setAuthentication(outwardAuthenticationToken);
        return authentication;
    }


    private void validate(ClientAuthenticationData data) {

        // get client
        val clientEntityOpt = clientDbService.findClientByClientId(data.getUsername());
        if (!clientEntityOpt.isPresent()) {
            throw new AccessControlException(new ErrorResponse(NIP_124));
        }

        val clientEntity = clientEntityOpt.get();
        /// validate password
        if ( !bCryptPasswordEncoder.matches(StringUtils.defaultString(data.getPassword(),""), clientEntity.getPassword()) ) {
            throw new AccessControlException(new ErrorResponse(NIP_125));
        }


        // save client in session
        data.setClient(clientEntity);
    }




    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(ClientAuthenticationToken.class);
    }

}
