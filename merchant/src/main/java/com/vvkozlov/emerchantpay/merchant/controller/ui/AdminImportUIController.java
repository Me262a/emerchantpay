package com.vvkozlov.emerchantpay.merchant.controller.ui;

import com.vvkozlov.emerchantpay.merchant.service.AdminService;
import com.vvkozlov.emerchantpay.merchant.service.model.AdminViewDTO;
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
@RequestMapping("/ui/admins/import")
@PreAuthorize("hasAuthority(T(com.vvkozlov.emerchantpay.merchant.domain.constants.UserRoles).ROLE_ADMIN)")
public class AdminImportUIController {

    private final AdminService adminService;

    @Autowired
    public AdminImportUIController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(
            summary = "Import new admins",
            description = "Import admins from pre-defined csv file in classpath."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully imported"),
            @ApiResponse(responseCode = "500", description = "Server error - check the server log")
    })
    @PostMapping("")
    public ResponseEntity<List<AdminViewDTO>> importAdminsFromCsv() {
        var operationResult = adminService.importAdminsFromCsv();
        if (operationResult.isSuccess()) {
            return ResponseEntity.ok(operationResult.getResult());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
