package com.vvkozlov.emerchantpay.merchant.service.contract;

import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;

import java.util.List;

public interface OAuthServerAdminClient {
    OperationResult<String> addUser(String email, List<String> roles);
}
