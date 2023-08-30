package com.vvkozlov.emerchantpay.transaction.service.model;

import com.vvkozlov.emerchantpay.transaction.domain.constants.TransactionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Use this DTO to view transaction data (get)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionViewDTO {
    private UUID uuid;
    private String belongsTo;
    private TransactionStatusEnum status;
    private UUID referenceId;
    private String customerEmail;
    private String customerPhone;
    private BigDecimal amount;
    private String transactionType;
}
