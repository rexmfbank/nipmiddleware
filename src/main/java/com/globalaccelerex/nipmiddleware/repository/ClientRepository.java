package com.globalaccelerex.nipmiddleware.repository;

import com.globalaccelerex.nipmiddleware.entity.ClientEntity;

import com.google.inject.internal.util.$Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends PagingAndSortingRepository<ClientEntity,Integer> {

    Optional<ClientEntity> findByClientId(String clientId);

    Optional<ClientEntity> findFirstByClientName(String clientName);

    Page<ClientEntity> findAll(Pageable pageable);

    Page<ClientEntity> findAllByClientIdStartingWith(String startWith ,Pageable pageable);
}
