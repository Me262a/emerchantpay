package com.vvkozlov.emerchantpay.transaction.infra.repository;

import com.vvkozlov.emerchantpay.transaction.domain.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<AbstractTransaction, UUID> {
    Optional<AuthorizeTransaction> findAuthorizeTransactionByUuid(UUID uuid);
    Optional<ChargeTransaction> findChargeTransactionByUuid(UUID uuid);
    Optional<RefundTransaction> findRefundTransactionByUuid(UUID uuid);
    Optional<ReversalTransaction> findReversalTransactionByUuid(UUID uuid);
}