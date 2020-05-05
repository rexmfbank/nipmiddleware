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

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

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

        //Basic aHJtczpkYmQzYWJjNWU3OTU1NTJlNzliZDc3ZDg0MzllY2M0YmY3NDhiMTI4NzE3MTUyMmNlYjA4NGMzNTM0NWUzNjA5MGIwMDlmYTljNDlmMTY0YTQ5ODYzM2U1YWJkNjY2Yjc0NTg5N2MxZTgzZDExNmE3NzAyMTAyNDdmMjUwODI2NQ==
        if (this.authorization == null){
            throw new AccessControlException(new ErrorResponse(NIP_121));
        }
        String auth = StringUtils.substringAfter(this.authorization, "Basic ");
        try{
            auth = new String(Base64.getDecoder().decode(auth.trim()));
        }catch (Exception ex){
            throw new AccessControlException(new ErrorResponse(NIP_122));
        }

        String[] token = StringUtils.split(auth, ":");
        if (token.length != 2){
            throw new AccessControlException(new ErrorResponse(NIP_123));
        }
        this.username = token[0];
        this.password = token[1];

    }


}
