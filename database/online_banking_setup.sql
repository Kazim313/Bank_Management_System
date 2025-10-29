-- =====================================================
-- Online Banking System - Database Backup Script
-- =====================================================
-- This script creates a backup of the online_banking database
-- Run this script to backup your database before making changes
-- =====================================================

-- Create backup database
CREATE DATABASE IF NOT EXISTS online_banking_backup;

-- Backup Users table
CREATE TABLE online_banking_backup.users AS 
SELECT * FROM online_banking.users;

-- Backup Accounts table
CREATE TABLE online_banking_backup.accounts AS 
SELECT * FROM online_banking.accounts;

-- Backup Transactions table
CREATE TABLE online_banking_backup.transactions AS 
SELECT * FROM online_banking.transactions;

-- Verify backup
SELECT 'Backup completed successfully!' as Status;
SELECT COUNT(*) as 'Backed up Users' FROM online_banking_backup.users;
SELECT COUNT(*) as 'Backed up Accounts' FROM online_banking_backup.accounts;
SELECT COUNT(*) as 'Backed up Transactions' FROM online_banking_backup.transactions;
