package com.vvkozlov.emerchantpay.transaction.service.processor;

import com.vvkozlov.emerchantpay.transaction.domain.constants.TransactionStatusEnum;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AuthorizeTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.ReversalTransaction;
import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import com.vvkozlov.emerchantpay.transaction.service.mapper.TransactionMapper;
import com.vvkozlov.emerchantpay.transaction.service.model.ReversalTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.validator.ReversalTransactionValidator;
import com.vvkozlov.emerchantpay.transaction.service.validator.ValidationResults;
import org.springframework.stereotype.Service;
@Service
public class ReversalTransactionProcessor extends AbstractTransactionProcessor<ReversalTransactionCreateDTO, ReversalTransaction> {

    private final ReversalTransactionValidator validator;

    public ReversalTransactionProcessor(TransactionRepository transactionRepository, ReversalTransactionValidator validator) {
        super(transactionRepository);
        this.validator = validator;
    }

    @Override
    protected ReversalTransaction convertDtoToEntity(ReversalTransactionCreateDTO createDto) {
        return TransactionMapper.INSTANCE.toEntity(createDto);
    }

    @Override
    protected ValidationResults validate(ReversalTransaction transactionEntity, AbstractTransaction referencedTransaction) {
        if (referencedTransaction instanceof AuthorizeTransaction)  {
            return validator.validate(transactionEntity, (AuthorizeTransaction) referencedTransaction);
        }  else {
            var result = new ValidationResults();
            result.addError("Referenced transaction is of incorrect type");
            return result;
        }
    }

    @Override
    protected void sendEventsForTransaction(ReversalTransaction transaction) {
        //Do nothing
    }

    @Override
    protected void finalizeReferencedTransaction(AbstractTransaction referencedTransaction) {
        referencedTransaction.setStatus(TransactionStatusEnum.REVERSED);
    }
}