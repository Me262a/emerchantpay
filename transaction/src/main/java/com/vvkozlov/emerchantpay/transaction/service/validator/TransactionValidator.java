package com.vvkozlov.emerchantpay.transaction.service.validator;

import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;

public interface TransactionValidator<T extends AbstractTransaction> {
    ValidationResults validate(T transaction);
}