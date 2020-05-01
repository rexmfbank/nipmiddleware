package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.mapper.ClientMapper;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.ClientDetail;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientResponse;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.*;

@Slf4j
@Service
public class ClientFacade {

    private final ClientDbService clientDbService;

    private final JwtTokenUtil jwtTokenUtil;

    private final ClientMapper clientMapper;

    @Autowired
    public ClientFacade(ClientDbService clientDbService,JwtTokenUtil jwtTokenUtil, ClientMapper clientMapper) {
        this.clientDbService = clientDbService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientMapper = clientMapper;
    }

    public CreateClientResponse createClient(CreateClientRequest createClientRequest){
        final val iMarker = createClientRequest.getMarker();
        iMarker.info("::::::: Handling Create Client ::::::: ");
        final val clientName = createClientRequest.getClientName();
        final val clientId = createClientRequest.getClientId();

        if(clientDbService.isClientPresent(clientId).isPresent()){
            throw new NIPMiddleWareAPIException(NIP_114,iMarker);
        }
        final val clientEntity = clientMapper.mapClientEntity.apply(createClientRequest);
        clientDbService.saveClientEntity(clientEntity);

        final val jwtTokenStr = jwtTokenUtil.createJWT(clientId, "NIP", "X_TOKEN", 0);
        final val createClientResponse = clientMapper.mapCreateClientResponse.apply(createClientRequest);
        createClientResponse.setSecretKey(jwtTokenStr);
        return createClientResponse;
    }

    public ClientDetail getClientDetail(String clientId , IMarker iMarker){
        final val clientEntityOpt = clientDbService.isClientPresent(clientId);
        if(clientEntityOpt.isPresent()){
            final val clientDetail = clientMapper.mapClientDetail.apply(clientEntityOpt.get());
            clientDetail.setResponse(NIP_00);
            return clientDetail;
        }else{
            throw new NIPMiddleWareAPIException(NIP_124,iMarker);
        }
    }

}
