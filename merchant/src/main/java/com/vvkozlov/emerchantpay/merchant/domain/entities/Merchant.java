package com.vvkozlov.emerchantpay.merchant.domain.entities;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "merchants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Merchant {
    /**
     * Internal db id, not used
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long id;

    /**
     * Id for this user received from external authentication server (usually oauth UUID)
     */
    @Column(name = "auth_id")
    private String authId;
    private String name;
    private String description;
    private String email;

    @Enumerated(EnumType.STRING)
    private MerchantStatusEnum status;

    @Column(name = "total_transaction_sum")
    private Double totalTransactionSum;
}