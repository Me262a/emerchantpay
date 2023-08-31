package com.vvkozlov.emerchantpay.merchant.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvkozlov.emerchantpay.merchant.api.rest.AdminController;
import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import com.vvkozlov.emerchantpay.merchant.service.AdminService;
import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdminController.class)
@Import({WebMvcConfig.class, UnitTestSecurityConfig.class})
@WithMockUser(authorities = "admin")
class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private MerchantService merchantService;

    @BeforeEach
    void setup() {
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
                .totalTransactionSum(BigDecimal.valueOf(300.0))
                .name("Updated Merchant")
                .description("Updated description")
                .email("updated_merchant@example.com")
                .build();

        MerchantEditDTO editDTO = new MerchantEditDTO();

        ObjectMapper objectMapper = new ObjectMapper();
        String editDTOPayload = objectMapper.writeValueAsString(editDTO);

        when(merchantService.updateMerchant(eq(testId), any(MerchantEditDTO.class))).thenReturn(OperationResult.success(updatedMockMerchant));

        mockMvc.perform(put("/api/admin/merchant/" + testId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editDTOPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Merchant"));
    }

    @Test
    public void testImportAdminsFromCsv_Success() throws Exception {
        when(adminService.importAdminsFromCsv()).thenReturn(OperationResult.success(Collections.emptyList())); // empty list for simplicity

        mockMvc.perform(post("/api/admin/import").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetMerchants_Pagination() throws Exception {
        Page<MerchantViewDTO> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        when(merchantService.getMerchants(any(Pageable.class))).thenReturn(OperationResult.success(page));

        mockMvc.perform(get("/api/admin/merchant?page=0&size=10&sort=id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }


    @Test
    public void testImportMerchantsFromCsv_Success() throws Exception {
        when(merchantService.importMerchantsFromCsv()).thenReturn(OperationResult.success(Collections.emptyList()));

        mockMvc.perform(post("/api/admin/merchant/import").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(authorities = "merchant")
    public void testImportMerchantsFromCsv_Forbidden() throws Exception {
        when(merchantService.importMerchantsFromCsv()).thenReturn(OperationResult.success(Collections.emptyList()));

        mockMvc.perform(post("/api/admin/merchant/import").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testImportMerchantsFromCsv_Failure() throws Exception {
        when(merchantService.importMerchantsFromCsv()).thenReturn(OperationResult.failure("Error importing merchants"));

        mockMvc.perform(post("/api/admin/merchant/import").with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRemoveMerchantById_Success() throws Exception {
        when(merchantService.removeMerchantById("testID")).thenReturn(OperationResult.success(null));

        mockMvc.perform(delete("/api/admin/merchant/testID").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveMerchantById_Failure() throws Exception {
        when(merchantService.removeMerchantById("invalidID")).thenReturn(OperationResult.failure("Merchant not found"));

        mockMvc.perform(delete("/api/admin/merchant/invalidID").with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRemoveAllMerchants_Success() throws Exception {
        when(merchantService.removeAllMerchants(false)).thenReturn(OperationResult.success(null));

        mockMvc.perform(delete("/api/admin/merchant").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveAllMerchants_Failure() throws Exception {
        when(merchantService.removeAllMerchants(false)).thenReturn(OperationResult.failure("Error deleting all merchants"));

        mockMvc.perform(delete("/api/admin/merchant").with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
