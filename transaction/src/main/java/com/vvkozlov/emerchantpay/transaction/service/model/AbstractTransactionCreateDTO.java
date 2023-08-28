package com.vvkozlov.emerchantpay.transaction.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Use this DTO to edit transaction data (post/put)
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class AbstractTransactionCreateDTO {
    @JsonIgnore //will be set from Merchant's JWT
    private String belongsTo;
    /**
     * May refer to previous transactions
     * */
    @Schema(name = "referenceId", example = "UUID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID referenceId;
}
