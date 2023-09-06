package com.vvkozlov.emerchantpay.transaction;

import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import com.vvkozlov.emerchantpay.transaction.service.job.TransactionCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class TransactionCleanupTest {

    @InjectMocks
    private TransactionCleanup transactionCleanup;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteOldTransactions_whenAlreadyRunning_skipsExecution() {
        // Introduce a small delay when the deleteByDateCreatedBefore method is called
        doAnswer(invocation -> {
            try {
                Thread.sleep(200);  // 2 seconds delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return null;
        }).when(transactionRepository).deleteByDateCreatedBefore(any());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            // Submit tasks to run in different threads
            Future<?> future1 = executor.submit(() -> transactionCleanup.deleteOldTransactions());
            Future<?> future2 = executor.submit(() -> transactionCleanup.deleteOldTransactions());
            future1.get();
            future2.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Thread was interrupted");
        } catch (ExecutionException e) {
            fail("Error occurred while executing the task");
        } finally {
            executor.shutdown();
        }

        // It should call the repository method only once, even though deleteOldTransactions is called twice
        verify(transactionRepository, times(1)).deleteByDateCreatedBefore(any());
    }

    @Test
    void deleteOldTransactions_whenJobCompletes_resetsIsRunningFlag() {
        transactionCleanup.deleteOldTransactions();

        // Call it again to verify that the isRunning flag was reset and the job can run again
        transactionCleanup.deleteOldTransactions();
        verify(transactionRepository, times(2)).deleteByDateCreatedBefore(any());
    }
}
