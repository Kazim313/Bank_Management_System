-- Online Banking System Database Schema
-- Run this script to create the database and tables

CREATE DATABASE IF NOT EXISTS online_banking;
USE online_banking;

-- Users table
CREATE TABLE IF NOT EXISTS users (
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

-- Accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
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

-- Insert sample data
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
