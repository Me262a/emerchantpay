package com.vvkozlov.emerchantpay.merchant.infra.repository;

import com.vvkozlov.emerchantpay.merchant.domain.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
}