package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private int userId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private LocalDateTime createdDate;
    private boolean isActive;

    public Account() {}

    public Account(int userId, String accountNumber, String accountType, BigDecimal balance) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.createdDate = LocalDateTime.now();
        this.isActive = true;
    }

    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
