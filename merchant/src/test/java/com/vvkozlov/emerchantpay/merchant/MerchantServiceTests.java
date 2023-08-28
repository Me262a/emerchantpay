package com.vvkozlov.emerchantpay.merchant;

import com.vvkozlov.emerchantpay.merchant.infra.repository.MerchantRepository;
import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false"
})
class MerchantServiceTests {
    @InjectMocks
    private MerchantService merchantService;

    @Mock
    private MerchantRepository merchantRepository;
/*Remove warnings
    @Mock
    private OAuthServerAdminClient oAuthServerAdminClient;

    @Mock
    private MessageBrokerProducer mbProducer;

    @Mock
    private TransactionMsClient transactionMsClient;*/


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getMerchant_WithInvalidId_ReturnsFailure() {
        // Setup
        when(merchantRepository.findByAuthId(anyString())).thenReturn(Optional.empty());

        // Execute
        OperationResult<MerchantViewDTO> result = merchantService.getMerchant("invalidAuthId");

        // Assert
        assertFalse(result.isSuccess());
    }

    @Test
    public void updateMerchant_WithInvalidId_ReturnsFailure() {
        // Setup
        when(merchantRepository.findByAuthId(anyString())).thenReturn(Optional.empty());

        MerchantEditDTO dto = new MerchantEditDTO();

        // Execute
        OperationResult<MerchantViewDTO> result = merchantService.updateMerchant("invalidAuthId", dto);

        // Assert
        assertFalse(result.isSuccess());
    }
}
