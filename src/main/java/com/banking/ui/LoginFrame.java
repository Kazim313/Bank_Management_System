package com.banking.ui;

import com.banking.BankingApplication;
import com.banking.model.User;
import com.banking.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private AuthenticationService authService;
    private BankingApplication mainApp;
    private BufferedImage backgroundImage;
    private ImageIcon bankIcon;
    private ImageIcon cardIcon;

    private static final String BG_IMAGE_PATH = "/icon/backbg.png";
    private static final String LOGO_IMAGE_PATH = "/icon/bank.png";
    private static final String CARD_IMAGE_PATH = "/icon/card.png";

    public LoginFrame(BankingApplication mainApp) {
        this.mainApp = mainApp;
        this.authService = new AuthenticationService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Online Banking System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load images
        backgroundImage = loadImage(BG_IMAGE_PATH);
        bankIcon = loadIcon(LOGO_IMAGE_PATH);
        cardIcon = loadIcon(CARD_IMAGE_PATH);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Button styling
        styleButton(loginButton);
        styleButton(registerButton);
    }

    private void setupLayout() {
        JPanel rootPanel = (backgroundImage != null)
                ? new BackgroundPanel(backgroundImage)
                : new JPanel(new BorderLayout());

        if (!(rootPanel.getLayout() instanceof BorderLayout)) {
            rootPanel.setLayout(new BorderLayout());
        }
        setContentPane(rootPanel);

        // ---------- HEADER PANEL ----------
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        if (bankIcon != null) {
            // Slightly enlarged logo
            ImageIcon scaledIcon = scaleIcon(bankIcon, 80, 80);
            JLabel logoLabel = new JLabel(scaledIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(logoLabel);
        }

        JLabel titleLabel = new JLabel("Welcome to Online Banking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        headerPanel.add(titleLabel);

        // ---------- FORM PANEL ----------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Username label + field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Password label + field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // ---------- BUTTON PANEL ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15)); // reduced gap
        buttonPanel.setOpaque(false);

        Color btnColor = new Color(10, 102, 204);
        Color hoverColor = new Color(0, 153, 255);
        Color textColor = Color.BLACK;

        // Make both buttons same size
        Dimension buttonSize = new Dimension(120, 40);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);

        JButton[] buttons = {loginButton, registerButton};
        for (JButton btn : buttons) {
            btn.setBackground(btnColor);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btn.setOpaque(true);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(hoverColor);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(btnColor);
                }
            });
        }

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(buttonPanel, gbc);

        // ---------- BOTTOM LEFT IMAGE ----------
        if (cardIcon != null) {
            // Increased size from (140, 90) → (180, 120)
            JLabel cardLabel = new JLabel(scaleIcon(cardIcon, 180, 120));
            JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 40));
            bottomLeftPanel.setOpaque(false);
            bottomLeftPanel.add(cardLabel);
            rootPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
        }

        // ---------- ADD TO FRAME ----------
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(formPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 90, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));
            }
        });
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> openRegistrationDialog());
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = authService.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + user.getFirstName() + "!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                mainApp.showDashboard(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistrationDialog() {
        RegistrationDialog dialog = new RegistrationDialog(this, authService);
        dialog.setVisible(true);
    }

    private BufferedImage loadImage(String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                return ImageIO.read(f);
            }
            File f2 = new File("resources" + (path.startsWith("/") ? path : ("/" + path)));
            if (f2.exists()) {
                return ImageIO.read(f2);
            }
            File f3 = new File("src/main/resources" + (path.startsWith("/") ? path : ("/" + path)));
            if (f3.exists()) {
                return ImageIO.read(f3);
            }
        } catch (IOException ignored) {
        }
        try {
            String normalized = path.startsWith("/") ? path.substring(1) : path;
            java.net.URL url = getClass().getClassLoader().getResource(normalized);
            if (url != null) {
                return ImageIO.read(url);
            }
            url = getClass().getResource(path);
            if (url != null) {
                return ImageIO.read(url);
            }
        } catch (IOException ignored) {
        }
        System.out.println("[LoginFrame] Image not found: " + path);
        return null;
    }

    private ImageIcon loadIcon(String path) {
        File f = new File(path);
        if (f.exists()) {
            return new ImageIcon(path);
        }
        File f2 = new File("resources" + (path.startsWith("/") ? path : ("/" + path)));
        if (f2.exists()) {
            return new ImageIcon(f2.getPath());
        }
        File f3 = new File("src/main/resources" + (path.startsWith("/") ? path : ("/" + path)));
        if (f3.exists()) {
            return new ImageIcon(f3.getPath());
        }
        String normalized = path.startsWith("/") ? path.substring(1) : path;
        java.net.URL url = getClass().getClassLoader().getResource(normalized);
        if (url != null) {
            return new ImageIcon(url);
        }
        url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        System.out.println("[LoginFrame] Icon not found: " + path);
        return null;
    }

    private ImageIcon scaleIcon(ImageIcon src, int targetW, int targetH) {
        if (src == null || src.getIconWidth() <= 0 || src.getIconHeight() <= 0) return src;
        Image img = src.getImage();
        double scale = Math.min((double) targetW / src.getIconWidth(), (double) targetH / src.getIconHeight());
        int newW = (int) Math.round(src.getIconWidth() * scale);
        int newH = (int) Math.round(src.getIconHeight() * scale);
        Image scaled = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static class BackgroundPanel extends JPanel {
        private final Image image;

        BackgroundPanel(Image image) {
            super(new BorderLayout());
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int w = getWidth();
                int h = getHeight();
                double imgW = image.getWidth(null);
                double imgH = image.getHeight(null);
                double scale = Math.max(w / imgW, h / imgH);
                int drawW = (int) (imgW * scale);
                int drawH = (int) (imgH * scale);
                int x = (w - drawW) / 2;
                int y = (h - drawH) / 2;
                g.drawImage(image, x, y, drawW, drawH, this);
            }
        }
    }
}
