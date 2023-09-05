package com.vvkozlov.emerchantpay.transaction.service.contract.service;

import com.vvkozlov.emerchantpay.transaction.service.model.AbstractTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.model.TransactionViewDTO;
import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;

public interface TransactionHandlingService {
    OperationResult<TransactionViewDTO> processTransaction(final AbstractTransactionCreateDTO createDto);
}
