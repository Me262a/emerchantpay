package com.vvkozlov.emerchantpay.transaction.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Use this DTO to edit transaction data (post/put)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AuthorizeTransactionCreateDTO extends AbstractTransactionCreateDTO {
    @Schema(name = "customerEmail", example = "customer@mail.io", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerEmail;

    @Schema(name = "customerPhone", example = "+1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerPhone;

    @Schema(name = "amount", example = "100.500", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @JsonIgnore //this is not required for authorize transaction
    private UUID referenceId;
}
