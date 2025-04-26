package com.finance.manager.piggymetrics.dto;

import com.finance.manager.piggymetrics.entity.UserRole;
import lombok.Data;

@Data
public class SignupRequest {
    private String accountHolderName;
    private String email;
    private String password;
    private UserRole role;
}
