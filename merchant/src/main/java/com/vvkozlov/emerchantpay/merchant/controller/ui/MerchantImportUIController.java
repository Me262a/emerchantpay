package com.vvkozlov.emerchantpay.merchant.controller.ui;

import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Admin controller to manage merchants and import admins.
 */
@RestController
@RequestMapping("/ui/merchants/import")
@PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.merchant.domain.constants.UserRoles).ROLE_ADMIN)")
public class MerchantImportUIController {

    private final MerchantService merchantService;

    @Autowired
    public MerchantImportUIController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Operation(
            summary = "Import new merchants",
            description = "Import merchants from pre-defined csv file in classpath. Returns list of imported merchants."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully imported"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @PostMapping("")
    public ResponseEntity<List<MerchantViewDTO>> importMerchantsFromCsv() {
        var operationResult = merchantService.importMerchantsFromCsv();
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
