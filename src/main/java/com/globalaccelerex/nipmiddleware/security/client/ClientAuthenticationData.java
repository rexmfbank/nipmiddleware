package com.globalaccelerex.nipmiddleware.security.client;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.security.accesscontrol.AccessControlException;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_201;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.AUTHORIZATION_HEADER_NOT_SENT_MSG;
import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.INVALID_AUTHORIZATION_HEADER_MSG;

@Data
@Builder
@Slf4j
@ToString
public class ClientAuthenticationData {

    private String authorization;
    private String username;
    private String password;
    private ClientEntity client;

    public void decrypt() {

        if (this.authorization == null){
            throw new AccessControlException(new ErrorResponse( AUTHORIZATION_HEADER_NOT_SENT_MSG, NIP_201.getCode()));
        }
        String auth = StringUtils.substringAfter(this.authorization, "Basic ");
        try{
            auth = new String(Base64.getDecoder().decode(auth.trim()));
        }catch (Exception ex){
            throw new AccessControlException(new ErrorResponse(INVALID_AUTHORIZATION_HEADER_MSG, NIP_201.getCode()));
        }

        String[] token = StringUtils.split(auth, ":");
        if (token.length != 2){
            throw new AccessControlException(new ErrorResponse(INVALID_AUTHORIZATION_HEADER_MSG, NIP_201.getCode()));
        }
        this.username = token[0];
        this.password = token[1];

    }


}
