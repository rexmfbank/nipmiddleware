package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.ClientMapper;
import com.globalaccelerex.nipmiddleware.payload.client.*;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

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
            throw new NIPMiddleWareAPIException(NIP_124,marker);
        }

        if(!updateClientPasswordRequest.isNewPasswordAndConfirmPasswordEqual()){
            throw new NIPMiddleWareAPIException(NIP_128 , marker);
        }

        val clientEntity = clientEntityOpt.get();
        if(!bCryptPasswordEncoder.matches(updateClientPasswordRequest.getOldPassword(), clientEntity.getPassword())){
            throw new NIPMiddleWareAPIException(NIP_129 , marker);
        }

        clientEntity.setPassword(bCryptPasswordEncoder.encode(updateClientPasswordRequest.getNewPassword()));

        clientDbService.updateClientEntity(clientEntity);

        marker.info("done processing update client password request ");
    }


}
