-- =====================================================
-- Online Banking System - Setup With Custom Database Name
-- =====================================================
-- How to use:
-- 1) Replace all occurrences of __DB_NAME__ with your desired database name
--    (e.g., MyBankDB) before running, OR use the PowerShell one-liner below.
-- 2) Then run the script in MySQL.
--
-- PowerShell example (Windows):
--   (Get-Content database/setup_with_dbname.sql).Replace('__DB_NAME__','MyBankDB') | mysql -u root -p
--
-- Bash example (macOS/Linux):
--   sed "s/__DB_NAME__/MyBankDB/g" database/setup_with_dbname.sql | mysql -u root -p
-- =====================================================

CREATE DATABASE IF NOT EXISTS __DB_NAME__;
USE __DB_NAME__;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    from_account_id INT,
    to_account_id INT,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (from_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL,
    FOREIGN KEY (to_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL
);

-- Sample data (you can remove if not needed)
INSERT INTO users (username, password, first_name, last_name, email, phone_number) VALUES
('john_doe', 'password123', 'John', 'Doe', 'john.doe@email.com', '123-456-7890'),
('jane_smith', 'password456', 'Jane', 'Smith', 'jane.smith@email.com', '098-765-4321');

INSERT INTO accounts (user_id, account_number, account_type, balance) VALUES
(1, 'ACC001', 'SAVINGS', 5000.00),
(1, 'ACC002', 'CHECKING', 2500.00),
(2, 'ACC003', 'SAVINGS', 7500.00);

INSERT INTO transactions (from_account_id, to_account_id, transaction_type, amount, description, status) VALUES
(1, 2, 'TRANSFER', 500.00, 'Transfer to checking account', 'COMPLETED'),
(2, 3, 'TRANSFER', 1000.00, 'Payment to Jane Smith', 'COMPLETED');

SELECT 'Setup completed for __DB_NAME__' as Status;


