package com.finance.manager.piggymetrics.controller;

import com.finance.manager.piggymetrics.entity.Account;
import com.finance.manager.piggymetrics.security.JwtUtil;

public class LoginController {

    private JwtUtil jwtUtil;

    private Account account;

    String token = jwtUtil.generateToken(account.getEmail(), account.getRole().name());

}
