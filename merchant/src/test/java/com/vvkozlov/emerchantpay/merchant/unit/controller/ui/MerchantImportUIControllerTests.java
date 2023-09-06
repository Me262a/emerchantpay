package com.vvkozlov.emerchantpay.merchant.unit.controller.ui;

import com.vvkozlov.emerchantpay.merchant.controller.ui.MerchantImportUIController;
import com.vvkozlov.emerchantpay.merchant.service.contract.service.UserCsvImporterService;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import com.vvkozlov.emerchantpay.merchant.unit.config.UnitTestSecurityConfig;
import com.vvkozlov.emerchantpay.merchant.unit.config.WebMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MerchantImportUIController.class)
@Import({WebMvcConfig.class, UnitTestSecurityConfig.class})
@WithMockUser(authorities = "admin")
class MerchantImportUIControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserCsvImporterService userCsvImporterService;

    @BeforeEach
    void setup() {
    }

    @Test
    public void testImportMerchantsFromCsv_Success() throws Exception {
        when(userCsvImporterService.importUsersFromCsv()).thenReturn(OperationResult.success(Collections.emptyList()));

        mockMvc.perform(post("/ui/merchants/import").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(authorities = "merchant")
    public void testImportMerchantsFromCsv_Forbidden() throws Exception {
        when(userCsvImporterService.importUsersFromCsv()).thenReturn(OperationResult.success(Collections.emptyList()));

        mockMvc.perform(post("/ui/merchants/import").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testImportMerchantsFromCsv_Failure() throws Exception {
        when(userCsvImporterService.importUsersFromCsv()).thenReturn(OperationResult.failure("Error importing merchants"));

        mockMvc.perform(post("/ui/merchants/import").with(csrf()))
                .andExpect(status().isInternalServerError());
    }
}
