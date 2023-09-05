package com.vvkozlov.emerchantpay.transaction.service.contract.service;

public interface MerchantTransactionsRetrievalService {
    boolean transactionsExistForMerchant(final String merchantId);
}
