package com.banking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/online_banking";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Channa@123";
    
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        initializeConnection();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to validate database connection", e);
        }
        if (connection == null) {
            throw new IllegalStateException("Database connection is not initialized. Check JDBC URL, username/password, and that MySQL is running.");
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }

    private void initializeConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC Driver not found. Ensure mysql-connector-java JAR is on the classpath.", e);
        }
        try {
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new IllegalStateException("Database connection failed for URL=" + URL + ", user=" + USERNAME + ". Cause: " + e.getMessage(), e);
        }
    }
}
