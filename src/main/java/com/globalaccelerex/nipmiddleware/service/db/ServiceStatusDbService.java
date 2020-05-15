package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.ServiceStatusEntity;
import com.globalaccelerex.nipmiddleware.repository.ServiceStatusRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ServiceStatusDbService {

    private final ServiceStatusRepository serviceStatusRepository;

    @Autowired
    public ServiceStatusDbService(ServiceStatusRepository serviceStatusRepository) {
        this.serviceStatusRepository = serviceStatusRepository;
    }

    public void updateServiceStatus(String name, String value){
        val serviceStatusEntity = findStatus(name);
        serviceStatusEntity.setValue(value);
        serviceStatusRepository.save(serviceStatusEntity);
        serviceStatusCache.invalidate(serviceStatusEntity.getId());
    }
    public ServiceStatusEntity findStatus(String name){
        return serviceStatusCache.getUnchecked(name);
    }

    private final LoadingCache<String, ServiceStatusEntity> serviceStatusCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5,TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, ServiceStatusEntity>() {
        @Override
        public ServiceStatusEntity load(String name) {
            return serviceStatusRepository.findFirstByName(name);
        }
    });
}
