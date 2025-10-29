# Database Files Overview

This directory contains all the database-related files for the Online Banking System.

## Files Description

### Core Database Files
- **`complete_database_setup.sql`** - Complete database setup with advanced features
  - Full database schema with constraints and indexes
  - Stored procedures for fund transfers
  - Views for common queries
  - Triggers for automatic balance updates
  - Comprehensive sample data

- **`simple_setup.sql`** - Basic database setup for quick start
  - Simple table structure
  - Basic sample data
  - Perfect for testing and development

- **`schema.sql`** - Original schema file (basic version)
  - Original database structure
  - Sample data included

### Utility Files
- **`backup_database.sql`** - Database backup script
  - Creates backup of all tables
  - Useful before making changes

- **`reset_database.sql`** - Database reset script
  - Clears all data and resets to initial state
  - Re-inserts sample data

- **`database.properties`** - Configuration file
  - Database connection settings
  - Connection pool configuration
  - SSL and logging settings

## Quick Start

### Option 1: Simple Setup (Recommended for beginners)
```bash
mysql -u root -p < database/simple_setup.sql
```

### Option 2: Complete Setup (Advanced features)
```bash
mysql -u root -p < database/complete_database_setup.sql
```

### Option 3: Original Schema
```bash
mysql -u root -p < database/schema.sql
```

## Database Configuration

Update the database connection settings in:
`src/main/java/com/banking/database/DatabaseConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/online_banking";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

## Sample Login Credentials

After running any setup script, you can login with:

- **Username**: john_doe, **Password**: password123
- **Username**: jane_smith, **Password**: password456
- **Username**: admin, **Password**: admin123

## Troubleshooting

### Connection Issues
1. Ensure MySQL server is running
2. Check username and password
3. Verify database exists
4. Check port 3306 is accessible

### Permission Issues
```sql
-- Grant permissions to user
GRANT ALL PRIVILEGES ON online_banking.* TO 'your_user'@'localhost';
FLUSH PRIVILEGES;
```

### Reset Database
If you need to start fresh:
```bash
mysql -u root -p < database/reset_database.sql
```

## Advanced Features (Complete Setup Only)

### Stored Procedures
- `TransferFunds()` - Secure fund transfer with validation
- `GetUserTransactions()` - Get user transaction history

### Views
- `user_account_summary` - User account overview
- `recent_transactions` - Recent transaction details

### Triggers
- Automatic balance updates on transaction completion
- Data integrity enforcement

## Backup and Restore

### Create Backup
```bash
mysqldump -u root -p online_banking > backup_$(date +%Y%m%d).sql
```

### Restore from Backup
```bash
mysql -u root -p online_banking < backup_20231201.sql
```
