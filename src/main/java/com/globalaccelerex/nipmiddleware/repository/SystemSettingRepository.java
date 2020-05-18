package com.globalaccelerex.nipmiddleware.repository;

import com.globalaccelerex.nipmiddleware.entity.SystemSettingEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemSettingRepository extends PagingAndSortingRepository<SystemSettingEntity,Integer> {

    SystemSettingEntity findFirstByName(String name);
}
