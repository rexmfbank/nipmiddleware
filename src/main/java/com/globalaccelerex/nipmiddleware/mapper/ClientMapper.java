package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.payload.client.ClientDetail;
import com.globalaccelerex.nipmiddleware.payload.client.CreateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.CreateClientResponse;
import com.globalaccelerex.nipmiddleware.payload.client.UpdateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.outward.nameenquiry.NESingleRequest;
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

    public Function<ClientEntity, ClientDetail> mapClientDetail = clientEntity ->  {
        final val clientDetail = new ClientDetail();
        clientDetail.setActive(clientEntity.isActive());
        clientDetail.setBusinessDesc(clientEntity.getBusinessDesc());
        clientDetail.setCallbackUrl(clientEntity.getCallbackUrl());
        clientDetail.setClientId(clientEntity.getClientId());
        clientDetail.setClientName(clientEntity.getClientName());
        clientDetail.setContactEmail(clientEntity.getContactEmail());
        clientDetail.setContactPhone(clientEntity.getContactPhone());
        clientDetail.setResponse(NIP_00);
        return clientDetail;
    };

    public Function<CreateClientRequest , NESingleRequest> mapNESingleRequest =  createClientRequest ->
            NESingleRequest.builder()
            .accountNo(createClientRequest.getAccountNo())
            .destinationBankCode(createClientRequest.getBankCode())
            .originatorBankCode(createClientRequest.getOriginatorBankCode())
            .build();

    public Function<UpdateClientRequest , NESingleRequest> mapNESingleRequest_1 = updateClientRequest -> NESingleRequest.builder()
            .accountNo(updateClientRequest.getAccountNo())
            .destinationBankCode(updateClientRequest.getBankCode())
            .originatorBankCode(updateClientRequest.getOriginatorBankCode())
            .build();


    public ClientEntity updateClientEntity(ClientEntity clientEntity , UpdateClientRequest updateClientRequest){
        if(updateClientRequest.getActive() != null){
            clientEntity.setActive(updateClientRequest.getActive());
        }
        clientEntity.setBusinessDesc(updateClientRequest.getBusinessDesc());
        clientEntity.setCallbackUrl(updateClientRequest.getCallbackUrl());
        clientEntity.setClientName(updateClientRequest.getClientName());
        clientEntity.setContactEmail(updateClientRequest.getContactEmail());
        clientEntity.setContactPhone(updateClientRequest.getContactPhone());
        return clientEntity;

    }





}
