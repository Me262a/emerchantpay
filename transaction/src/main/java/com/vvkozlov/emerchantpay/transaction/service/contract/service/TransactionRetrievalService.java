package com.vvkozlov.emerchantpay.transaction.service.contract.service;

import com.vvkozlov.emerchantpay.transaction.service.model.TransactionViewDTO;
import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionRetrievalService {
    OperationResult<TransactionViewDTO> getTransaction(final String belongsTo, final UUID uuid);
    OperationResult<Page<TransactionViewDTO>> getTransactions(final String belongsTo, final Pageable pageable);
}
