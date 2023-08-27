package com.vvkozlov.emerchantpay.merchant.api.rest;

import com.vvkozlov.emerchantpay.merchant.service.AdminService;
import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.model.AdminViewDTO;
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
 * Admin controller to manage merchants and import admins.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.merchant.domain.constants.UserRoles).ROLE_ADMIN)")
public class AdminController {

    private final AdminService adminService;
    private final MerchantService merchantService;

    @Autowired
    public AdminController(AdminService adminService, MerchantService merchantService) {
        this.adminService = adminService;
        this.merchantService = merchantService;
    }

    @Operation(summary = "Get a merchant by uuid", description = "Returns a merchant data as per the uuid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "The merchant was not found")
    })
    @GetMapping("merchant/{uuid}")
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
    @GetMapping("merchant")
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
    @PutMapping("merchant/{uuid}")
    public ResponseEntity<MerchantViewDTO> updateMerchant(@PathVariable String uuid, @RequestBody MerchantEditDTO dto) {
        var operationResult = merchantService.updateMerchant(uuid, dto);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Import new admins",
            description = "Import admins from pre-defined csv file in classpath."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully imported"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @PostMapping("import")
    public ResponseEntity<List<AdminViewDTO>> importAdminsFromCsv() {
        var operationResult = adminService.importAdminsFromCsv();
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
    @PostMapping("merchant/import")
    public ResponseEntity<List<MerchantViewDTO>> importMerchantsFromCsv() {
        var operationResult = merchantService.importMerchantsFromCsv();
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
    @DeleteMapping("merchant/{merchantId}")
    public ResponseEntity<Void> removeMerchantById(@PathVariable String merchantId) {
        var operationResult = merchantService.removeMerchantById(merchantId);
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
    @DeleteMapping("merchant")
    public ResponseEntity<Void> removeAllMerchants() {
        var operationResult = merchantService.removeAllMerchants();
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
