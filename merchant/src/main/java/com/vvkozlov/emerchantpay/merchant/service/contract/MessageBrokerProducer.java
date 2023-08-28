package com.vvkozlov.emerchantpay.merchant.service.contract;

import java.math.BigDecimal;

public interface MessageBrokerProducer {
    public void sendMessage(String id, boolean isActive);
}
