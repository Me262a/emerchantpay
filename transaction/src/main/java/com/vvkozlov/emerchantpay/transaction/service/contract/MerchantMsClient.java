package com.vvkozlov.emerchantpay.transaction.service.contract;

import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;

public interface MerchantMsClient {
    public OperationResult<Boolean> getIsMerchantActive(String merchantId);
}
