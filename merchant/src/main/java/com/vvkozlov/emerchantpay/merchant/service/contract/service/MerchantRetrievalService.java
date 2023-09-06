package com.vvkozlov.emerchantpay.merchant.service.contract.service;

import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MerchantRetrievalService {
    OperationResult<MerchantViewDTO> getMerchant(final String id);
    OperationResult<Page<MerchantViewDTO>> getMerchants(final Pageable pageable);
}
