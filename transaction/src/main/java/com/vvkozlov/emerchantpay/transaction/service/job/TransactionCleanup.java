package com.vvkozlov.emerchantpay.transaction.service.job;

import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TransactionCleanup {

    private final TransactionRepository transactionRepository;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public TransactionCleanup(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void deleteOldTransactions() {
        if (isRunning.getAndSet(true)) {
            System.out.println("Transaction cleanup is already running. Skipping execution.");
            return;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, -1);
            Date oneHourAgo = calendar.getTime();

            // If dealing with very large data, we can also consider using pagination to delete in batches
            transactionRepository.deleteByDateCreatedBefore(oneHourAgo);
        } finally {
            isRunning.set(false);
            System.out.println("Transaction cleanup has completed execution.");
        }
    }
}
