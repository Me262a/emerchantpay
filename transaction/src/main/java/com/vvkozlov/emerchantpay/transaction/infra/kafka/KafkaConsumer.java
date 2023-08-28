package com.vvkozlov.emerchantpay.transaction.infra.kafka;

import com.vvkozlov.emerchantpay.transaction.service.contract.MessageBrokerConsumer;
import com.vvkozlov.emerchantpay.transaction.service.model.messagebroker.MerchantMbModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer implements MessageBrokerConsumer {
    @KafkaListener(topics = "merchant_user_topic", groupId = "transaction_microservice")
    public MerchantMbModel consumeMessage(MerchantMbModel merchantInfo) {
        System.out.println("Received merchant with ID: "
                + merchantInfo.getId() + " and state: " + merchantInfo.isActive());

        //Do smth - Handle merchant transactions in this case

        return merchantInfo;
    }
}
