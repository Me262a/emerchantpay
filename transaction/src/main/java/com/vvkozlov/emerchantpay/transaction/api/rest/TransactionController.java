package com.vvkozlov.emerchantpay.transaction.api.rest;

import com.vvkozlov.emerchantpay.transaction.service.TransactionService;
import com.vvkozlov.emerchantpay.transaction.service.model.*;
import io.swagger.v3.oas.annotations.Operation;
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
 * Transaction controller to manage transactions.
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get a transaction by uuid", description = "Returns a transaction data as per the uuid. " +
            "Any transaction, any role, not restricted to current user yet")
    @GetMapping("/{uuid}")
    public ResponseEntity<TransactionViewDTO> getTransaction(@PathVariable UUID uuid) {
        var operationResult = transactionService.getTransaction(uuid);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get a transaction by uuid", description = "Returns a transaction data as per the uuid. " +
            "All transactions on the server, any role, not restricted to current user yet")
    @GetMapping("/all")
    public ResponseEntity<Page<TransactionViewDTO>> getTransactions(Pageable pageable) {
        var operationResult = transactionService.getTransactions(pageable);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Creates auth transaction.", description = "Restricted to merchant role.")
    @PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.transaction.domain.constants.UserRoles).ROLE_MERCHANT)")
    @PostMapping("/authorize")
    public ResponseEntity<Object> createAuthorizeTransaction(@RequestBody AuthorizeTransactionCreateDTO createDto, @AuthenticationPrincipal Jwt jwt) {
        //TODO: use filter to extract and store it in HttpServletRequest
        createDto.setBelongsTo(jwt.getClaimAsString("sub"));
        var operationResult = transactionService.processTransaction(createDto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationResult.getErrors());
        }
    }
    @Operation(summary = "Creates charge transaction.", description = "Restricted to merchant role.")
    @PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.transaction.domain.constants.UserRoles).ROLE_MERCHANT)")
    @PostMapping("/charge")
    public ResponseEntity<Object> createChargeTransaction(@RequestBody ChargeTransactionCreateDTO createDto, @AuthenticationPrincipal Jwt jwt) {
        createDto.setBelongsTo(jwt.getClaimAsString("sub"));
        var operationResult = transactionService.processTransaction(createDto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationResult.getErrors());
        }
    }

    @Operation(summary = "Creates refund transaction.", description = "Restricted to merchant role.")
    @PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.transaction.domain.constants.UserRoles).ROLE_MERCHANT)")
    @PostMapping("/refund")
    public ResponseEntity<Object> createRefundTransaction(@RequestBody RefundTransactionCreateDTO createDto, @AuthenticationPrincipal Jwt jwt) {
        createDto.setBelongsTo(jwt.getClaimAsString("sub"));
        var operationResult = transactionService.processTransaction(createDto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationResult.getErrors());
        }
    }

    @Operation(summary = "Creates reversal transaction.", description = "Restricted to merchant role.")
    @PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.transaction.domain.constants.UserRoles).ROLE_MERCHANT)")
    @PostMapping("/reversal")
    public ResponseEntity<Object> createReversalTransaction(@RequestBody ReversalTransactionCreateDTO createDto, @AuthenticationPrincipal Jwt jwt) {
        createDto.setBelongsTo(jwt.getClaimAsString("sub"));
        var operationResult = transactionService.processTransaction(createDto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationResult.getErrors());
        }
    }
}
