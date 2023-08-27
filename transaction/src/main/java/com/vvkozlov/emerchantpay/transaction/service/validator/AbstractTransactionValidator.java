package com.vvkozlov.emerchantpay.transaction.service.validator;

import com.vvkozlov.emerchantpay.transaction.domain.constants.TransactionStatusEnum;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;
import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransactionWithAmount;

import java.util.regex.Pattern;

public abstract class AbstractTransactionValidator<T, K extends AbstractTransaction> {
    //TODO: Use some standard library to check email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public ValidationResults validate(T transaction){
        throw new UnsupportedOperationException("This validator is not implemented");
    };

    public ValidationResults validate(T transaction, K referencedTransaction){
        throw new UnsupportedOperationException("This validator is not implemented");
    };

    protected void validateNonZeroAmount(AbstractTransactionWithAmount transaction, ValidationResults results) {
        if (transaction.getAmount().signum() <= 0) {
            results.addError("Amount must be greater than 0");
        }
    }

    protected void validateIsMerchantActive(AbstractTransaction transaction, ValidationResults results) {
        //TODO: Implement request to microservice
        // Send request to merchant service
        // There may be a gap when merchant is deactivated during the process
        // We will get an event for this from merchants via kafka and reverse/refund the transactions
        // if (!active) {
        //    results.addError("Merchant is not active");
        //    || results.addError("Merchant does not exist");
        // }
    }

    protected void validateIsSameMerchant(AbstractTransaction currentTransaction, AbstractTransaction previousTransaction, ValidationResults results) {
        if (!currentTransaction.getBelongsTo().equals(previousTransaction.getBelongsTo())) {
            results.addError("Transactions do not belong to the same merchant");
        }
    }

    protected void validateIsTransactionApproved(AbstractTransaction transaction, ValidationResults results) {
        if (transaction.getStatus() != TransactionStatusEnum.APPROVED) {
            results.addError("Referenced transaction is not approved");
        }
    }

    protected void validateIsTransactionAmountNotGreaterThanReferenced(AbstractTransactionWithAmount transaction, AbstractTransactionWithAmount referencedTransaction, ValidationResults results) {
        if (transaction.getAmount().compareTo(referencedTransaction.getAmount()) > 0) {
            results.addError("Transaction amount is greater than amount in referenced transaction");
        }
    }

    protected void validateIsEmailCorrect(AbstractTransaction transaction, ValidationResults results) {
        if (transaction.getCustomerEmail() == null || !EMAIL_PATTERN.matcher(transaction.getCustomerEmail()).matches()) {
            results.addError("Invalid email format");
        }
    }


    protected void validateIsNotNullReferenceId(AbstractTransaction transaction, ValidationResults results) {
        if (transaction.getReferenceId() == null) {
            results.addError("Reference ID cannot be null");
        }
    }
}
