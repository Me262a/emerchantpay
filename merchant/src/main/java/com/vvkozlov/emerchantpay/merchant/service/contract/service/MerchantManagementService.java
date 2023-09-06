package com.vvkozlov.emerchantpay.merchant.service.contract.service;

import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;

import java.util.List;

public interface MerchantManagementService {
    OperationResult<MerchantViewDTO> updateMerchant(final String id, final MerchantEditDTO dto);
    OperationResult<Void> removeMerchantById(final String merchantId);
    OperationResult<List<String>> removeAllMerchants(boolean checkForActiveTransactions);
}
