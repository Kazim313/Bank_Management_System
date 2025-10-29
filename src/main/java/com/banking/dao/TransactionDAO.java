package com.banking.dao;

import com.banking.database.DatabaseConnection;
import com.banking.model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private DatabaseConnection dbConnection;

    public TransactionDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (from_account_id, to_account_id, transaction_type, amount, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, transaction.getFromAccountId());
            stmt.setInt(2, transaction.getToAccountId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setString(5, transaction.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
        }
        return false;
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ? ORDER BY transaction_date DESC LIMIT 20";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
        }
        return transactions;
    }

    public boolean updateTransactionStatus(int transactionId, String status) {
        String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, transactionId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating transaction status: " + e.getMessage());
            return false;
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setFromAccountId(rs.getInt("from_account_id"));
        transaction.setToAccountId(rs.getInt("to_account_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setDescription(rs.getString("description"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        transaction.setStatus(rs.getString("status"));
        return transaction;
    }
}
