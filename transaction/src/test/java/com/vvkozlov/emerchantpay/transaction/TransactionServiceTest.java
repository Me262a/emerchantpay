package com.vvkozlov.emerchantpay.transaction;

import com.vvkozlov.emerchantpay.transaction.domain.entities.AbstractTransaction;
import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import com.vvkozlov.emerchantpay.transaction.service.TransactionService;
import com.vvkozlov.emerchantpay.transaction.service.model.AbstractTransactionCreateDTO;
import com.vvkozlov.emerchantpay.transaction.service.model.TransactionViewDTO;
import com.vvkozlov.emerchantpay.transaction.service.processor.TransactionProcessor;
import com.vvkozlov.emerchantpay.transaction.service.processor.TransactionProcessorFactory;
import com.vvkozlov.emerchantpay.transaction.service.util.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionProcessorFactory processorFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransaction_validId_returnsTransaction() {
        UUID testId = UUID.randomUUID();
        AbstractTransaction mockTransaction = mock(AbstractTransaction.class);
        when(transactionRepository.findById(testId)).thenReturn(Optional.of(mockTransaction));

        OperationResult<TransactionViewDTO> result = transactionService.getTransaction(testId);

        assertTrue(result.isSuccess());
        assertNotNull(result.getResult());
    }

    @Test
    void getTransaction_invalidId_returnsFailure() {
        UUID testId = UUID.randomUUID();
        when(transactionRepository.findById(testId)).thenReturn(Optional.empty());

        OperationResult<TransactionViewDTO> result = transactionService.getTransaction(testId);

        assertFalse(result.isSuccess());
        assertNull(result.getResult());
        assertEquals(List.of("Transaction not found"), result.getErrors());
    }

    @Test
    void getTransactions_validPageable_returnsTransactions() {
        Page<AbstractTransaction> mockPage = new PageImpl<>(Collections.singletonList(mock(AbstractTransaction.class)));
        when(transactionRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        OperationResult<Page<TransactionViewDTO>> result = transactionService.getTransactions(PageRequest.of(0, 10));

        assertTrue(result.isSuccess());
        assertNotNull(result.getResult());
        assertEquals(1, result.getResult().getTotalElements());
    }

    @Test
    void transactionsExistForMerchant_validMerchantId_returnsTrue() {
        when(transactionRepository.countByBelongsTo("validMerchantId")).thenReturn(1L);

        boolean exists = transactionService.transactionsExistForMerchant("validMerchantId");

        assertTrue(exists);
    }

    @Test
    void processTransaction_validCreateDto_returnsSuccess() {
        AbstractTransactionCreateDTO createDto = mock(AbstractTransactionCreateDTO.class);
        TransactionProcessor<AbstractTransactionCreateDTO> mockProcessor = mock(TransactionProcessor.class);
        when(processorFactory.getProcessor(createDto)).thenReturn((TransactionProcessor) mockProcessor);
        when(mockProcessor.process(createDto)).thenReturn(OperationResult.success(mock(TransactionViewDTO.class)));

        OperationResult<TransactionViewDTO> result = transactionService.processTransaction(createDto);

        assertTrue(result.isSuccess());
    }

    @Test
    void processTransaction_unknownTransactionType_throwsException() {
        AbstractTransactionCreateDTO createDto = mock(AbstractTransactionCreateDTO.class);
        when(processorFactory.getProcessor(createDto)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transactionService.processTransaction(createDto));
    }
}

