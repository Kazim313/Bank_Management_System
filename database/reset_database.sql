-- =====================================================
-- Online Banking System - Database Reset Script
-- =====================================================
-- WARNING: This script will delete all data in the online_banking database
-- Use this script only when you want to reset the database to initial state
-- =====================================================

USE online_banking;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Clear all tables
TRUNCATE TABLE transactions;
TRUNCATE TABLE accounts;
TRUNCATE TABLE users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Reset auto-increment counters
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE accounts AUTO_INCREMENT = 1;
ALTER TABLE transactions AUTO_INCREMENT = 1;

-- Re-insert sample data
INSERT INTO users (username, password, first_name, last_name, email, phone_number) VALUES
('john_doe', 'password123', 'John', 'Doe', 'john.doe@email.com', '123-456-7890'),
('jane_smith', 'password456', 'Jane', 'Smith', 'jane.smith@email.com', '098-765-4321'),
('admin', 'admin123', 'Admin', 'User', 'admin@bank.com', '000-000-0000');

INSERT INTO accounts (user_id, account_number, account_type, balance) VALUES
(1, 'ACC001', 'SAVINGS', 5000.00),
(1, 'ACC002', 'CHECKING', 2500.00),
(2, 'ACC003', 'SAVINGS', 7500.00),
(2, 'ACC004', 'CHECKING', 1200.00),
(3, 'ACC005', 'SAVINGS', 10000.00);

INSERT INTO transactions (from_account_id, to_account_id, transaction_type, amount, description, status) VALUES
(1, 2, 'TRANSFER', 500.00, 'Transfer to checking account', 'COMPLETED'),
(2, 3, 'TRANSFER', 1000.00, 'Payment to Jane Smith', 'COMPLETED'),
(3, 4, 'TRANSFER', 750.00, 'Bill payment', 'COMPLETED'),
(1, 3, 'TRANSFER', 200.00, 'Gift money', 'COMPLETED'),
(2, 4, 'TRANSFER', 150.00, 'Shared expense', 'COMPLETED');

-- Verify reset
SELECT 'Database reset completed!' as Status;
SELECT COUNT(*) as 'Total Users' FROM users;
SELECT COUNT(*) as 'Total Accounts' FROM accounts;
SELECT COUNT(*) as 'Total Transactions' FROM transactions;
