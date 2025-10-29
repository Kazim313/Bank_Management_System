package com.banking.ui;

import com.banking.model.User;
import com.banking.model.Account;
import com.banking.service.BankingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class TransferDialog extends JDialog {
    private JComboBox<String> fromAccountCombo;
    private JTextField toAccountField;
    private JTextField amountField;
    private JTextArea descriptionArea;
    private JButton transferButton;
    private JButton cancelButton;
    private BankingService bankingService;
    private User currentUser;
    private boolean transferCompleted = false;

    public TransferDialog(JFrame parent, BankingService bankingService, User currentUser) {
        super(parent, "Transfer Funds", true);
        this.bankingService = bankingService;
        this.currentUser = currentUser;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserAccounts();
    }

    private void initializeComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        fromAccountCombo = new JComboBox<>();
        toAccountField = new JTextField(20);
        amountField = new JTextField(20);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        transferButton = new JButton("Transfer");
        cancelButton = new JButton("Cancel");

        // Style buttons
        transferButton.setBackground(new Color(0, 123, 255));
        transferButton.setForeground(Color.WHITE);
        transferButton.setFocusPainted(false);
        transferButton.setFont(new Font("Arial", Font.BOLD, 12));

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
        
        JLabel titleLabel = new JLabel("Transfer Funds");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // From Account
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("From Account:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fromAccountCombo, gbc);

        // To Account
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("To Account Number:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(toAccountField, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(amountField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(transferButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTransfer();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadUserAccounts() {
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

        // Validation
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
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive amount.", 
                                        "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (description.isEmpty()) {
            description = "Fund transfer";
        }

        // Get source account ID
        String selectedAccount = (String) fromAccountCombo.getSelectedItem();
        String fromAccountNumber = selectedAccount.split(" ")[0];
        Account fromAccount = bankingService.getAccountByNumber(fromAccountNumber);
        
        if (fromAccount == null) {
            JOptionPane.showMessageDialog(this, "Source account not found.", 
                                        "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if destination account exists
        Account toAccount = bankingService.getAccountByNumber(toAccountNumber);
        if (toAccount == null) {
            JOptionPane.showMessageDialog(this, "Destination account not found.", 
                                        "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            JOptionPane.showMessageDialog(this, "Insufficient balance for transfer.", 
                                        "Transfer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm transfer
        int confirmResult = JOptionPane.showConfirmDialog(this, 
            "Transfer $" + amount + " from " + fromAccountNumber + " to " + toAccountNumber + "?", 
            "Confirm Transfer", JOptionPane.YES_NO_OPTION);
        
        if (confirmResult != JOptionPane.YES_OPTION) {
            return;
        }

        // Perform transfer
        boolean success = bankingService.transferFunds(fromAccount.getAccountId(), 
                                                     toAccount.getAccountId(), 
                                                     amount, description);

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
