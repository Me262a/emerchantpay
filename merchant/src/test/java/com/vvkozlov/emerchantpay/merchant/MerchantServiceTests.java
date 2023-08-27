package com.vvkozlov.emerchantpay.merchant;

import com.vvkozlov.emerchantpay.merchant.infra.repository.MerchantRepository;
import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.contract.OAuthServerAdminClient;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class MerchantServiceTests {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private OAuthServerAdminClient oAuthServerAdminClient;

    @InjectMocks
    private MerchantService merchantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
/*
    @Test
    void testGetMerchant_ExistingId() {
        String authId = UUID.randomUUID().toString();
        Merchant mockMerchant = new Merchant();
        Optional<Merchant> mockOptional = mock(Optional.class);
        when(mockOptional.isPresent()).thenReturn(true);
        when(mockOptional.get()).thenReturn(mockMerchant);
        when(merchantRepository.findByAuthId(authId)).thenReturn(mockOptional);

        OperationResult<MerchantViewDTO> result = merchantService.getMerchant(authId);

        assertThat(result.isSuccess()).isTrue();
        verify(merchantRepository, times(1)).findByAuthId(authId);
    }*/

    @Test
    void testGetMerchant_NonExistingId() {
        String authId = UUID.randomUUID().toString();
        when(merchantRepository.findByAuthId(authId)).thenReturn(Optional.empty());

        OperationResult<MerchantViewDTO> result = merchantService.getMerchant(authId);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains("Merchant not found");
    }
/*
    @Test
    void testUpdateMerchant_ExistingId() {
        String authId = UUID.randomUUID().toString();
        Merchant mockMerchant = new Merchant();
        MerchantEditDTO editDTO = new MerchantEditDTO();
        when(merchantRepository.findByAuthId(authId)).thenReturn(Optional.of(mockMerchant));

        OperationResult<MerchantViewDTO> result = merchantService.updateMerchant(authId, editDTO);

        assertThat(result.isSuccess()).isTrue();
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }*/

    @Test
    void testUpdateMerchant_NonExistingId() {
        String authId = UUID.randomUUID().toString();
        MerchantEditDTO editDTO = new MerchantEditDTO();
        when(merchantRepository.findByAuthId(authId)).thenReturn(Optional.empty());

        OperationResult<MerchantViewDTO> result = merchantService.updateMerchant(authId, editDTO);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains("Merchant not found");
    }
/*
    @Test
    void testImportMerchantsFromCsv() throws Exception {
        when(oAuthServerAdminClient.addUser(anyString(), anyList())).thenReturn(OperationResult.success("Some AuthId"));

        OperationResult<List<MerchantViewDTO>> result = merchantService.importMerchantsFromCsv();

        assertThat(result.isSuccess()).isTrue();
        assertThat(Collections.singletonList(result.getResult())).isNotEmpty();
        verify(merchantRepository, atLeastOnce()).save(any(Merchant.class));
    }*/
}
