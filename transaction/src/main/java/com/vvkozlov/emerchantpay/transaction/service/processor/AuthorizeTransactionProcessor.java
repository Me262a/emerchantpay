package com.vvkozlov.emerchantpay.transaction.service.processor;

import com.vvkozlov.emerchantpay.transaction.domain.constants.TransactionStatusEnum;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AuthorizeTransaction;
import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import com.vvkozlov.emerchantpay.transaction.service.mapper.TransactionMapper;
import com.vvkozlov.emerchantpay.transaction.service.model.AuthorizeTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.model.TransactionViewDTO;
import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;
import com.vvkozlov.emerchantpay.transaction.service.validator.AuthorizeTransactionValidator;
import com.vvkozlov.emerchantpay.transaction.service.validator.ValidationResults;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeTransactionProcessor implements TransactionProcessor<AuthorizeTransactionCreateDTO> {

    private final AuthorizeTransactionValidator validator;

    private final TransactionRepository transactionRepository;

    public AuthorizeTransactionProcessor(AuthorizeTransactionValidator validator, TransactionRepository transactionRepository) {
        this.validator = validator;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public OperationResult<TransactionViewDTO> process(AuthorizeTransactionCreateDTO createDto) {
        AuthorizeTransaction transactionEntity = TransactionMapper.INSTANCE.toEntity(createDto);
        ValidationResults validationResult = validator.validate(transactionEntity);
        if (!validationResult.isValid()) {
            return OperationResult.failure(validationResult.getErrorMessages().toString());
        }
        transactionEntity.setStatus(TransactionStatusEnum.APPROVED);
        transactionRepository.save(transactionEntity);
        return OperationResult.success(TransactionMapper.INSTANCE.toDto(transactionEntity));
    }
}