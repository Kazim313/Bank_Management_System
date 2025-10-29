package com.banking.ui;

import com.banking.model.User;
import com.banking.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton registerButton;
    private JButton cancelButton;
    private AuthenticationService authService;

    public RegistrationDialog(JFrame parent, AuthenticationService authService) {
        super(parent, "User Registration", true);
        this.authService = authService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");

        // Style the buttons
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));

        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        addFormField(formPanel, gbc, "Username:", usernameField, 0);
        
        // Password
        addFormField(formPanel, gbc, "Password:", passwordField, 1);
        
        // Confirm Password
        addFormField(formPanel, gbc, "Confirm Password:", confirmPasswordField, 2);
        
        // First Name
        addFormField(formPanel, gbc, "First Name:", firstNameField, 3);
        
        // Last Name
        addFormField(formPanel, gbc, "Last Name:", lastNameField, 4);
        
        // Email
        addFormField(formPanel, gbc, "Email:", emailField, 5);
        
        // Phone
        addFormField(formPanel, gbc, "Phone Number:", phoneField, 6);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void setupEventHandlers() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void performRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || 
            lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", 
                                        "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", 
                                        "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", 
                                        "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User newUser = new User(username, password, firstName, lastName, email, phone);
            boolean success = authService.register(newUser);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", 
                                            "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                                        "Registration Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
