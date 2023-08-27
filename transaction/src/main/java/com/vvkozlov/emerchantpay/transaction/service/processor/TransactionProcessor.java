package com.vvkozlov.emerchantpay.transaction.service.processor;

import com.vvkozlov.emerchantpay.transaction.service.model.AbstractTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.model.TransactionViewDTO;
import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;

public interface TransactionProcessor<T extends AbstractTransactionCreateDTO> {
    OperationResult<TransactionViewDTO> process(T createDto);
}
