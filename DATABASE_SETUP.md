# Online Banking System - Database Setup Instructions

## MySQL Installation

### Windows
1. Download MySQL Installer from https://dev.mysql.com/downloads/installer/
2. Run the installer and select "MySQL Server" and "MySQL Workbench"
3. Set root password during installation
4. Start MySQL service

### macOS
```bash
# Using Homebrew
brew install mysql
brew services start mysql

# Set root password
mysql_secure_installation
```

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

## Database Configuration

### 1. Create Database User (Optional)
```sql
-- Connect as root
mysql -u root -p

-- Create user
CREATE USER 'banking_user'@'localhost' IDENTIFIED BY 'banking_password';
GRANT ALL PRIVILEGES ON online_banking.* TO 'banking_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Create Database and Tables
```bash
# Run the schema script
mysql -u root -p < database/schema.sql
```

### 3. Verify Setup
```sql
-- Connect to database
mysql -u root -p online_banking

-- Check tables
SHOW TABLES;

-- Check sample data
SELECT * FROM users;
SELECT * FROM accounts;
SELECT * FROM transactions;
```

## Application Configuration

Update `src/main/java/com/banking/database/DatabaseConnection.java`:

```java
// For root user
private static final String USERNAME = "root";
private static final String PASSWORD = "your_root_password";

// For custom user
private static final String USERNAME = "banking_user";
private static final String PASSWORD = "banking_password";
```

## Sample Data

The schema.sql file includes sample users and accounts:

### Users
- john_doe / password123
- jane_smith / password456

### Accounts
- ACC001 (Savings) - $5,000.00
- ACC002 (Checking) - $2,500.00  
- ACC003 (Savings) - $7,500.00

## Troubleshooting

### Connection Issues
```bash
# Check MySQL status
sudo systemctl status mysql

# Restart MySQL
sudo systemctl restart mysql

# Check port
netstat -an | grep 3306
```

### Permission Issues
```sql
-- Grant permissions
GRANT ALL PRIVILEGES ON online_banking.* TO 'your_user'@'localhost';
FLUSH PRIVILEGES;
```

### Port Conflicts
```bash
# Check if port 3306 is in use
sudo lsof -i :3306

# Change MySQL port in my.cnf
[mysqld]
port = 3307
```

## Security Notes

- Change default passwords
- Use strong passwords
- Limit user privileges
- Enable SSL for production
- Regular backups recommended
