package com.globalaccelerex.nipmiddleware.repository;

import com.globalaccelerex.nipmiddleware.entity.ServiceStatusEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceStatusRepository extends PagingAndSortingRepository<ServiceStatusEntity,Integer> {


}
