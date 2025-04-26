package com.finance.manager.piggymetrics.controller;

import com.finance.manager.piggymetrics.dto.LoginRequest;
import com.finance.manager.piggymetrics.dto.SignupRequest;

import com.finance.manager.piggymetrics.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public String signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }
}
