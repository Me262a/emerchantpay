package com.vvkozlov.emerchantpay.merchant.unit;

import com.vvkozlov.emerchantpay.merchant.domain.constants.MerchantStatusEnum;
import com.vvkozlov.emerchantpay.merchant.domain.entities.Merchant;
import com.vvkozlov.emerchantpay.merchant.infra.repository.MerchantRepository;
import com.vvkozlov.emerchantpay.merchant.service.MerchantService;
import com.vvkozlov.emerchantpay.merchant.service.contract.MessageBrokerProducer;
import com.vvkozlov.emerchantpay.merchant.service.contract.OAuthServerAdminClient;
import com.vvkozlov.emerchantpay.merchant.service.contract.TransactionMsClient;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantEditDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.MerchantViewDTO;
import com.vvkozlov.emerchantpay.merchant.service.model.messagebroker.TransactionMbModel;
import com.vvkozlov.emerchantpay.merchant.service.util.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTests {
    @InjectMocks
    private MerchantService merchantService;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private MessageBrokerProducer mbProducer;
    @Mock
    private TransactionMsClient transactionMsClient;
    @Mock
    private OAuthServerAdminClient oAuthServerAdminClient;
    @BeforeEach
    public void setUp() {

    }

    @Test
    public void getMerchant_WithInvalidId_ReturnsFailure() {
        OperationResult<MerchantViewDTO> result = merchantService.getMerchant("invalidAuthId");
        assertFalse(result.isSuccess());
    }

    @Test
    public void updateMerchant_WithInvalidId_ReturnsFailure() {
        MerchantEditDTO dto = new MerchantEditDTO();
        OperationResult<MerchantViewDTO> result = merchantService.updateMerchant("invalidAuthId", dto);
        assertFalse(result.isSuccess());
    }

    @Test
    public void getMerchants_ReturnsListOfMerchants() {
        Page<Merchant> pageMock = mock(Page.class);
        when(merchantRepository.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(pageMock.stream()).thenReturn(Stream.of(new Merchant()));

        OperationResult<Page<MerchantViewDTO>> result = merchantService.getMerchants(PageRequest.of(0, 5));
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResult().getTotalElements());
    }

    @Test
    public void updateMerchant_StatusChange_SendsMessage() {
        Merchant merchant = new Merchant();
        merchant.setStatus(MerchantStatusEnum.ACTIVE);
        Merchant savedMerchant = new Merchant();
        savedMerchant.setStatus(MerchantStatusEnum.INACTIVE);
        savedMerchant.setAuthId("someString");

        when(merchantRepository.findByAuthId(anyString())).thenReturn(Optional.of(merchant));
        when(merchantRepository.save(any())).thenReturn(savedMerchant);

        MerchantEditDTO dto = new MerchantEditDTO();
        dto.setStatus(MerchantStatusEnum.INACTIVE);

        merchantService.updateMerchant("validAuthId", dto);
        verify(mbProducer).sendMessage(anyString(), anyBoolean());
    }

    @Test
    public void addTransactionAmountToMerchant_UpdatesTotalCorrectly() {
        Merchant merchant = new Merchant();
        merchant.setTotalTransactionSum(BigDecimal.ZERO);

        TransactionMbModel tranMbModel = new TransactionMbModel();
        tranMbModel.setAmount(BigDecimal.TEN);
        tranMbModel.setBelongsTo("validAuthId");  // Setting this value to ensure it's not null

        when(merchantRepository.findByAuthId("validAuthId")).thenReturn(Optional.of(merchant));
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        OperationResult<MerchantViewDTO> result = merchantService.addTransactionAmountToMerchant(tranMbModel);

        assertTrue(result.isSuccess());
        assertEquals(BigDecimal.TEN, result.getResult().getTotalTransactionSum());
    }


    @Test
    public void updateMerchant_NoStatusChange_DoesNotSendMessage() {
        Merchant merchant = new Merchant();
        merchant.setStatus(MerchantStatusEnum.ACTIVE);

        when(merchantRepository.findByAuthId(anyString())).thenReturn(Optional.of(merchant));

        MerchantEditDTO dto = new MerchantEditDTO();
        dto.setStatus(MerchantStatusEnum.ACTIVE); // Same status

        merchantService.updateMerchant("validAuthId", dto);

        verify(mbProducer, never()).sendMessage(anyString(), anyBoolean());
    }

    @Test
    public void updateMerchant_OtherAttributesChange_DoesNotSendMessage() {
        Merchant merchant = new Merchant();
        merchant.setStatus(MerchantStatusEnum.ACTIVE);

        when(merchantRepository.findByAuthId(anyString())).thenReturn(Optional.of(merchant));

        MerchantEditDTO dto = new MerchantEditDTO();
        dto.setStatus(MerchantStatusEnum.ACTIVE);
        dto.setName("newValue");
        dto.setDescription("newValue");
        dto.setEmail("newValue");

        merchantService.updateMerchant("validAuthId", dto);

        verify(mbProducer, never()).sendMessage(anyString(), anyBoolean());
    }

    @Test
    public void removeMerchantById_NoActiveTransactions_RemovesMerchant() {
        when(transactionMsClient.getDoesMerchantHaveTransactions(anyString()))
                .thenReturn(OperationResult.success(false));

        merchantService.removeMerchantById("validMerchantId");

        verify(merchantRepository).deleteByAuthId("validMerchantId");
    }

    @Test
    public void removeMerchantById_WithActiveTransactions_DoesNotRemoveMerchant() {
        when(transactionMsClient.getDoesMerchantHaveTransactions(anyString()))
                .thenReturn(OperationResult.success(true));

        merchantService.removeMerchantById("validMerchantId");

        verify(merchantRepository, never()).deleteByAuthId(anyString());
    }

    @Test
    public void removeAllMerchants_NoActiveTransactionsAndCheckIsTrue_RemovesAllMerchants() {
        when(merchantRepository.findAll()).thenReturn(Collections.emptyList());
        when(oAuthServerAdminClient.removeAllUsersWithRole(anyString()))
                .thenReturn(OperationResult.success(Collections.emptyList()));

        merchantService.removeAllMerchants(true);

        verify(merchantRepository).deleteAllInBatch();
    }

    @Test
    public void removeAllMerchants_WithActiveTransactionsAndCheckIsTrue_DoesNotRemoveAllMerchants() {
        merchantService.removeAllMerchants(true);

        verify(merchantRepository, never()).deleteAllInBatch();
    }

    @Test
    public void removeAllMerchants_CheckIsFalse_RemovesAllMerchantsRegardlessOfTransactionStatus() {
        when(oAuthServerAdminClient.removeAllUsersWithRole(anyString()))
                .thenReturn(OperationResult.success(Collections.emptyList()));

        merchantService.removeAllMerchants(false);
        verify(merchantRepository).deleteAllInBatch();
    }
}
