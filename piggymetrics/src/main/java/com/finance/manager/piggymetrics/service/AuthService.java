package com.finance.manager.piggymetrics.service;

import com.finance.manager.piggymetrics.dto.LoginRequest;
import com.finance.manager.piggymetrics.dto.SignupRequest;
import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.repository.AccountRepository;
import com.finance.manager.piggymetrics.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtil.generateToken(account.getEmail(), account.getRole().name());
    }

    public String signup(SignupRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setAccountHolderName(request.getAccountHolderName());
        account.setRole(request.getRole());

        Account saved = accountRepository.save(account);
        return jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());
    }
}
