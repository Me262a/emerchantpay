package com.vvkozlov.emerchantpay.transaction.service.model.messagebroker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionMbModel {
    private String belongsTo;
    private BigDecimal amount;
}
