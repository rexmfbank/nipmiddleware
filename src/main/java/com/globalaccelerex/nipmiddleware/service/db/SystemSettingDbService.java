package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.SystemSettingEntity;
import com.globalaccelerex.nipmiddleware.repository.SystemSettingRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SystemSettingDbService {

    private final SystemSettingRepository systemSettingRepository;

    @Autowired
    public SystemSettingDbService(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }

    public void updateServiceStatus(String name, String value){
        val serviceStatusEntity = findStatus(name);
        serviceStatusEntity.setValue(value);
        systemSettingRepository.save(serviceStatusEntity);
        serviceStatusCache.invalidate(serviceStatusEntity.getId());
    }
    public SystemSettingEntity findStatus(String name){
        return serviceStatusCache.getUnchecked(name);
    }

    private final LoadingCache<String, SystemSettingEntity> serviceStatusCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5,TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, SystemSettingEntity>() {
        @Override
        public SystemSettingEntity load(String name) {
            return systemSettingRepository.findFirstByName(name);
        }
    });
}
