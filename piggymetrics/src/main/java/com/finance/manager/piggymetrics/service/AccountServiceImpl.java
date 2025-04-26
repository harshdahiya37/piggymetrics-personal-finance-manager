package com.finance.manager.piggymetrics.service;

import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void updateExpenses(Long newExpense, Account account) {
        account.setExpenses(account.getExpenses() + newExpense);

        if (account.getRemainingBudget() >= newExpense) {
            account.setRemainingBudget(account.getRemainingBudget() - newExpense);
        } else {
            Long deficit = newExpense - account.getRemainingBudget();
            account.setRemainingBudget(0L);

            if (account.getSavings() >= deficit) {
                account.setSavings(account.getSavings() - deficit);
            } else {
                Long debtAmount = deficit - account.getSavings();
                account.setSavings(0L);
                account.setDebt(account.getDebt() + debtAmount);
            }
        }

        accountRepository.save(account);
    }
}
