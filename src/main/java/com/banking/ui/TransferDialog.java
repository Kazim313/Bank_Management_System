package com.banking.ui;

import com.banking.model.User;
import com.banking.model.Account;
import com.banking.service.BankingService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class TransferDialog extends JDialog {
    private JComboBox<String> fromAccountCombo;
    private JTextField toAccountField;
    private JTextField amountField;
    private JTextArea descriptionArea;
    private JButton transferButton;
    private JButton refreshButton;
    private JButton cancelButton;
    private BankingService bankingService;
    private User currentUser;
    private boolean transferCompleted = false;
    private Image backgroundImage;

    public TransferDialog(JFrame parent, BankingService bankingService, User currentUser) {
        super(parent, "Transfer Funds", true);
        this.bankingService = bankingService;
        this.currentUser = currentUser;

        // ✅ Load background image from given absolute directory
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Kazim\\Desktop\\Bank\\src\\main\\resources\\icon\\backbg.png"));
        } catch (IOException e) {
            backgroundImage = null;
            System.err.println("Background image not found: " + e.getMessage());
        }

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserAccounts();
    }

    private void initializeComponents() {
        setSize(550, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);

        fromAccountCombo = new JComboBox<>();
        toAccountField = new JTextField(20);
        amountField = new JTextField(20);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        // Buttons
        transferButton = new JButton("Transfer");
        refreshButton = new JButton("Refresh");
        cancelButton = new JButton("Cancel");

        // ---- Style buttons ----
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);

        transferButton.setBackground(new Color(0, 123, 255));
        transferButton.setForeground(Color.BLACK);
        transferButton.setFont(buttonFont);
        transferButton.setFocusPainted(false);
        transferButton.setOpaque(true);
        transferButton.setBorderPainted(false);
        transferButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        refreshButton.setBackground(new Color(40, 167, 69));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFont(buttonFont);
        refreshButton.setFocusPainted(false);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(buttonFont);
        cancelButton.setFocusPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effects
        transferButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                transferButton.setBackground(new Color(0, 105, 217));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                transferButton.setBackground(new Color(0, 123, 255));
            }
        });

        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(new Color(33, 136, 56));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(new Color(40, 167, 69));
            }
        });

        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(220, 53, 69));
            }
        });
    }

    private void setupLayout() {
        // Custom background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(0, 51, 102)); // fallback color
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Header
        JLabel titleLabel = new JLabel("💸 Transfer Funds", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("From Account:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fromAccountCombo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(createLabel("To Account Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(toAccountField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(createLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        formPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(createLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(transferButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void setupEventHandlers() {
        transferButton.addActionListener((ActionEvent e) -> performTransfer());
        refreshButton.addActionListener((ActionEvent e) -> loadUserAccounts());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadUserAccounts() {
        fromAccountCombo.removeAllItems();
        List<Account> accounts = bankingService.getUserAccounts(currentUser.getUserId());
        for (Account account : accounts) {
            String displayText = account.getAccountNumber() + " (" + account.getAccountType() +
                    ") - $" + account.getBalance().toString();
            fromAccountCombo.addItem(displayText);
        }
    }

    private void performTransfer() {
        String toAccountNumber = toAccountField.getText().trim();
        String amountText = amountField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (fromAccountCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a source account.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (toAccountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter destination account number.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter transfer amount.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive amount.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (description.isEmpty()) description = "Fund transfer";

        String selectedAccount = (String) fromAccountCombo.getSelectedItem();
        String fromAccountNumber = selectedAccount.split(" ")[0];
        Account fromAccount = bankingService.getAccountByNumber(fromAccountNumber);

        if (fromAccount == null) {
            JOptionPane.showMessageDialog(this, "Source account not found.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Account toAccount = bankingService.getAccountByNumber(toAccountNumber);
        if (toAccount == null) {
            JOptionPane.showMessageDialog(this, "Destination account not found.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            JOptionPane.showMessageDialog(this, "Insufficient balance for transfer.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmResult = JOptionPane.showConfirmDialog(this,
                "Transfer $" + amount + " from " + fromAccountNumber + " to " + toAccountNumber + "?",
                "Confirm Transfer", JOptionPane.YES_NO_OPTION);

        if (confirmResult != JOptionPane.YES_OPTION) return;

        boolean success = bankingService.transferFunds(
                fromAccount.getAccountId(),
                toAccount.getAccountId(),
                amount, description
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Transfer completed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            transferCompleted = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Transfer failed. Please try again.",
                    "Transfer Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isTransferCompleted() {
        return transferCompleted;
    }
}
