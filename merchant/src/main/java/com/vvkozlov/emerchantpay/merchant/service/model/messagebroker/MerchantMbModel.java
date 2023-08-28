package com.vvkozlov.emerchantpay.merchant.service.model.messagebroker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantMbModel {
    String id;
    boolean isActive;
}
