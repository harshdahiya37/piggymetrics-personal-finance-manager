package com.finance.manager.piggymetrics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String accountHolderName;
    private String password;
    private String email;
    private Long salary;  // Store fixed salary amount
    private Long budget;  // Current budget
    private boolean autoBudgetEnabled;  // If true, budget updates automatically
    private Long expenses = 0L;  // ✅ Tracks monthly expenses
    private Long savings = 0L;  // ✅ Stores leftover money at month-end
    private Long debt = 0L;  // ✅ Tracks overspending beyond savings & budget
    private Long remainingBudget;
    private boolean autoDebtRepaymentEnabled;  // ✅ Toggle for automatic repayment
    private Long interest = 0L;
    private Long interestRate = 0L;
    private boolean autoInterestPaymentEnabled;
    private Long totalDebt = 0L;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String debtNotification;

}
