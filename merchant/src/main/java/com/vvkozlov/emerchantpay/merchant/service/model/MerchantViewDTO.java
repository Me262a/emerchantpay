package com.vvkozlov.emerchantpay.merchant.service.model;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import lombok.*;

import java.util.UUID;

/**
 * Use this DTO to view merchant data (get)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantViewDTO {
    private UUID id;
    private String name;
    private String description;
    private String email;
    private MerchantStatusEnum status;
}
