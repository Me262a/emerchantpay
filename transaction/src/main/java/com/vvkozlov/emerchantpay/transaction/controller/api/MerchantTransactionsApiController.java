package com.vvkozlov.emerchantpay.transaction.controller.api;

import com.vvkozlov.emerchantpay.transaction.service.contract.service.MerchantTransactionsRetrievalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to query data related to specific merchant
 */
@RestController
@RequestMapping("/api/merchants")
@PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.transaction.domain.constants.UserRoles).ROLE_ADMIN)")
public class MerchantTransactionsApiController {

    private final MerchantTransactionsRetrievalService merchantTransactionsRetrievalService;

    @Autowired
    public MerchantTransactionsApiController(MerchantTransactionsRetrievalService merchantTransactionsRetrievalService) {
        this.merchantTransactionsRetrievalService = merchantTransactionsRetrievalService;
    }

    @Operation(summary = "Checks if specified merchant id has associated transactions",
            description = "Returns true if the merchant has associated transactions, false if not.")
    @GetMapping("{merchantId}/transactions/exist")
    public ResponseEntity<Boolean> hasTransactionsForMerchant(@PathVariable String merchantId) {
        boolean hasTransactions = merchantTransactionsRetrievalService.transactionsExistForMerchant(merchantId);
        return ResponseEntity.ok(hasTransactions);
    }
}
