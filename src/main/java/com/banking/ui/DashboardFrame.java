package com.banking.ui;

import com.banking.BankingApplication;
import com.banking.model.User;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.BankingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private BankingService bankingService;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JTable accountsTable;
    private JTable transactionsTable;
    private JButton transferButton;
    private JButton refreshButton;
    private JButton logoutButton;

    public DashboardFrame(User user) {
        this.currentUser = user;
        this.bankingService = new BankingService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserData();
    }

    private void initializeComponents() {
        setTitle("Online Banking System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        welcomeLabel = new JLabel();
        balanceLabel = new JLabel();
        
        // Accounts table
        String[] accountColumns = {"Account Number", "Type", "Balance", "Created Date"};
        accountsTable = new JTable(new DefaultTableModel(accountColumns, 0));
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Transactions table
        String[] transactionColumns = {"Date", "Type", "Amount", "Description", "Status"};
        transactionsTable = new JTable(new DefaultTableModel(transactionColumns, 0));
        
        transferButton = new JButton("Transfer Funds");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");

        // Style buttons
        styleButton(transferButton, new Color(0, 123, 255));
        styleButton(refreshButton, new Color(40, 167, 69));
        styleButton(logoutButton, new Color(220, 53, 69));
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Main content panel
        JPanel mainPanel = createMainPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(33, 37, 41));
        
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(40, 167, 69));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(248, 249, 250));
        leftPanel.add(welcomeLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(248, 249, 250));
        rightPanel.add(balanceLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Accounts panel
        JPanel accountsPanel = new JPanel(new BorderLayout());
        accountsPanel.setBorder(BorderFactory.createTitledBorder("My Accounts"));
        
        JScrollPane accountsScrollPane = new JScrollPane(accountsTable);
        accountsScrollPane.setPreferredSize(new Dimension(400, 200));
        accountsPanel.add(accountsScrollPane, BorderLayout.CENTER);

        // Transactions panel
        JPanel transactionsPanel = new JPanel(new BorderLayout());
        transactionsPanel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));
        
        JScrollPane transactionsScrollPane = new JScrollPane(transactionsTable);
        transactionsScrollPane.setPreferredSize(new Dimension(400, 200));
        transactionsPanel.add(transactionsScrollPane, BorderLayout.CENTER);

        mainPanel.add(accountsPanel);
        mainPanel.add(transactionsPanel);

        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        buttonPanel.add(transferButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        return buttonPanel;
    }

    private void setupEventHandlers() {
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTransferDialog();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserData();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(DashboardFrame.this, 
                    "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    new BankingApplication().showLogin();
                }
            }
        });
    }

    private void loadUserData() {
        // Update welcome message
        welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
        
        // Load accounts
        List<Account> accounts = bankingService.getUserAccounts(currentUser.getUserId());
        DefaultTableModel accountsModel = (DefaultTableModel) accountsTable.getModel();
        accountsModel.setRowCount(0);

        BigDecimal totalBalance = BigDecimal.ZERO;
        for (Account account : accounts) {
            Object[] row = {
                account.getAccountNumber(),
                account.getAccountType(),
                "$" + account.getBalance().toString(),
                account.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            };
            accountsModel.addRow(row);
            totalBalance = totalBalance.add(account.getBalance());
        }

        balanceLabel.setText("Total Balance: $" + totalBalance.toString());

        // Load transactions for first account if available
        if (!accounts.isEmpty()) {
            loadTransactions(accounts.get(0).getAccountId());
        }
    }

    private void loadTransactions(int accountId) {
        List<Transaction> transactions = bankingService.getAccountTransactions(accountId);
        DefaultTableModel transactionsModel = (DefaultTableModel) transactionsTable.getModel();
        transactionsModel.setRowCount(0);

        for (Transaction transaction : transactions) {
            Object[] row = {
                transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                transaction.getTransactionType(),
                "$" + transaction.getAmount().toString(),
                transaction.getDescription(),
                transaction.getStatus()
            };
            transactionsModel.addRow(row);
        }
    }

    private void openTransferDialog() {
        TransferDialog dialog = new TransferDialog(this, bankingService, currentUser);
        dialog.setVisible(true);
        
        // Refresh data after transfer
        if (dialog.isTransferCompleted()) {
            loadUserData();
        }
    }
}
