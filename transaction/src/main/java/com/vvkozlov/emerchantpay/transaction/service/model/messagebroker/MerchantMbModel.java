package com.vvkozlov.emerchantpay.transaction.service.model.messagebroker;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantMbModel {
    String id;
    boolean isActive;
}
