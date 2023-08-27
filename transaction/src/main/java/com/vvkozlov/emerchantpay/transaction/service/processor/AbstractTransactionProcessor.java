package com.vvkozlov.emerchantpay.transaction.service.processor;

import com.vvkozlov.emerchantpay.transaction.domain.constants.TransactionStatusEnum;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;
import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import com.vvkozlov.emerchantpay.transaction.service.mapper.TransactionMapper;
import com.vvkozlov.emerchantpay.transaction.service.model.AbstractTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.model.TransactionViewDTO;
import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;
import com.vvkozlov.emerchantpay.transaction.service.validator.ValidationResults;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractTransactionProcessor<T extends AbstractTransactionCreateDTO, R extends AbstractTransaction> implements TransactionProcessor<T> {

    protected final TransactionRepository transactionRepository;

    public AbstractTransactionProcessor(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    protected abstract R convertDtoToEntity(T createDto);

    protected abstract void sendEventsForTransaction(R transaction);
    protected abstract void finalizeReferencedTransaction(AbstractTransaction referencedTransaction);

    protected abstract ValidationResults validate(R transactionEntity, AbstractTransaction referencedTransaction);

    public OperationResult<TransactionViewDTO> process(T createDto) {
        R transactionEntity = convertDtoToEntity(createDto);
        UUID referenceId = createDto.getReferenceId();
        AbstractTransaction referencedTransaction = (referenceId != null)
                ? findReferencedTransaction(referenceId).orElse(null)
                : null;
        if (referencedTransaction == null) {
            return OperationResult.failure("Referenced transaction not found");
        }

        ValidationResults validationResult = validate(transactionEntity, referencedTransaction);
        if (!validationResult.isValid()) {
            return OperationResult.failure(validationResult.getErrorMessages().toString());
        }

        if (checkReferencedTransactionStatus(referencedTransaction)) {
            callExternalPaymentProcessor(transactionEntity, referencedTransaction);
        } else {
            transactionEntity.setStatus(TransactionStatusEnum.ERROR);
        }

        transactionRepository.save(transactionEntity);
        sendEventsForTransaction(transactionEntity);

        return OperationResult.success(TransactionMapper.INSTANCE.toDto(transactionEntity));
    }

    /**
     * Stub method to emulate call to an external payment processor (Card processing network, etc)
     * For simplicity, this method returns current transaction as approved immediately.
     * In real system, we likely will have to wait asynchronously until payment processor responds
     */
    protected void callExternalPaymentProcessor(R transaction, AbstractTransaction referencedTransaction) {
        transaction.setStatus(TransactionStatusEnum.APPROVED);
        finalizeReferencedTransaction(referencedTransaction);
    }

    /**
     * Business rule is to set transaction to error state in DB if referenced transaction is not in correct status
     */
    protected boolean checkReferencedTransactionStatus(AbstractTransaction referencedTransaction) {
        return referencedTransaction.getStatus().equals(TransactionStatusEnum.APPROVED) || referencedTransaction.getStatus().equals(TransactionStatusEnum.REFUNDED);
    }

    protected Optional<AbstractTransaction> findReferencedTransaction(UUID referenceId) {
        return transactionRepository.findById(referenceId);
    }
}