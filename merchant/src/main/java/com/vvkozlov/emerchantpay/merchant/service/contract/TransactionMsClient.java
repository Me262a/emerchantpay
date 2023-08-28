package com.vvkozlov.emerchantpay.merchant.service.contract;

import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;

public interface TransactionMsClient {
    public OperationResult<Boolean> getDoesMerchantHaveTransactions(String merchantId);
}
