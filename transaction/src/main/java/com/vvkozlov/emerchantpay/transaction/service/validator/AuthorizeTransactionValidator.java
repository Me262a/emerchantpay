package com.vvkozlov.emerchantpay.transaction.service.validator;

import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AuthorizeTransaction;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeTransactionValidator extends AbstractTransactionValidator<AuthorizeTransaction, AbstractTransaction> {
    @Override
    public ValidationResults validate(AuthorizeTransaction transaction) {
        ValidationResults results = new ValidationResults();
        validateNonZeroAmount(transaction, results);
        validateIsEmailCorrect(transaction, results);
        validateIsMerchantActive(transaction, results);

        return results;
    }
}
