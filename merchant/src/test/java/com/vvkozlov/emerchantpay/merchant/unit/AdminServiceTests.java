package com.vvkozlov.emerchantpay.merchant.unit;

import com.vvkozlov.emerchantpay.merchant.service.AdminService;
import com.vvkozlov.emerchantpay.merchant.service.contract.OAuthServerAdminClient;
import com.vvkozlov.emerchantpay.merchant.service.model.AdminViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {
    @InjectMocks
    private AdminService adminService;

    @Mock
    private OAuthServerAdminClient oAuthServerAdminClient;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void importAdminsFromCsv_SuccessfulImport_ReturnsAdminList() {
        when(oAuthServerAdminClient.addUser(anyString(), anyList()))
                .thenReturn(OperationResult.success("123456"));

        OperationResult<List<AdminViewDTO>> result = adminService.importAdminsFromCsv();

        assertTrue(result.isSuccess());
        assertEquals(2, result.getResult().size());
    }

    @Test
    public void importAdminsFromCsv_ExceptionDuringImport_ReturnsFailure() {
        when(oAuthServerAdminClient.addUser(anyString(), anyList()))
                .thenThrow(new RuntimeException("Failed to add user"));

        OperationResult<List<AdminViewDTO>> result = adminService.importAdminsFromCsv();

        assertFalse(result.isSuccess());
        assertTrue(result.getErrors().stream().findFirst().orElse("")
                .contains("An error occurred while importing admins"));
    }
}

