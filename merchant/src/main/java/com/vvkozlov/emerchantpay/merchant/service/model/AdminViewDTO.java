package com.vvkozlov.emerchantpay.merchant.service.model;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import lombok.*;

import java.util.UUID;

/**
 * Use this DTO to view merchant data (get)
 */
@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AdminViewDTO extends BaseUserViewDTO {
}
