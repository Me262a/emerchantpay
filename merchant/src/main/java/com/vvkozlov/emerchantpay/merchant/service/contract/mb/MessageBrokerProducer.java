package com.vvkozlov.emerchantpay.merchant.service.contract.mb;

public interface MessageBrokerProducer {
    public void sendMessage(String id, boolean isActive);
}
