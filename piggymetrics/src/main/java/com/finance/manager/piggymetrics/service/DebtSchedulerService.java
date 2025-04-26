package com.finance.manager.piggymetrics.service;

import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@AllArgsConstructor
public class DebtSchedulerService {

    private AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs at midnight on the 1st of the month
    @Transactional
    public void processDebtRepayment() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.isAutoDebtRepaymentEnabled()) {
                Long debtAmount = account.getDebt();
                if (account.getRemainingBudget() >= debtAmount) {
                    account.setRemainingBudget(account.getRemainingBudget() - debtAmount);
                    account.setDebt(0L);  // ✅ Debt cleared
                } else {
                    account.setDebt(account.getDebt() - account.getRemainingBudget());
                    account.setRemainingBudget(0L);  // ✅ Remaining budget exhausted
                }
                accountRepository.save(account);
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs monthly
    @Transactional
    public void applyInterestOnDebt() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.getDebt() > 0) {
                Long interestAmount = (long) (account.getDebt() * account.getInterestRate()); // ✅ Calculate interest
                account.setInterest(interestAmount);  // ✅ Store interest amount
                account.setTotalDebt(account.getDebt() + interestAmount);  // ✅ Update total debt
            }
            accountRepository.save(account);
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs at month-end
    @Transactional
    public void autoInterestPayment() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.isAutoInterestPaymentEnabled() && account.getInterest() > 0) {
                if (account.getRemainingBudget() >= account.getInterest()) {
                    account.setRemainingBudget(account.getRemainingBudget() - account.getInterest());
                    account.setInterest(0L);  // ✅ Interest cleared
                } else {
                    account.setInterest(account.getInterest() - account.getRemainingBudget());
                    account.setRemainingBudget(0L);  // ✅ Remaining budget exhausted
                }
                accountRepository.save(account);
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")  // Runs at 8 AM daily
    @Transactional
    public void checkDebtWarnings() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.getDebt() > (account.getBudget() * 0.5)) {
                account.setDebtNotification("⚠️ Warning: Your debt has exceeded 50% of your budget.");
            }

            if (account.getDebt() > account.getSavings()) {
                account.setDebtNotification("⚠️ Critical Alert: Your debt is now greater than your savings!");
            }

            accountRepository.save(account);
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")  // Runs on the 1st of each month
    @Transactional
    public void detectMissedPayments() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (!account.isAutoDebtRepaymentEnabled() && account.getDebt() > 0) {
                account.setDebtNotification("⚠️ Reminder: You missed a debt payment last month!");
            }
            accountRepository.save(account);
        }
    }

}
