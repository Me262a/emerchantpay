package com.vvkozlov.emerchantpay.transaction.service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Use this DTO to edit transaction data (post/put)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ChargeTransactionCreateDTO extends AbstractTransactionCreateDTO {
    //In real app can include customer phone or email,
    //since some payment providers accept charge transactions without authorise transaction

    @Schema(name = "amount", example = "100.500", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
}
