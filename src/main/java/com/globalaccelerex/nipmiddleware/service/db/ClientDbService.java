package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;
import com.globalaccelerex.nipmiddleware.repository.ClientRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ClientDbService {

    int DEFAULT_LIST_SIZE = 50;

    private final ClientRepository clientRepository;

    @Autowired
    public ClientDbService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<ClientEntity> isClientPresent(String clientId ){
        return clientRepository.findByClientId(clientId);

    }

    public Page<ClientEntity> findClients(int records , String startWith , int pageIndex){
       int size = (records > 0) ? records : DEFAULT_LIST_SIZE;
        val pageRequest = PageRequest.of(pageIndex, size,Sort.by("clientId").ascending());
        if(StringUtils.isBlank(startWith)){
           return clientRepository.findAll(pageRequest);
        }else{
            return clientRepository.findAllByClientIdStartingWith(startWith, pageRequest);
        }
    }

    public Optional<ClientEntity> isClientNamePresent(String clientName){
        return clientRepository.findFirstByClientName(clientName);
    }
    public void saveClientEntity(ClientEntity clientEntity){
        clientRepository.save(clientEntity);
    }

    public void updateClientEntity(ClientEntity clientEntity){
        clientRepository.save(clientEntity);
        clientCache.invalidate(clientEntity.getClientId());
    }

    public ClientEntity findClientByClientId(String clientId ){
        try{
            return clientCache.getUnchecked(clientId);
        }catch (UncheckedExecutionException exception){
            return null;
        }
    }

    private LoadingCache<String, ClientEntity> clientCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, ClientEntity>() {
                @Override
                public ClientEntity load(String clientId) {
                    return clientRepository.findByClientId(clientId).get();
                }
            });
}
