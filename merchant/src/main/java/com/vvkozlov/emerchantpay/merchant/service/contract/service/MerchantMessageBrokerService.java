package com.vvkozlov.emerchantpay.merchant.service.contract.service;

import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.messagebroker.TransactionMbModel;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;

public interface MerchantMessageBrokerService {
    OperationResult<MerchantViewDTO> addTransactionAmountToMerchant(TransactionMbModel tranMbModel);
}
