package com.vvkozlov.emerchantpay.transaction.service.processor;

import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AuthorizeTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.ChargeTransaction;
import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import com.vvkozlov.emerchantpay.transaction.service.contract.mb.MessageBrokerProducer;
import com.vvkozlov.emerchantpay.transaction.service.mapper.TransactionMapper;
import com.vvkozlov.emerchantpay.transaction.service.model.ChargeTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.validator.ChargeTransactionValidator;
import com.vvkozlov.emerchantpay.transaction.service.validator.ValidationResults;
import org.springframework.stereotype.Service;

@Service
public class ChargeTransactionProcessor extends AbstractTransactionProcessor<ChargeTransactionCreateDTO, ChargeTransaction> {

    private final ChargeTransactionValidator validator;
    public final MessageBrokerProducer mbProducer;

    public ChargeTransactionProcessor(ChargeTransactionValidator validator, TransactionRepository transactionRepository, MessageBrokerProducer mbProducer) {
        super(transactionRepository);
        this.validator = validator;
        this.mbProducer = mbProducer;
    }

    @Override
    protected ChargeTransaction convertDtoToEntity(ChargeTransactionCreateDTO createDto) {
        return TransactionMapper.INSTANCE.toEntity(createDto);
    }

    @Override
    protected ValidationResults validate(ChargeTransaction transactionEntity, AbstractTransaction referencedTransaction) {
        if (referencedTransaction instanceof AuthorizeTransaction)  {
            return validator.validate(transactionEntity, (AuthorizeTransaction) referencedTransaction);
        }  else {
            var result = new ValidationResults();
            result.addError("Referenced transaction is of incorrect type");
            return result;
        }
    }

    @Override
    protected void sendEventsForTransaction(ChargeTransaction transaction) {
        //Send to kafka +amount
        mbProducer.sendMessage(transaction.getBelongsTo(), transaction.getAmount());
    }

    @Override
    protected void finalizeReferencedTransaction(AbstractTransaction referencedTransaction) {
        //do nothing
    }

}