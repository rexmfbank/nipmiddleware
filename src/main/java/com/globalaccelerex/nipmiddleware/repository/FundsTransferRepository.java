package com.globalaccelerex.nipmiddleware.repository;

import com.globalaccelerex.nipmiddleware.entity.FundsTransferEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface FundsTransferRepository extends PagingAndSortingRepository<FundsTransferEntity,Integer> {

    Optional<FundsTransferEntity> findBySessionIdAndClientId(String sessionId, String clientId);

    Optional<FundsTransferEntity> findByClientIdAndPaymentReference(String clientId, String paymentReference);

    Optional<FundsTransferEntity>
    findByClientIdAndPaymentReferenceAndSessionId(String clientId, String paymentReference, String sessionId);
}
