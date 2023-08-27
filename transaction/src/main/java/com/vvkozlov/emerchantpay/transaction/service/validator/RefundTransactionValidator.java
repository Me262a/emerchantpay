package com.vvkozlov.emerchantpay.transaction.service.validator;

import com.vvkozlov.emerchantpay.transaction.domain.entities.AuthorizeTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.ChargeTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.RefundTransaction;
import org.springframework.stereotype.Component;

@Component
public class RefundTransactionValidator extends AbstractTransactionValidator<RefundTransaction, ChargeTransaction> {
    @Override
    public ValidationResults validate(RefundTransaction transaction, ChargeTransaction referencedTransaction) {
        ValidationResults results = new ValidationResults();
        validateNonZeroAmount(transaction, results);
        validateIsMerchantActive(transaction, results);
        validateIsNotNullReferenceId(transaction, results);

        //check referenced transaction
        validateIsSameMerchant(transaction, referencedTransaction, results);
        validateIsTransactionAmountNotGreaterThanReferenced(transaction, referencedTransaction, results);
        validateIsTransactionApproved(referencedTransaction, results);

        return results;
    }

    protected void validateIsNullReferenceId(AuthorizeTransaction transaction, ValidationResults results) {
    }
}
