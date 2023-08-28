package com.vvkozlov.emerchantpay.merchant.infra.kafka;

import com.vvkozlov.emerchantpay.merchant.service.contract.MessageBrokerProducer;
import com.vvkozlov.emerchantpay.merchant.service.model.messagebroker.MerchantMbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer implements MessageBrokerProducer {

    private final KafkaTemplate<String, MerchantMbModel> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, MerchantMbModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String id, boolean isActive) {
        MerchantMbModel transaction = new MerchantMbModel(id, isActive);
        kafkaTemplate.send("merchant_user_topic", transaction);
    }
}
