package com.vvkozlov.emerchantpay.transaction.infra.kafka;

import com.vvkozlov.emerchantpay.transaction.service.contract.MessageBrokerProducer;
import com.vvkozlov.emerchantpay.transaction.service.model.messagebroker.TransactionMbModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaProducer implements MessageBrokerProducer {

    @Autowired
    private KafkaTemplate<String, TransactionMbModel> kafkaTemplate;

    public void sendMessage(String belongsTo, BigDecimal amount) {
        TransactionMbModel transaction = new TransactionMbModel(belongsTo, amount);
        kafkaTemplate.send("transaction_topic", transaction);
    }
}
