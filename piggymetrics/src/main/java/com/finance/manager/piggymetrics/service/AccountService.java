package com.finance.manager.piggymetrics.service;

import com.finance.manager.piggymetrics.entity.Account;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
public interface AccountService {
    Optional<Account> getAccountById(Long id);
    Account save(Account account);
    Optional<Account> findByEmail(String email) throws AccountNotFoundException;
    void updateExpenses(Long newExpense, Account account);
}
