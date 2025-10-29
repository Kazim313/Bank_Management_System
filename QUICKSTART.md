# Online Banking System - Quick Start Guide

## Prerequisites
- Java JDK 11 or higher
- MySQL Server 8.0 or higher
- Maven 3.6 or higher

## Quick Setup

### 1. Database Setup
```bash
# Start MySQL server
mysql -u root -p

# Create database and tables
source database/schema.sql
```

### 2. Configure Database Connection
Edit `src/main/java/com/banking/database/DatabaseConnection.java`:
```java
private static final String USERNAME = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

### 3. Build and Run
```bash
# Build project
mvn clean compile

# Run application
mvn exec:java -Dexec.mainClass="com.banking.BankingApplication"
```

## Sample Login Credentials
- **Username**: john_doe, **Password**: password123
- **Username**: jane_smith, **Password**: password456

## Features Overview
- ✅ User Registration & Login
- ✅ Account Management
- ✅ Fund Transfers
- ✅ Transaction History
- ✅ Clean UI Design
- ✅ Database Integration

## Project Structure
```
src/main/java/com/banking/
├── BankingApplication.java     # Main entry point
├── model/                      # Data models
├── dao/                        # Database access
├── service/                    # Business logic
├── ui/                         # User interface
└── database/                   # DB utilities
```

## Troubleshooting
- **Connection Error**: Check MySQL credentials and server status
- **Class Not Found**: Ensure MySQL Connector JAR is in classpath
- **Build Error**: Verify Java and Maven versions

For detailed documentation, see README.md
