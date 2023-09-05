package com.vvkozlov.emerchantpay.transaction.service.contract.mb;

import com.vvkozlov.emerchantpay.transaction.service.model.messagebroker.MerchantMbModel;

public interface MessageBrokerConsumer {
    public MerchantMbModel consumeMessage(MerchantMbModel merchantInfo);
}
