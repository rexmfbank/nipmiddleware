package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.repository.ClientRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientDbService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientDbService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public boolean findClientByClientIdOrClientName(String clientId , String clientName){
        final val optClientEntity = clientRepository.findByClientIdOrClientName(clientId, clientName);
        return optClientEntity.isPresent();
    }

    public void saveClientEntity(ClientEntity clientEntity){
        clientRepository.save(clientEntity);
    }
}
