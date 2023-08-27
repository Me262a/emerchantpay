package com.vvkozlov.emerchantpay.transaction.service.validator;

import com.vvkozlov.emerchantpay.transaction.domain.entities.AuthorizeTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.ChargeTransaction;
import org.springframework.stereotype.Component;

@Component
public class ChargeTransactionValidator extends AbstractTransactionValidator<ChargeTransaction, AuthorizeTransaction>  {
    @Override
    public ValidationResults validate(ChargeTransaction transaction, AuthorizeTransaction referencedTransaction) {
        ValidationResults results = new ValidationResults();
        validateNonZeroAmount(transaction, results);
        validateIsNotNullReferenceId(transaction, results);
        validateIsMerchantActive(transaction, results);

        //check referenced transaction
        validateIsSameMerchant(transaction, referencedTransaction, results);
        validateIsTransactionAmountNotGreaterThanReferenced(transaction, referencedTransaction, results);
        validateIsTransactionApproved(referencedTransaction, results);

        return results;
    }
}
