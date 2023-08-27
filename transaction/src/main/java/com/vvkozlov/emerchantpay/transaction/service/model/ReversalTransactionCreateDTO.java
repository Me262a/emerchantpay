package com.vvkozlov.emerchantpay.transaction.service.model;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Use this DTO to edit transaction data (post/put)
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ReversalTransactionCreateDTO extends AbstractTransactionCreateDTO {
}
