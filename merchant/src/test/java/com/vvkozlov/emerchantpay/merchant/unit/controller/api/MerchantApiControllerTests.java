package com.vvkozlov.emerchantpay.merchant.unit.controller.api;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.vvkozlov.emerchantpay.merchant.controller.api.MerchantApiController;
import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import com.vvkozlov.emerchantpay.merchant.service.contract.service.MerchantRetrievalService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.KeyPair;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MerchantApiController.class)
@Import({WebMvcConfig.class, UnitTestSecurityConfig.class})
@WithMockUser(authorities = "merchant")
class MerchantApiControllerTests {

    final String CONTROLLER_BASE_URL = "/api/merchants";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KeyPair testKeyPair;

    @MockBean
    private MerchantRetrievalService merchantRetrievalService;

    @BeforeEach
    void setup() {
    }

    @Test
    public void testGetIsMerchantActive_MerchantFoundAndActive_ReturnsTrue() throws Exception {
        String id = "12345";
        MerchantViewDTO merchantDTO = mock(MerchantViewDTO.class);
        when(merchantDTO.getStatus()).thenReturn(MerchantStatusEnum.ACTIVE);
        OperationResult<MerchantViewDTO> operationResult = OperationResult.success(merchantDTO);

        when(merchantRetrievalService.getMerchant(id)).thenReturn(operationResult);

        String token = createJwtToken(id, List.of("merchant"));

        mockMvc.perform(get(CONTROLLER_BASE_URL + "/is-active")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testGetIsMerchantActive_MerchantFoundAndInactive_ReturnsFalse() throws Exception {
        String id = "12345";
        MerchantViewDTO merchantDTO = mock(MerchantViewDTO.class);
        when(merchantDTO.getStatus()).thenReturn(MerchantStatusEnum.INACTIVE);
        OperationResult<MerchantViewDTO> operationResult = OperationResult.success(merchantDTO);

        when(merchantRetrievalService.getMerchant(id)).thenReturn(operationResult);

        String token = createJwtToken(id, List.of("merchant"));

        mockMvc.perform(get(CONTROLLER_BASE_URL + "/is-active")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    public void testGetIsMerchantActive_MerchantNotFound_ReturnsNotFoundStatus() throws Exception {
        String id = "12345";
        OperationResult<MerchantViewDTO> operationResult = OperationResult.failure("Merchant not found");

        String token = createJwtToken(id, List.of("merchant"));

        when(merchantRetrievalService.getMerchant(id)).thenReturn(operationResult);

        mockMvc.perform(get(CONTROLLER_BASE_URL + "/is-active")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetIsMerchantActive_Forbidden() throws Exception {
        String id = "12345";
        String token = createJwtToken(id, List.of("admin"));

        mockMvc.perform(get(CONTROLLER_BASE_URL + "/is-active")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    /** Create signed JWT using Nimbus
     */
    private String createJwtToken(String subject, List<String> authorities) throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .claim("authorities", authorities)
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        JWSSigner signer = new RSASSASigner(testKeyPair.getPrivate());

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }
}
