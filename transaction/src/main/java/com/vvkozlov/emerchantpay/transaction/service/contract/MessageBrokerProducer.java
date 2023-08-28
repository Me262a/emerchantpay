package com.vvkozlov.emerchantpay.transaction.service.contract;

import java.math.BigDecimal;

public interface MessageBrokerProducer {
    public void sendMessage(String belongsTo, BigDecimal amount);
}
