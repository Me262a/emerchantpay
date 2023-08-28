package com.vvkozlov.emerchantpay.merchant.infra.kafka;

import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.contract.MessageBrokerConsumer;
import com.vvkozlov.emerchantpay.merchant.service.model.messagebroker.TransactionMbModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer implements MessageBrokerConsumer {
    private final MerchantService merchantService;

    public KafkaConsumer(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @KafkaListener(topics = "transaction_topic", groupId = "merchant_microservice")
    public TransactionMbModel consumeMessage(TransactionMbModel transactionInfo) {
        merchantService.addTransactionAmountToMerchant(transactionInfo);

        System.out.println("Received merchant with ID: "
                + transactionInfo.getBelongsTo() + " and state: " + transactionInfo.getAmount());
        return transactionInfo;
    }
}
