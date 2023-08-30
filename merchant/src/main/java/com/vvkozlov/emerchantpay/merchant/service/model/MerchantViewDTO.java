package com.vvkozlov.emerchantpay.merchant.service.model;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Use this DTO to view merchant data (get)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MerchantViewDTO extends BaseUserViewDTO {
    private MerchantStatusEnum status;
    private BigDecimal totalTransactionSum;
}
