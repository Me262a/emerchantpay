package com.vvkozlov.emerchantpay.merchant.infra.repository;

import com.vvkozlov.emerchantpay.merchant.domain.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByAuthId(String authId);

    void deleteByAuthId(String authId);
    void deleteByAuthIdIn(List<String> authIds);
}