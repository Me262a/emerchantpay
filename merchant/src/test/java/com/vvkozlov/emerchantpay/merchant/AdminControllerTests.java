package com.vvkozlov.emerchantpay.merchant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvkozlov.emerchantpay.merchant.api.rest.AdminController;
import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import com.vvkozlov.emerchantpay.merchant.service.AdminService;
import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTests {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @Mock
    private MerchantService merchantService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testGetMerchant_ExistingID() throws Exception {
        String testID = UUID.randomUUID().toString();
        MerchantViewDTO mockMerchant = new MerchantViewDTO();
        mockMerchant.setId(testID);
        when(merchantService.getMerchant(testID)).thenReturn(OperationResult.success(mockMerchant));

        mockMvc.perform(get("/api/admin/merchant/" + testID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testID));
    }

    @Test
    public void testGetMerchant_NonExistingID() throws Exception {
        when(merchantService.getMerchant("invalidID")).thenReturn(OperationResult
                .failure("Merchant not found."));

        mockMvc.perform(get("/api/admin/merchant/invalidID"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testUpdateMerchant_ValidData() throws Exception {
        var testId = UUID.randomUUID().toString();
        MerchantViewDTO updatedMockMerchant = MerchantViewDTO.builder()
                .id(testId)
                .status(MerchantStatusEnum.ACTIVE)
                .totalTransactionSum(300.0)
                .name("Updated Merchant")
                .description("Updated description")
                .email("updated_merchant@example.com")
                .build();

        MerchantEditDTO editDTO = new MerchantEditDTO();

        ObjectMapper objectMapper = new ObjectMapper();
        String editDTOPayload = objectMapper.writeValueAsString(editDTO);

        when(merchantService.updateMerchant(eq(testId), any(MerchantEditDTO.class))).thenReturn(OperationResult.success(updatedMockMerchant));

        mockMvc.perform(put("/api/admin/merchant/" + testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editDTOPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Merchant"));
    }

    @Test
    public void testImportAdminsFromCsv_Success() throws Exception {
        when(adminService.importAdminsFromCsv()).thenReturn(OperationResult.success(Collections.emptyList())); // empty list for simplicity

        mockMvc.perform(post("/api/admin/import"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
