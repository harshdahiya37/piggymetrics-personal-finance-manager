package com.finance.manager.piggymetrics.service;

import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@AllArgsConstructor
public class ExpenseSchedulerService {

    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs at midnight on the 1st of the month
    @Transactional
    public void resetMonthlyExpenses() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            account.setExpenses(0L);  // ✅ Reset expenses
            accountRepository.save(account);
        }
    }
}
