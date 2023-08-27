package com.vvkozlov.emerchantpay.transaction.service.validator;

import com.vvkozlov.emerchantpay.transaction.domain.entities.AuthorizeTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.ReversalTransaction;
import org.springframework.stereotype.Component;

@Component
public class ReversalTransactionValidator extends AbstractTransactionValidator<ReversalTransaction, AuthorizeTransaction> {
    @Override
    public ValidationResults validate(ReversalTransaction transaction, AuthorizeTransaction referencedTransaction) {
        ValidationResults results = new ValidationResults();
        validateIsMerchantActive(transaction, results);
        validateIsNotNullReferenceId(transaction, results);

        //check referenced transaction
        validateIsSameMerchant(transaction, referencedTransaction, results);

        return results;
    }

    protected void validateIsNullReferenceId(AuthorizeTransaction transaction, ValidationResults results) {
    }
}
