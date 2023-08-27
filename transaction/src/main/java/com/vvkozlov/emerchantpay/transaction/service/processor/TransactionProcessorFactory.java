package com.vvkozlov.emerchantpay.transaction.service.processor;

import com.vvkozlov.emerchantpay.transaction.service.model.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TransactionProcessorFactory {
    private final ApplicationContext applicationContext;

    public TransactionProcessorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public TransactionProcessor<? extends AbstractTransactionCreateDTO> getProcessor(AbstractTransactionCreateDTO dto) {
        if (dto instanceof AuthorizeTransactionCreateDTO) {
            return applicationContext.getBean(AuthorizeTransactionProcessor.class);
        } else if (dto instanceof ChargeTransactionCreateDTO) {
            return applicationContext.getBean(ChargeTransactionProcessor.class);
        } else if (dto instanceof RefundTransactionCreateDTO) {
            return applicationContext.getBean(RefundTransactionProcessor.class);
        } else if (dto instanceof ReversalTransactionCreateDTO) {
            return applicationContext.getBean(ReversalTransactionProcessor.class);
        }
        return null;
    }
}