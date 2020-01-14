package com.globalaccelerex.nipmiddleware.repository;

import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundsTransferRepository extends PagingAndSortingRepository<FundsTransferEntity,Integer> {

    Optional<FundsTransferEntity> findBySessionId(String sessionId);
}
