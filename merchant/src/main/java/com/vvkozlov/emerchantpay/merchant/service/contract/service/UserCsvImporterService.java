package com.vvkozlov.emerchantpay.merchant.service.contract.service;

import com.vvkozlov.emerchantpay.merchant.service.model.BaseUserViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;

import java.util.List;

public interface UserCsvImporterService {
    OperationResult<List<? extends BaseUserViewDTO>> importUsersFromCsv();
}
