package com.vvkozlov.emerchantpay.transaction.service.validator;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TransactionValidatorFactory {

    private final AuthorizeTransactionValidator authorizeTransactionValidator;
    private final ChargeTransactionValidator chargeTransactionValidator;
    private final RefundTransactionValidator refundTransactionValidator;
    private final ReversalTransactionValidator reversalTransactionValidator;

    @Autowired
    public TransactionValidatorFactory(AuthorizeTransactionValidator authorizeTransactionValidator,
                                       ChargeTransactionValidator chargeTransactionValidator,
                                       RefundTransactionValidator refundTransactionValidator,
                                       ReversalTransactionValidator reversalTransactionValidator) {
        this.authorizeTransactionValidator = authorizeTransactionValidator;
        this.chargeTransactionValidator = chargeTransactionValidator;
        this.refundTransactionValidator = refundTransactionValidator;
        this.reversalTransactionValidator = reversalTransactionValidator;
    }
}
