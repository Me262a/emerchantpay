package com.vvkozlov.emerchantpay.transaction.controller.api;

import com.vvkozlov.emerchantpay.transaction.service.contract.service.MerchantTransactionsRetrievalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Transaction controller to manage transactions.
 */
@RestController
@RequestMapping("/api/merchants")
public class MerchantApiController {

    private final MerchantTransactionsRetrievalService merchantTransactionsRetrievalService;

    @Autowired
    public MerchantApiController(MerchantTransactionsRetrievalService merchantTransactionsRetrievalService) {
        this.merchantTransactionsRetrievalService = merchantTransactionsRetrievalService;
    }

    @Operation(summary = "Checks if specified merchant id has related transactions",
            description = "Returns a boolean of check result. " +
                    "Protection for this method to be added for real application")
    @GetMapping("{merchantId}/transactions/exist")
    public ResponseEntity<Boolean> hasTransactionsForMerchant(@PathVariable String merchantId) {
        boolean hasTransactions = merchantTransactionsRetrievalService.transactionsExistForMerchant(merchantId);
        return ResponseEntity.ok(hasTransactions);
    }
}
