package com.vvkozlov.emerchantpay.merchant.infra.repository;

import com.vvkozlov.emerchantpay.merchant.domain.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByAuthId(String authId);
    @Modifying
    @Transactional
    void deleteByAuthId(String authId);
    @Modifying
    @Transactional
    void deleteByAuthIdIn(List<String> authIds);
}