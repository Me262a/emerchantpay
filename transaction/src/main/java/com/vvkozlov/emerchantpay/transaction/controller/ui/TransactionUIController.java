package com.vvkozlov.emerchantpay.transaction.controller.ui;

import com.vvkozlov.emerchantpay.transaction.service.contract.service.TransactionHandlingService;
import com.vvkozlov.emerchantpay.transaction.service.contract.service.TransactionRetrievalService;
import com.vvkozlov.emerchantpay.transaction.service.model.AbstractTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.model.TransactionViewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller to manage transactions.
 */
@RestController
@RequestMapping("/ui/transactions")
@PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.transaction.domain.constants.UserRoles).ROLE_MERCHANT)")
public class TransactionUIController {

    private final TransactionRetrievalService transactionRetrievalService;
    private final TransactionHandlingService transactionHandlingService;


    @Autowired
    public TransactionUIController(TransactionRetrievalService transactionRetrievalService, TransactionHandlingService transactionHandlingService) {
        this.transactionRetrievalService = transactionRetrievalService;
        this.transactionHandlingService = transactionHandlingService;
    }

    @Operation(summary = "Get a transaction by uuid", description = "Returns a transaction data as per the uuid")
    @GetMapping("{uuid}")
    public ResponseEntity<TransactionViewDTO> getTransaction(@PathVariable UUID uuid, @AuthenticationPrincipal Jwt jwt) {
        var operationResult = transactionRetrievalService.getTransaction(jwt.getClaimAsString("sub"), uuid);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get paged transactions",
            description = "Specify page number, size, and sort order. Returns a list of transactions in a page."
    )
    @GetMapping("")
    public ResponseEntity<Page<TransactionViewDTO>> getTransactions(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(name = "page", example = "0", description = "Page number") int page,
            @Parameter(name = "size", example = "10", description = "Items per page") int size,
            @Parameter(name = "sort", example = "name,asc", description = "Sorting criteria") String sort,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var operationResult = transactionRetrievalService.getTransactions(jwt.getClaimAsString("sub"), pageable);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Creates a transaction.", description = "Returns a created transaction or error message.")
    @PostMapping("")
    public ResponseEntity<Object> createTransaction(
            @RequestBody AbstractTransactionCreateDTO createDto,
            @AuthenticationPrincipal Jwt jwt)
    {
        createDto.setBelongsTo(jwt.getClaimAsString("sub"));
        var operationResult = transactionHandlingService.processTransaction(createDto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationResult.getErrors());
        }
    }
}
