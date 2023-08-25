package com.vvkozlov.emerchantpay.merchant.api.rest;

import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The Merchant controller to import, update, view, remove merchants.
 */
@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Operation(summary = "Get a merchant by uuid", description = "Returns a merchant data as per the uuid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "The merchant was not found")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<MerchantViewDTO> getMerchant(@PathVariable String uuid) {
        var operationResult = merchantService.getMerchant(uuid);
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
    @GetMapping
    public ResponseEntity<Page<MerchantViewDTO>> getMerchants(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(name = "page", example = "0", description = "Page number") int page,
            @Parameter(name = "size", example = "10", description = "Items per page") int size,
            @Parameter(name = "sort", example = "name,asc", description = "Sorting criteria") String sort
    ) {
        var operationResult = merchantService.getMerchants(pageable);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Update a merchant data by uuid",
            description = "Update a merchant. Returns an updated merchant data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<MerchantViewDTO> updateMerchant(@PathVariable String uuid, @RequestBody MerchantEditDTO dto) {
        var operationResult = merchantService.updateMerchant(uuid, dto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Import new merchants",
            description = "Import merchants from pre-defined csv file in classpath. Returns list of imported merchants."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully imported"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @PostMapping("/import")
    public ResponseEntity<List<MerchantViewDTO>> importMerchantsFromCsv() {
        var operationResult = merchantService.importMerchantsFromCsv();
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
