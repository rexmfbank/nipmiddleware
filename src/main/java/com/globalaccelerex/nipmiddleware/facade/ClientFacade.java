package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.ClientMapper;
import com.globalaccelerex.nipmiddleware.payload.client.UpdateClientPasswordRequest;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.exception.ErrorMessage.*;

@Slf4j
@Service
public class ClientFacade {

    private final ClientDbService clientDbService;

    private final JwtTokenUtil jwtTokenUtil;

    private final ClientMapper clientMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ClientFacade(ClientDbService clientDbService,JwtTokenUtil jwtTokenUtil, ClientMapper clientMapper) {
        this.clientDbService = clientDbService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientMapper = clientMapper;
    }


    public void updateClientPassword(UpdateClientPasswordRequest updateClientPasswordRequest){
        IMarker marker = updateClientPasswordRequest.getMarker();
        marker.info("processing update client password request ");

        val clientEntityOpt = clientDbService.isClientPresent(updateClientPasswordRequest.getClientId());
        if(!clientEntityOpt.isPresent()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(CLIENT_NOT_FOUND_MSG,marker);
            throw nipMiddleWareAPIException;
        }

        if(!updateClientPasswordRequest.isNewPasswordAndConfirmPasswordEqual()){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(NEW_AND_CONFIRM_PASSWORD_NOT_SAME_MSG,marker);
            throw nipMiddleWareAPIException;
        }

        val clientEntity = clientEntityOpt.get();
        if(!bCryptPasswordEncoder.matches(updateClientPasswordRequest.getOldPassword(), clientEntity.getPassword())){
            val nipMiddleWareAPIException = new NIPMiddleWareAPIException();
            nipMiddleWareAPIException.buildFailureStatusException(PASSWORD_MATCH_ERROR_MSG,marker);
            throw nipMiddleWareAPIException;
        }

        clientEntity.setPassword(bCryptPasswordEncoder.encode(updateClientPasswordRequest.getNewPassword()));

        clientDbService.updateClientEntity(clientEntity);

        marker.info("done processing update client password request ");
    }

}
