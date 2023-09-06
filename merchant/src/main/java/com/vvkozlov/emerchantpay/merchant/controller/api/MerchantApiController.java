package com.vvkozlov.emerchantpay.merchant.controller.api;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import com.vvkozlov.emerchantpay.merchant.service.contract.service.MerchantRetrievalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin controller to manage merchants and import admins.
 */
@RestController
@RequestMapping("/api/merchants")
@PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.merchant.domain.constants.UserRoles).ROLE_MERCHANT)")
public class MerchantApiController {

    private final MerchantRetrievalService merchantRetrievalService;

    @Autowired
    public MerchantApiController(MerchantRetrievalService merchantRetrievalService) {
        this.merchantRetrievalService = merchantRetrievalService;
    }

    @Operation(
            summary = "Get a merchant status by uuid",
            description = "Returns a merchant status data as per the uuid."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "The merchant was not found")
    })
    @GetMapping("/is-active")
    public ResponseEntity<Boolean> getIsMerchantActive(
            @AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getClaimAsString("sub");
        var operationResult = merchantRetrievalService.getMerchant(id);
        if (operationResult.isSuccess()) {
            boolean isActive = operationResult.getResult().getStatus() == MerchantStatusEnum.ACTIVE;
            return ResponseEntity.ok(isActive);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
