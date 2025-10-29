package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import java.math.BigDecimal;
import java.util.List;

public class BankingService {
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public BankingService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public List<Account> getUserAccounts(int userId) {
        return accountDAO.getAccountsByUserId(userId);
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountDAO.getAccountByAccountNumber(accountNumber);
    }

    public boolean transferFunds(int fromAccountId, int toAccountId, BigDecimal amount, String description) {
        try {
            // Get accounts
            Account fromAccount = accountDAO.getAccountsByUserId(0).stream()
                .filter(acc -> acc.getAccountId() == fromAccountId)
                .findFirst()
                .orElse(null);
            
            Account toAccount = accountDAO.getAccountsByUserId(0).stream()
                .filter(acc -> acc.getAccountId() == toAccountId)
                .findFirst()
                .orElse(null);

            if (fromAccount == null || toAccount == null) {
                return false;
            }

            // Check sufficient balance
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                return false;
            }

            // Create transaction
            Transaction transaction = new Transaction(fromAccountId, toAccountId, "TRANSFER", amount, description);
            boolean transactionCreated = transactionDAO.createTransaction(transaction);

            if (!transactionCreated) {
                return false;
            }

            // Update balances
            BigDecimal newFromBalance = fromAccount.getBalance().subtract(amount);
            BigDecimal newToBalance = toAccount.getBalance().add(amount);

            boolean fromUpdated = accountDAO.updateAccountBalance(fromAccountId, newFromBalance.doubleValue());
            boolean toUpdated = accountDAO.updateAccountBalance(toAccountId, newToBalance.doubleValue());

            if (fromUpdated && toUpdated) {
                transactionDAO.updateTransactionStatus(transaction.getTransactionId(), "COMPLETED");
                return true;
            } else {
                transactionDAO.updateTransactionStatus(transaction.getTransactionId(), "FAILED");
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error in transfer: " + e.getMessage());
            return false;
        }
    }

    public List<Transaction> getAccountTransactions(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    public boolean createAccount(int userId, String accountType) {
        String accountNumber = generateAccountNumber();
        Account account = new Account(userId, accountNumber, accountType, BigDecimal.ZERO);
        return accountDAO.createAccount(account);
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
}
