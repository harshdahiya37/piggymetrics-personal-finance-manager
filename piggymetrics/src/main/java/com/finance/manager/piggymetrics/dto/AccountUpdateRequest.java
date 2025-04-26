package com.finance.manager.piggymetrics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateRequest {
    private Long budget;
    private Long expenses;
}
