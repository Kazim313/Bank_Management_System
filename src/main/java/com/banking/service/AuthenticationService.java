package com.banking.service;

import com.banking.dao.UserDAO;
import com.banking.model.User;

public class AuthenticationService {
    private UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        return userDAO.loginUser(username.trim(), password);
    }

    public boolean register(User user) {
        if (user == null || !isValidUser(user)) {
            return false;
        }

        // Check if username or email already exists
        if (userDAO.isUsernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userDAO.isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        return userDAO.registerUser(user);
    }

    private boolean isValidUser(User user) {
        return user.getUsername() != null && !user.getUsername().trim().isEmpty() &&
               user.getPassword() != null && !user.getPassword().trim().isEmpty() &&
               user.getFirstName() != null && !user.getFirstName().trim().isEmpty() &&
               user.getLastName() != null && !user.getLastName().trim().isEmpty() &&
               user.getEmail() != null && !user.getEmail().trim().isEmpty() &&
               isValidEmail(user.getEmail());
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
