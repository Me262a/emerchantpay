package com.vvkozlov.emerchantpay.transaction.infra.repository;

import com.vvkozlov.emerchantpay.transaction.domain.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.Date;

public interface TransactionRepository extends JpaRepository<AbstractTransaction, UUID> {
    Optional<AuthorizeTransaction> findAuthorizeTransactionByUuid(UUID uuid);
    Optional<ChargeTransaction> findChargeTransactionByUuid(UUID uuid);
    Optional<RefundTransaction> findRefundTransactionByUuid(UUID uuid);
    Optional<ReversalTransaction> findReversalTransactionByUuid(UUID uuid);
    @Modifying
    @Transactional
    void deleteByDateCreatedBefore(Date date);
}