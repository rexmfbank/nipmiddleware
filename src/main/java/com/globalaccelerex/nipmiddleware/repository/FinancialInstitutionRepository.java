package com.globalaccelerex.nipmiddleware.repository;

import com.globalaccelerex.nipmiddleware.entity.FinancialInstitutionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialInstitutionRepository extends PagingAndSortingRepository<FinancialInstitutionEntity,Integer> {

    List<FinancialInstitutionEntity> findByOrderByInstitutionNameAsc();
}
