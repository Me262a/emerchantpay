package com.vvkozlov.emerchantpay.merchant.service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Use this DTO to view merchant data (get)
 */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminViewDTO extends BaseUserViewDTO {
}
