package com.vvkozlov.emerchantpay.transaction.infra.repository;

import com.vvkozlov.emerchantpay.transaction.domain.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<AbstractTransaction, UUID> {
    Optional<AbstractTransaction> findByUuidAndBelongsTo(UUID id, String belongsTo);
    Page<AbstractTransaction> findAllByBelongsTo(String belongsTo, Pageable pageable);
    @Modifying
    @Transactional
    void deleteByDateCreatedBefore(Date date);
    long countByBelongsTo(String merchantId);
}