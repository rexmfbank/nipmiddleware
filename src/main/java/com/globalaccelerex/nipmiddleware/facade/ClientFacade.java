package com.globalaccelerex.nipmiddleware.facade;

import com.globalaccelerex.nipmiddleware.exception.NIPMiddleWareAPIException;
import com.globalaccelerex.nipmiddleware.mapper.UtilMapper;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientResponse;
import com.globalaccelerex.nipmiddleware.service.db.ClientDbService;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_114;

@Slf4j
@Service
public class ClientFacade {

    private final ClientDbService clientDbService;

    private final UtilMapper utilMapper;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ClientFacade(ClientDbService clientDbService, UtilMapper utilMapper, JwtTokenUtil jwtTokenUtil) {
        this.clientDbService = clientDbService;
        this.utilMapper = utilMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public CreateClientResponse createClient(CreateClientRequest createClientRequest){
        final val iMarker = createClientRequest.getMarker();
        iMarker.info("::::::: Handling Create Client ::::::: ");
        final val clientName = createClientRequest.getClientName();
        final val clientId = createClientRequest.getClientId();
        final val output = clientDbService.findClientByClientIdOrClientName(clientId, clientName);
        if(output){
            throw new NIPMiddleWareAPIException(NIP_114);
        }
        final val clientEntity = utilMapper.mapClientEntity.apply(createClientRequest);
        clientDbService.saveClientEntity(clientEntity);

        final val jwtTokenStr = jwtTokenUtil.createJWT(clientId, "NIP", "X_TOKEN", 0);
        iMarker.info("::::::: JwtToken :::: " + jwtTokenStr);

        final val createClientResponse = new CreateClientResponse();
        createClientResponse.setClientId(createClientRequest.getClientId());
        createClientResponse.setSecretKey(jwtTokenStr);
        return createClientResponse;
    }
}
