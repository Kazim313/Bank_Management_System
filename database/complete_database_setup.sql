-- =====================================================
-- Online Banking System Database Setup Script
-- =====================================================
-- This script creates the complete database structure
-- for the Online Banking System application
-- =====================================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS online_banking;
USE online_banking;

-- =====================================================
-- Drop existing tables (in correct order due to foreign keys)
-- =====================================================
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

-- =====================================================
-- Create Users Table
-- =====================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Indexes for better performance
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_active (is_active)
);

-- =====================================================
-- Create Accounts Table
-- =====================================================
CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('SAVINGS', 'CHECKING', 'BUSINESS')),
    balance DECIMAL(15,2) DEFAULT 0.00 CHECK (balance >= 0),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Foreign key constraint
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    
    -- Indexes for better performance
    INDEX idx_user_id (user_id),
    INDEX idx_account_number (account_number),
    INDEX idx_account_type (account_type),
    INDEX idx_active_accounts (is_active)
);

-- =====================================================
-- Create Transactions Table
-- =====================================================
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    from_account_id INT,
    to_account_id INT,
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('TRANSFER', 'DEPOSIT', 'WITHDRAWAL', 'PAYMENT')),
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    description TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED')),
    
    -- Foreign key constraints
    FOREIGN KEY (from_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL,
    FOREIGN KEY (to_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL,
    
    -- Indexes for better performance
    INDEX idx_from_account (from_account_id),
    INDEX idx_to_account (to_account_id),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_status (status)
);

-- =====================================================
-- Insert Sample Data
-- =====================================================

-- Insert sample users
INSERT INTO users (username, password, first_name, last_name, email, phone_number) VALUES
('john_doe', 'password123', 'John', 'Doe', 'john.doe@email.com', '123-456-7890'),
('jane_smith', 'password456', 'Jane', 'Smith', 'jane.smith@email.com', '098-765-4321'),
('mike_wilson', 'password789', 'Mike', 'Wilson', 'mike.wilson@email.com', '555-123-4567'),
('sarah_jones', 'password321', 'Sarah', 'Jones', 'sarah.jones@email.com', '444-987-6543');

-- Insert sample accounts
INSERT INTO accounts (user_id, account_number, account_type, balance) VALUES
-- John Doe's accounts
(1, 'ACC001', 'SAVINGS', 5000.00),
(1, 'ACC002', 'CHECKING', 2500.00),

-- Jane Smith's accounts
(2, 'ACC003', 'SAVINGS', 7500.00),
(2, 'ACC004', 'CHECKING', 1200.00),

-- Mike Wilson's accounts
(3, 'ACC005', 'SAVINGS', 3200.00),
(3, 'ACC006', 'BUSINESS', 15000.00),

-- Sarah Jones's accounts
(4, 'ACC007', 'SAVINGS', 8900.00),
(4, 'ACC008', 'CHECKING', 3400.00);

-- Insert sample transactions
INSERT INTO transactions (from_account_id, to_account_id, transaction_type, amount, description, status) VALUES
-- Completed transfers
(1, 2, 'TRANSFER', 500.00, 'Transfer to checking account', 'COMPLETED'),
(2, 3, 'TRANSFER', 1000.00, 'Payment to Jane Smith', 'COMPLETED'),
(3, 4, 'TRANSFER', 750.00, 'Bill payment', 'COMPLETED'),
(5, 6, 'TRANSFER', 2000.00, 'Business expense', 'COMPLETED'),

-- Recent transactions
(7, 8, 'TRANSFER', 300.00, 'Monthly allowance', 'COMPLETED'),
(1, 3, 'TRANSFER', 200.00, 'Gift money', 'COMPLETED'),
(2, 4, 'TRANSFER', 150.00, 'Shared expense', 'COMPLETED'),

-- Pending transaction
(5, 7, 'TRANSFER', 500.00, 'Loan repayment', 'PENDING');

-- =====================================================
-- Create Views for Common Queries
-- =====================================================

-- View for user account summary
CREATE VIEW user_account_summary AS
SELECT 
    u.user_id,
    u.username,
    u.first_name,
    u.last_name,
    COUNT(a.account_id) as total_accounts,
    SUM(a.balance) as total_balance,
    MAX(a.created_date) as latest_account_date
FROM users u
LEFT JOIN accounts a ON u.user_id = a.user_id AND a.is_active = TRUE
WHERE u.is_active = TRUE
GROUP BY u.user_id, u.username, u.first_name, u.last_name;

-- View for recent transactions
CREATE VIEW recent_transactions AS
SELECT 
    t.transaction_id,
    t.transaction_date,
    t.transaction_type,
    t.amount,
    t.description,
    t.status,
    fa.account_number as from_account,
    ta.account_number as to_account,
    CONCAT(uf.first_name, ' ', uf.last_name) as from_user,
    CONCAT(ut.first_name, ' ', ut.last_name) as to_user
FROM transactions t
LEFT JOIN accounts fa ON t.from_account_id = fa.account_id
LEFT JOIN accounts ta ON t.to_account_id = ta.account_id
LEFT JOIN users uf ON fa.user_id = uf.user_id
LEFT JOIN users ut ON ta.user_id = ut.user_id
ORDER BY t.transaction_date DESC;

-- =====================================================
-- Create Stored Procedures
-- =====================================================

DELIMITER //

-- Procedure to transfer funds between accounts
CREATE PROCEDURE TransferFunds(
    IN p_from_account_id INT,
    IN p_to_account_id INT,
    IN p_amount DECIMAL(15,2),
    IN p_description TEXT
)
BEGIN
    DECLARE v_from_balance DECIMAL(15,2);
    DECLARE v_to_balance DECIMAL(15,2);
    DECLARE v_transaction_id INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Check if from account has sufficient balance
    SELECT balance INTO v_from_balance 
    FROM accounts 
    WHERE account_id = p_from_account_id AND is_active = TRUE;
    
    IF v_from_balance < p_amount THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient balance';
    END IF;
    
    -- Create transaction record
    INSERT INTO transactions (from_account_id, to_account_id, transaction_type, amount, description, status)
    VALUES (p_from_account_id, p_to_account_id, 'TRANSFER', p_amount, p_description, 'PENDING');
    
    SET v_transaction_id = LAST_INSERT_ID();
    
    -- Update balances
    UPDATE accounts 
    SET balance = balance - p_amount 
    WHERE account_id = p_from_account_id;
    
    UPDATE accounts 
    SET balance = balance + p_amount 
    WHERE account_id = p_to_account_id;
    
    -- Mark transaction as completed
    UPDATE transactions 
    SET status = 'COMPLETED' 
    WHERE transaction_id = v_transaction_id;
    
    COMMIT;
END //

-- Procedure to get user transaction history
CREATE PROCEDURE GetUserTransactions(
    IN p_user_id INT,
    IN p_limit INT
)
BEGIN
    SELECT 
        t.transaction_id,
        t.transaction_date,
        t.transaction_type,
        t.amount,
        t.description,
        t.status,
        fa.account_number as from_account,
        ta.account_number as to_account
    FROM transactions t
    LEFT JOIN accounts fa ON t.from_account_id = fa.account_id
    LEFT JOIN accounts ta ON t.to_account_id = ta.account_id
    WHERE (fa.user_id = p_user_id OR ta.user_id = p_user_id)
    ORDER BY t.transaction_date DESC
    LIMIT p_limit;
END //

DELIMITER ;

-- =====================================================
-- Create Triggers
-- =====================================================

-- Trigger to update account balance after transaction completion
DELIMITER //
CREATE TRIGGER after_transaction_completed
AFTER UPDATE ON transactions
FOR EACH ROW
BEGIN
    IF NEW.status = 'COMPLETED' AND OLD.status != 'COMPLETED' THEN
        -- Update from account balance
        IF NEW.from_account_id IS NOT NULL THEN
            UPDATE accounts 
            SET balance = balance - NEW.amount 
            WHERE account_id = NEW.from_account_id;
        END IF;
        
        -- Update to account balance
        IF NEW.to_account_id IS NOT NULL THEN
            UPDATE accounts 
            SET balance = balance + NEW.amount 
            WHERE account_id = NEW.to_account_id;
        END IF;
    END IF;
END //
DELIMITER ;

-- =====================================================
-- Grant Permissions (if using custom user)
-- =====================================================
-- Uncomment and modify if you want to create a specific database user
-- CREATE USER 'banking_user'@'localhost' IDENTIFIED BY 'banking_password';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON online_banking.* TO 'banking_user'@'localhost';
-- GRANT EXECUTE ON PROCEDURE online_banking.TransferFunds TO 'banking_user'@'localhost';
-- GRANT EXECUTE ON PROCEDURE online_banking.GetUserTransactions TO 'banking_user'@'localhost';
-- FLUSH PRIVILEGES;

-- =====================================================
-- Verification Queries
-- =====================================================

-- Check if all tables were created successfully
SELECT 'Tables created:' as Status;
SHOW TABLES;

-- Check sample data
SELECT 'Users count:' as Info, COUNT(*) as Count FROM users;
SELECT 'Accounts count:' as Info, COUNT(*) as Count FROM accounts;
SELECT 'Transactions count:' as Info, COUNT(*) as Count FROM transactions;

-- Display sample data
SELECT 'Sample Users:' as Info;
SELECT user_id, username, first_name, last_name, email FROM users LIMIT 3;

SELECT 'Sample Accounts:' as Info;
SELECT account_id, account_number, account_type, balance FROM accounts LIMIT 5;

SELECT 'Sample Transactions:' as Info;
SELECT transaction_id, transaction_type, amount, status FROM transactions LIMIT 5;

-- =====================================================
-- Database Setup Complete
-- =====================================================
SELECT 'Database setup completed successfully!' as Status;
