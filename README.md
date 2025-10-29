# Online Banking System

A comprehensive online banking application built with Java Swing and MySQL database, featuring user authentication, account management, fund transfers, and transaction history.

## Features

### 🔐 User Authentication
- Secure login system with username/password validation
- User registration with comprehensive validation
- Email and username uniqueness checks
- Password strength requirements

### 💳 Account Management
- View multiple account types (Savings, Checking)
- Real-time balance display
- Account creation and management
- Account number generation

### 💸 Fund Transfer
- Transfer money between accounts
- Real-time balance validation
- Transaction confirmation dialogs
- Secure transaction processing

### 📊 Transaction History
- View recent transactions
- Transaction status tracking
- Detailed transaction information
- Chronological transaction display

## Technology Stack

- **Frontend**: Java Swing
- **Backend**: Java 11+
- **Database**: MySQL 8.0+
- **Build Tool**: Maven
- **Architecture**: MVC Pattern

## Prerequisites

Before running the application, ensure you have:

1. **Java Development Kit (JDK) 11 or higher**
2. **MySQL Server 8.0 or higher**
3. **Maven 3.6 or higher** (for building the project)

## Installation & Setup

### 1. Database Setup

1. Install MySQL Server on your system
2. Create a MySQL user with appropriate permissions
3. Run the database schema script:

```sql
-- Execute the schema.sql file
mysql -u root -p < database/schema.sql
```

### 2. Database Configuration

Update the database connection settings in `src/main/java/com/banking/database/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/online_banking";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

### 3. Build and Run

```bash
# Clone or download the project
cd OnlineBankingSystem

# Build the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.banking.BankingApplication"
```

Or run directly with Java:

```bash
# Compile all Java files
javac -cp "lib/*" src/main/java/com/banking/**/*.java

# Run the application
java -cp ".:lib/*:src/main/java" com.banking.BankingApplication
```

## Project Structure

```
OnlineBankingSystem/
├── src/main/java/com/banking/
│   ├── BankingApplication.java          # Main application entry point
│   ├── model/                           # Data models
│   │   ├── User.java
│   │   ├── Account.java
│   │   └── Transaction.java
│   ├── dao/                             # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── AccountDAO.java
│   │   └── TransactionDAO.java
│   ├── service/                         # Business logic
│   │   ├── AuthenticationService.java
│   │   └── BankingService.java
│   ├── ui/                              # User interface components
│   │   ├── LoginFrame.java
│   │   ├── RegistrationDialog.java
│   │   ├── DashboardFrame.java
│   │   └── TransferDialog.java
│   └── database/                        # Database utilities
│       └── DatabaseConnection.java
├── database/
│   └── schema.sql                       # Database schema
├── pom.xml                              # Maven configuration
└── README.md                            # This file
```

## Usage

### 1. Login
- Launch the application
- Use the provided sample credentials or register a new account
- Sample users:
  - Username: `john_doe`, Password: `password123`
  - Username: `jane_smith`, Password: `password456`

### 2. Dashboard
- View account balances and details
- Check recent transaction history
- Access transfer functionality

### 3. Transfer Funds
- Click "Transfer Funds" button
- Select source account
- Enter destination account number
- Specify amount and description
- Confirm the transfer

### 4. Registration
- Click "Register" on login screen
- Fill in all required fields
- Ensure email and username are unique
- Password must be at least 6 characters

## Security Features

- Password validation and strength requirements
- SQL injection prevention through prepared statements
- Transaction rollback on failures
- Account balance validation before transfers
- Secure database connection management

## Database Schema

### Users Table
- `user_id` (Primary Key)
- `username` (Unique)
- `password`
- `first_name`, `last_name`
- `email` (Unique)
- `phone_number`
- `registration_date`
- `is_active`

### Accounts Table
- `account_id` (Primary Key)
- `user_id` (Foreign Key)
- `account_number` (Unique)
- `account_type`
- `balance`
- `created_date`
- `is_active`

### Transactions Table
- `transaction_id` (Primary Key)
- `from_account_id` (Foreign Key)
- `to_account_id` (Foreign Key)
- `transaction_type`
- `amount`
- `description`
- `transaction_date`
- `status`

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL server is running
   - Check connection credentials
   - Ensure database exists

2. **Class Not Found Error**
   - Download MySQL Connector JAR
   - Add to classpath or lib directory

3. **Login Issues**
   - Verify sample data was inserted
   - Check username/password combination

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Future Enhancements

- [ ] Password encryption
- [ ] Two-factor authentication
- [ ] Bill payment functionality
- [ ] Account statements export
- [ ] Mobile-responsive web interface
- [ ] Real-time notifications
- [ ] Advanced reporting features
- [ ] Multi-currency support
