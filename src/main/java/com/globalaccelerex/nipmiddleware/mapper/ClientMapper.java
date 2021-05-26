package com.globalaccelerex.nipmiddleware.mapper;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.enums.ClientStatusEnum;
import com.globalaccelerex.nipmiddleware.payload.client.createClient.CreateClientRequest;
import com.globalaccelerex.nipmiddleware.payload.client.createClient.CreateClientResponse;
import com.globalaccelerex.nipmiddleware.payload.client.getclients.ClientDetail;
import com.globalaccelerex.nipmiddleware.payload.client.nameenquiry.NESingleRequest;
import com.globalaccelerex.nipmiddleware.payload.client.updateclient.UpdateClientRequest;
import com.globalaccelerex.nipmiddleware.util.JwtTokenUtil;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
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

    public Function<CreateClientRequest, ClientEntity> mapClientEntity = createClientRequest -> {
        val clientEntity = ClientEntity.builder()
                .active(true)
                .businessDesc(createClientRequest.getBusinessDesc())
                .callbackUrl(createClientRequest.getCallbackUrl())
                .clientId(createClientRequest.getClientId())
                .clientName(createClientRequest.getClientName())
                .contactEmail(createClientRequest.getContactEmail())
                .contactPhone(createClientRequest.getContactPhone())
                .password(bCryptPasswordEncoder.encode(createClientRequest.getClientPassword()))
                .longitude(String.valueOf(createClientRequest.getLongitude()))
                .build();
        if(createClientRequest.getLatitude() == null){
            clientEntity.setLatitude("2.2");
        }else{
            clientEntity.setLatitude(String.valueOf(createClientRequest.getLatitude()));
        }
        if(createClientRequest.getLongitude() == null){
            clientEntity.setLongitude("3.3");
        }else{
            clientEntity.setLongitude(String.valueOf(createClientRequest.getLongitude()));
        }
        return clientEntity;
    };


    public Function<CreateClientRequest , CreateClientResponse> mapCreateClientResponse = createClientRequest -> {
        final val createClientResponse = new CreateClientResponse(NIP_00);
        createClientResponse.setClientId(createClientRequest.getClientId());
        createClientResponse.setClientName(createClientRequest.getClientName());
        createClientResponse.setContactEmail(createClientRequest.getContactEmail());
        createClientResponse.setContactPhone(createClientRequest.getContactPhone());
        createClientResponse.setBusinessDesc(createClientRequest.getBusinessDesc());
        createClientResponse.setCallbackUrl(createClientRequest.getCallbackUrl());

        if(createClientRequest.getLatitude() == null){
            createClientResponse.setLatitude("2.2");
        }else{
            createClientResponse.setLatitude(String.valueOf(createClientRequest.getLatitude()));
        }
        if(createClientRequest.getLongitude() == null){
            createClientResponse.setLongitude("3.3");
        }else{
            createClientResponse.setLongitude(String.valueOf(createClientRequest.getLongitude()));
        }
        createClientResponse.setStatus(ClientStatusEnum.ACTIVE);
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
        clientDetail.setLatitude(clientEntity.getLatitude());
        clientDetail.setLongitude(clientEntity.getLongitude());
        clientDetail.setBankCode(clientEntity.getBankCode());
        clientDetail.setAccountNo(clientEntity.getAccountNo());
        clientDetail.setAccountName(clientEntity.getAccountName());
        clientDetail.setBvn(clientEntity.getBvn());
        clientDetail.setKycLevel(clientEntity.getKycLevel());
        clientDetail.setResponse(NIP_00);
        clientDetail.setStatus(clientEntity.getClientStatus());
        return clientDetail;
    };

    public Function<CreateClientRequest , NESingleRequest> mapNESingleRequest =  createClientRequest ->
            NESingleRequest.builder()
            .accountNo(createClientRequest.getAccountDetail().getAccountNo())
            .destinationBankCode(createClientRequest.getAccountDetail().getBankCode())
            .originatorBankCode(createClientRequest.getOriginatorBankCode())
            .build();

    public Function<UpdateClientRequest , NESingleRequest> mapNESingleRequest_1 = updateClientRequest -> NESingleRequest.builder()
            .accountNo(updateClientRequest.getAccountDetail().getAccountNo())
            .destinationBankCode(updateClientRequest.getAccountDetail().getBankCode())
            .originatorBankCode(updateClientRequest.getOriginatorBankCode())
            .build();


    public ClientEntity updateClientEntity(ClientEntity clientEntity , UpdateClientRequest updateClientRequest){
        if(updateClientRequest.getActive() != null){
            clientEntity.setActive(updateClientRequest.getActive());
        }
        if(StringUtils.isNotBlank(updateClientRequest.getBusinessDesc())){
            clientEntity.setBusinessDesc(updateClientRequest.getBusinessDesc());
        }
        if(StringUtils.isNotBlank(updateClientRequest.getCallbackUrl())){
            clientEntity.setCallbackUrl(updateClientRequest.getCallbackUrl());
        }

        if(StringUtils.isNotBlank(updateClientRequest.getClientName())){
            clientEntity.setClientName(updateClientRequest.getClientName());
        }

        if(StringUtils.isNotBlank(updateClientRequest.getContactEmail())){
            clientEntity.setContactEmail(updateClientRequest.getContactEmail());
        }
        if(StringUtils.isNotBlank(updateClientRequest.getContactPhone())){
            clientEntity.setContactPhone(updateClientRequest.getContactPhone());
        }
        if(updateClientRequest.getLatitude() != null){
            clientEntity.setLatitude(String.valueOf(updateClientRequest.getLatitude()));
        }
        if(updateClientRequest.getLongitude() != null){
            clientEntity.setLongitude(String.valueOf(updateClientRequest.getLongitude()));
        }

        return clientEntity;

    }





}
