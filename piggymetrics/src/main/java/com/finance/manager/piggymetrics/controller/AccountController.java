package com.finance.manager.piggymetrics.controller;

import com.finance.manager.piggymetrics.dto.AccountUpdateRequest;
import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.repository.AccountRepository;
import com.finance.manager.piggymetrics.security.JwtUtil;
import com.finance.manager.piggymetrics.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            Optional<Account> existingAccount = accountService.findByEmail(account.getEmail());
            if (existingAccount.isPresent()) {
                return ResponseEntity.badRequest().body("Email already registered");
            }
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        Account savedAccount = accountService.save(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/account")
    public ResponseEntity<String> updateOwnAccount(@RequestBody AccountUpdateRequest updateRequest, HttpServletRequest request) throws AccessDeniedException, AccountNotFoundException {
        String token = jwtUtil.extractTokenFromHeader(request.getHeader("Authorization"));
        String email = jwtUtil.extractEmail(token);

        Account account = accountService.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!account.getEmail().equals(email)) {
            throw new AccessDeniedException("You are not allowed to update this account.");
        }

        if (updateRequest.getBudget() != null) {
            account.setBudget(updateRequest.getBudget());
        }

        account.setExpenses(updateRequest.getExpenses());
        accountService.save(account);

        return ResponseEntity.ok("Account updated successfully");
    }

    @PutMapping("/account/update-salary")
    public ResponseEntity<String> updateSalary(@RequestParam Long salary, Principal principal) throws AccountNotFoundException {
        Account account = accountService.findByEmail(principal.getName())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        account.setSalary(salary);

        if (account.isAutoBudgetEnabled()) {
            account.setBudget(salary);  // ✅ Auto-update budget when salary changes
        }

        accountService.save(account);
        return ResponseEntity.ok("Salary updated successfully.");
    }

    @PutMapping("/account/set-budget")
    public ResponseEntity<String> setBudget(@RequestParam Long budget, Principal principal) throws AccountNotFoundException {
        Account account = accountService.findByEmail(principal.getName())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        account.setBudget(budget);
        account.setAutoBudgetEnabled(false);  // ✅ Disables auto-budget mode if set manually

        accountService.save(account);
        return ResponseEntity.ok("Budget updated successfully.");
    }

    @PutMapping("/account/update-expenses")
    public ResponseEntity<String> updateExpenses(@RequestParam Long newExpense, Principal principal) throws AccountNotFoundException {
        Account account = accountService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountService.updateExpenses(newExpense, account);
        return ResponseEntity.ok("Expense updated successfully.");
    }

    @PutMapping("/account/pay-debt")
    public ResponseEntity<String> payDebt(@RequestParam Long debtPayment, Principal principal) throws AccountNotFoundException {
        Account account = accountService.findByEmail(principal.getName())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (account.getDebt() == 0) {
            return ResponseEntity.badRequest().body("No outstanding debt to repay.");
        }

        if (debtPayment > account.getRemainingBudget()) {
            return ResponseEntity.badRequest().body("Insufficient budget to repay debt.");
        }

        account.setDebt(account.getDebt() - debtPayment);
        account.setRemainingBudget(account.getRemainingBudget() - debtPayment);
        accountRepository.save(account);

        return ResponseEntity.ok("Debt payment successful.");
    }

}
