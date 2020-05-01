package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.ClientDetail;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientResponse;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_00;

@Service
public class ClientMapper {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ClientMapper(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Function<CreateClientRequest, ClientEntity> mapClientEntity = createClientRequest ->
            ClientEntity.builder()
                    .active(true)
                    .businessDesc(createClientRequest.getBusinessDesc())
                    .callbackUrl(createClientRequest.getCallbackUrl())
                    .clientId(createClientRequest.getClientId())
                    .clientName(createClientRequest.getClientName())
                    .contactEmail(createClientRequest.getContactEmail())
                    .contactPhone(createClientRequest.getContactPhone())
                    .password(bCryptPasswordEncoder.encode(createClientRequest.getClientPassword()))
                    .build();

    public Function<CreateClientRequest , CreateClientResponse> mapCreateClientResponse = createClientRequest -> {
        final val createClientResponse = new CreateClientResponse(NIP_00);
        createClientResponse.setClientId(createClientRequest.getClientId());
        createClientResponse.setClientName(createClientRequest.getClientName());
        createClientResponse.setContactEmail(createClientRequest.getContactEmail());
        createClientResponse.setContactPhone(createClientRequest.getContactPhone());
        createClientResponse.setBusinessDesc(createClientRequest.getBusinessDesc());
        createClientResponse.setCallbackUrl(createClientRequest.getCallbackUrl());
        return createClientResponse;
    };

    public Function<ClientEntity, ClientDetail> mapClientDetail = clientEntity ->  ClientDetail.builder()
                .active(clientEntity.isActive())
                .businessDesc(clientEntity.getBusinessDesc())
                .callbackUrl(clientEntity.getCallbackUrl())
                .clientId(clientEntity.getClientId())
                .clientName(clientEntity.getClientName())
                .contactEmail(clientEntity.getContactEmail())
                .contactPhone(clientEntity.getContactPhone())
                .build();



}
