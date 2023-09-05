package com.vvkozlov.emerchantpay.merchant.infra.kafka;

import com.vvkozlov.emerchantpay.merchant.service.contract.mb.MessageBrokerConsumer;
import com.vvkozlov.emerchantpay.merchant.service.contract.service.MerchantMessageBrokerService;
import com.vvkozlov.emerchantpay.merchant.service.model.messagebroker.TransactionMbModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer implements MessageBrokerConsumer {
    private final MerchantMessageBrokerService merchantMbService;

    public KafkaConsumer(MerchantMessageBrokerService merchantMbService) {
        this.merchantMbService = merchantMbService;
    }

    @KafkaListener(topics = "transaction_topic", groupId = "merchant_microservice")
    public TransactionMbModel consumeMessage(TransactionMbModel transactionInfo) {
        merchantMbService.addTransactionAmountToMerchant(transactionInfo);

        System.out.println("Received merchant with ID: "
                + transactionInfo.getBelongsTo() + " and state: " + transactionInfo.getAmount());
        return transactionInfo;
    }
}
