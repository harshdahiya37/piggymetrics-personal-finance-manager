package com.finance.manager.piggymetrics.service;

import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@AllArgsConstructor
public class BudgetSchedulerService {

    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs at midnight on the 1st of every month
    @Transactional
    public void updateBudgetAutomatically() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.isAutoBudgetEnabled()) {
                account.setBudget(account.getSalary());  // ✅ Auto-reset budget to salary
                accountRepository.save(account);
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs at midnight on the 1st day of every month
    @Transactional
    public void resetMonthlyBudget() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.getRemainingBudget() > 0) {
                account.setSavings(account.getSavings() + account.getRemainingBudget());
            }
            account.setRemainingBudget(account.getBudget());
            account.setExpenses(0L);
            accountRepository.save(account);
        }
    }
}
