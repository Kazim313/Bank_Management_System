package com.banking.dao;

import com.banking.database.DatabaseConnection;
import com.banking.model.Account;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private DatabaseConnection dbConnection;

    public AccountDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public List<Account> getAccountsByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ? AND is_active = TRUE";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting accounts: " + e.getMessage());
        }
        return accounts;
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ? AND is_active = TRUE";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
        }
        return null;
    }

    public boolean updateAccountBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating account balance: " + e.getMessage());
            return false;
        }
    }

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (user_id, account_number, account_type, balance) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, account.getUserId());
            stmt.setString(2, account.getAccountNumber());
            stmt.setString(3, account.getAccountType());
            stmt.setBigDecimal(4, account.getBalance());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
        account.setActive(rs.getBoolean("is_active"));
        return account;
    }
}
