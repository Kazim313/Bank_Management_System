package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int fromAccountId;
    private int toAccountId;
    private String transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private String status;

    public Transaction() {}

    public Transaction(int fromAccountId, int toAccountId, String transactionType, 
                     BigDecimal amount, String description) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.transactionDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(int fromAccountId) { this.fromAccountId = fromAccountId; }

    public int getToAccountId() { return toAccountId; }
    public void setToAccountId(int toAccountId) { this.toAccountId = toAccountId; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
