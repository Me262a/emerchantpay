package com.vvkozlov.emerchantpay.merchant.controller.ui;

import com.vvkozlov.emerchantpay.merchant.service.contract.service.MerchantManagementService;
import com.vvkozlov.emerchantpay.merchant.service.contract.service.MerchantRetrievalService;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to manage existing merchants.
 */
@RestController
@RequestMapping("/ui/merchants")
@PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.merchant.domain.constants.UserRoles).ROLE_ADMIN)")
public class MerchantUIController {

    private final MerchantRetrievalService merchantRetrievalService;
    private final MerchantManagementService merchantManagementService;

    @Autowired
    public MerchantUIController(MerchantRetrievalService merchantRetrievalService, MerchantManagementService merchantManagementService) {

        this.merchantRetrievalService = merchantRetrievalService;
        this.merchantManagementService = merchantManagementService;
    }

    @Operation(summary = "Get a merchant by id", description = "Returns a merchant data as per the id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "The merchant was not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<MerchantViewDTO> getMerchant(@PathVariable String id) {
        var operationResult = merchantRetrievalService.getMerchant(id);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "Get a paged list of merchants",
            description = "Specify page number, size, and sort order. Returns a merchant data list."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "500", description = "Server error - the check server log")
    })
    @GetMapping("")
    public ResponseEntity<Page<MerchantViewDTO>> getMerchants(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(name = "page", example = "0", description = "Page number") int page,
            @Parameter(name = "size", example = "10", description = "Items per page") int size,
            @Parameter(name = "sort", example = "name,asc", description = "Sorting criteria") String sort
    ) {
        var operationResult = merchantRetrievalService.getMerchants(pageable);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Update a merchant data by id",
            description = "Update a merchant. Returns an updated merchant data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @PutMapping("{id}")
    public ResponseEntity<MerchantViewDTO> updateMerchant(@PathVariable String id, @RequestBody MerchantEditDTO dto) {
        var operationResult = merchantManagementService.updateMerchant(id, dto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(
            summary = "Remove a merchant by user auth ID",
            description = "Delete a merchant based on the provided user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed the merchant"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<List<String>> removeMerchantById(@PathVariable String id) {
        var operationResult = merchantManagementService.removeMerchantById(id);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationResult.getErrors());
        }
    }

    @Operation(
            summary = "Remove all merchants",
            description = "Delete all merchants from the current microservice and auth server."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed all merchants"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @DeleteMapping("")
    public ResponseEntity<List<String>> removeAllMerchants() {
        //set parameter to true if you want to check that merchants does not have transactions associated
        var operationResult = merchantManagementService.removeAllMerchants(false);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationResult.getErrors());
        }
    }
}
