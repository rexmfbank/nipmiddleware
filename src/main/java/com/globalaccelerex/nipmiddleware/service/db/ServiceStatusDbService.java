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

    public void updateServiceStatus(String status){
        val serviceStatusEntity = findStatus();
        serviceStatusEntity.setStatus(status);
        serviceStatusRepository.save(serviceStatusEntity);
        serviceStatusCache.invalidate(serviceStatusEntity.getId());
    }
    public ServiceStatusEntity findStatus(){
        return serviceStatusCache.getUnchecked(1);
    }

    private final LoadingCache<Integer, ServiceStatusEntity> serviceStatusCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5,TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<Integer, ServiceStatusEntity>() {
        @Override
        public ServiceStatusEntity load(Integer id) {
            return serviceStatusRepository.findById(id).get();
        }
    });
}
