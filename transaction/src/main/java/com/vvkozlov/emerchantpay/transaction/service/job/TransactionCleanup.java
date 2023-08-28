package com.vvkozlov.emerchantpay.transaction.service.job;

import com.vvkozlov.emerchantpay.transaction.infra.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;

@Service
public class TransactionCleanup {

    private final TransactionRepository transactionRepository;

    public TransactionCleanup(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void deleteOldTransactions() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        Date oneHourAgo = calendar.getTime();

        transactionRepository.deleteByDateCreatedBefore(oneHourAgo);
        System.out.println("Transaction cleanup was run");
    }
}
