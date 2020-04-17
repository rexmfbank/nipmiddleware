package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.exception.ErrorResponse;
import com.globalaccelerex.nipmiddleware.repository.ClientRepository;
import com.globalaccelerex.nipmiddleware.security.AccessControlException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.globalaccelerex.nipmiddleware.enums.NIPResponseCodeEnum.NIP_116;

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

    public ClientEntity findClientByClientId(String clientId ){
        try{
            return clientCache.getUnchecked(clientId);
        }catch (UncheckedExecutionException exception){
            if (AccessControlException.class.isInstance(exception.getCause())) {
                throw (AccessControlException) exception.getCause();
            }
        }
        return null;
    }

    private LoadingCache<String, ClientEntity> clientCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, ClientEntity>() {
                @Override
                public ClientEntity load(String clientId) {
                    return findClientByClientId(clientId);
                }
            });
}
