package com.banking.ui;

import com.banking.model.User;
import com.banking.service.AuthenticationService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private BufferedImage backgroundImage;

    public RegistrationDialog(JFrame parent, AuthenticationService authService) {
        super(parent, "User Registration", true);
        this.authService = authService;
        loadBackgroundImage();
        initializeComponents();
        setupUI();
        setupEventHandlers();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Kazim\\Desktop\\Bank\\src\\main\\resources\\icon\\backbg.png"));
        } catch (IOException e) {
            System.out.println("Background image not found: " + e.getMessage());
        }
    }

    private void initializeComponents() {
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);

        usernameField = new JTextField(18);
        passwordField = new JPasswordField(18);
        confirmPasswordField = new JPasswordField(18);
        firstNameField = new JTextField(18);
        lastNameField = new JTextField(18);
        emailField = new JTextField(18);
        phoneField = new JTextField(18);

        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");

        // Style both buttons
        styleButton(registerButton);
        styleButton(cancelButton);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(130, 40));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 123, 255));
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        });
    }

    private void setupUI() {
        // ---------- Background Panel ----------
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(0, 51, 102));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // ---------- Form Panel ----------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 200)); // translucent white
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.setPreferredSize(new Dimension(550, 470)); // slightly taller

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ---------- Title ----------
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // ---------- Input Fields ----------
        addFormField(formPanel, gbc, "Username:", usernameField, 1);
        addFormField(formPanel, gbc, "Password:", passwordField, 2);
        addFormField(formPanel, gbc, "Confirm Password:", confirmPasswordField, 3);
        addFormField(formPanel, gbc, "First Name:", firstNameField, 4);
        addFormField(formPanel, gbc, "Last Name:", lastNameField, 5);
        addFormField(formPanel, gbc, "Email:", emailField, 6);
        addFormField(formPanel, gbc, "Phone Number:", phoneField, 7);

        // ---------- Buttons Panel ----------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 15)); // More spacing
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(buttonPanel, gbc);

        backgroundPanel.add(formPanel);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void setupEventHandlers() {
        registerButton.addActionListener((ActionEvent e) -> performRegistration());
        cancelButton.addActionListener((ActionEvent e) -> dispose());
    }

    private void performRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
