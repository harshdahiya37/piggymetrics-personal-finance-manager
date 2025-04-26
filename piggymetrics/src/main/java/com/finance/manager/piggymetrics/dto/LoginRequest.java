package com.finance.manager.piggymetrics.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginRequest {

    @NonNull
    private String email;

    @NonNull
    private String password;

}
