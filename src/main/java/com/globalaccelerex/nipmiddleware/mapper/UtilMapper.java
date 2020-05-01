package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.outward.client.CreateClientResponse;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class UtilMapper {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UtilMapper(JwtTokenUtil jwtTokenUtil) {
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




}
