package com.vvkozlov.emerchantpay.transaction.service.processor;

import com.vvkozlov.emerchantpay.transaction.domain.constants.TransactionStatusEnum;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.ChargeTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.RefundTransaction;
import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import com.vvkozlov.emerchantpay.transaction.service.contract.mb.MessageBrokerProducer;
import com.vvkozlov.emerchantpay.transaction.service.mapper.TransactionMapper;
import com.vvkozlov.emerchantpay.transaction.service.model.RefundTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.validator.RefundTransactionValidator;
import com.vvkozlov.emerchantpay.transaction.service.validator.ValidationResults;
import org.springframework.stereotype.Service;
@Service
public class RefundTransactionProcessor extends AbstractTransactionProcessor<RefundTransactionCreateDTO, RefundTransaction> {

    private final RefundTransactionValidator validator;
    public final MessageBrokerProducer mbProducer;

    public RefundTransactionProcessor(RefundTransactionValidator validator, TransactionRepository transactionRepository, MessageBrokerProducer mbProducer) {
        super(transactionRepository);
        this.validator = validator;
        this.mbProducer = mbProducer;
    }

    @Override
    protected RefundTransaction convertDtoToEntity(RefundTransactionCreateDTO createDto) {
        return TransactionMapper.INSTANCE.toEntity(createDto);
    }


    @Override
    protected ValidationResults validate(RefundTransaction transactionEntity, AbstractTransaction referencedTransaction) {
        if (referencedTransaction instanceof ChargeTransaction)  {
            return validator.validate(transactionEntity, (ChargeTransaction) referencedTransaction);
        }  else {
            var result = new ValidationResults();
            result.addError("Referenced transaction is of incorrect type");
            return result;
        }
    }

    @Override
    protected void sendEventsForTransaction(RefundTransaction transaction) {
        //Send to kafka -amount
        mbProducer.sendMessage(transaction.getBelongsTo(), transaction.getAmount().negate());
    }

    @Override
    protected void finalizeReferencedTransaction(AbstractTransaction referencedTransaction) {
        referencedTransaction.setStatus(TransactionStatusEnum.REFUNDED);
    }

}
