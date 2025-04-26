package com.finance.manager.piggymetrics.service;

import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@AllArgsConstructor
public class SavingSchedulerService {

    private AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs at midnight on the 1st of every month
    @Transactional
    public void processMonthEnd() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.getRemainingBudget() > 0) {
                account.setSavings(account.getSavings() + account.getRemainingBudget()); // ✅ Add leftover budget to savings
            }
            account.setRemainingBudget(account.getBudget()); // ✅ Reset remaining budget
            account.setExpenses(0L);  // ✅ Reset expenses
            accountRepository.save(account);
        }
    }
}
