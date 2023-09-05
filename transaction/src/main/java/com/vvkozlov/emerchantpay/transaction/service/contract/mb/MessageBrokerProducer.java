package com.vvkozlov.emerchantpay.transaction.service.contract.mb;

import java.math.BigDecimal;

public interface MessageBrokerProducer {
    public void sendMessage(String belongsTo, BigDecimal amount);
}
