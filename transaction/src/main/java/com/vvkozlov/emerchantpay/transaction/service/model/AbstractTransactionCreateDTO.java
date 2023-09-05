package com.vvkozlov.emerchantpay.transaction.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "transactionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthorizeTransactionCreateDTO.class, name = "AUTHORIZE"),
        @JsonSubTypes.Type(value = ChargeTransactionCreateDTO.class, name = "CHARGE"),
        @JsonSubTypes.Type(value = RefundTransactionCreateDTO.class, name = "REFUND"),
        @JsonSubTypes.Type(value = ReversalTransactionCreateDTO.class, name = "REVERSAL")
})
public class AbstractTransactionCreateDTO {
    @JsonIgnore //will be set from Merchant's JWT
    private String belongsTo;
    /**
     * May refer to previous transactions
     * */
    @Schema(name = "referenceId", example = "UUID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID referenceId;
}
