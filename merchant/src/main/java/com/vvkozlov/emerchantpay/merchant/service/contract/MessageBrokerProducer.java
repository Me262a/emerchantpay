package com.vvkozlov.emerchantpay.merchant.service.contract;

public interface MessageBrokerProducer {
    public void sendMessage(String id, boolean isActive);
}
