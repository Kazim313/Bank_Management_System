package com.banking.ui;

import com.banking.BankingApplication;
import com.banking.model.User;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.BankingService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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
    private BufferedImage backgroundImage;

    public DashboardFrame(User user) {
        this.currentUser = user;
        this.bankingService = new BankingService();
        loadBackgroundImage();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserData();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("C:/Users/Kazim/Desktop/Bank/src/main/resources/icon/backbg.png"));
        } catch (IOException e) {
            System.out.println("Background image not found: " + e.getMessage());
        }
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
        transferButton.setPreferredSize(new Dimension(150, 40));

        styleButton(refreshButton, new Color(40, 167, 69));
        refreshButton.setPreferredSize(new Dimension(100, 40));

        styleButton(logoutButton, new Color(220, 53, 69));
        logoutButton.setPreferredSize(new Dimension(100, 40));
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
                button.setForeground(Color.BLACK);
                button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }

    private void setupLayout() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        JPanel mainPanel = createMainPanel();
        JPanel buttonPanel = createButtonPanel();

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(backgroundPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);

        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(Color.WHITE);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(welcomeLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(balanceLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Accounts panel
        JPanel accountsPanel = new JPanel(new BorderLayout());
        accountsPanel.setOpaque(false);

        TitledBorder accountsBorder = BorderFactory.createTitledBorder("My Accounts");
        accountsBorder.setTitleColor(Color.WHITE);
        accountsBorder.setTitleFont(new Font("Arial", Font.BOLD, 17));
        accountsPanel.setBorder(accountsBorder);

        JScrollPane accountsScrollPane = new JScrollPane(accountsTable);
        accountsPanel.add(accountsScrollPane, BorderLayout.CENTER);

        // Transactions panel
        JPanel transactionsPanel = new JPanel(new BorderLayout());
        transactionsPanel.setOpaque(false);

        TitledBorder transactionsBorder = BorderFactory.createTitledBorder("Recent Transactions");
        transactionsBorder.setTitleColor(Color.WHITE);
        transactionsBorder.setTitleFont(new Font("Arial", Font.BOLD, 17));
        transactionsPanel.setBorder(transactionsBorder);

        JScrollPane transactionsScrollPane = new JScrollPane(transactionsTable);
        transactionsPanel.add(transactionsScrollPane, BorderLayout.CENTER);

        mainPanel.add(accountsPanel);
        mainPanel.add(transactionsPanel);

        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        buttonPanel.add(transferButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        return buttonPanel;
    }

    private void setupEventHandlers() {
        transferButton.addActionListener(e -> openTransferDialog());
        refreshButton.addActionListener(e -> loadUserData());

        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(DashboardFrame.this,
                    "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                new BankingApplication().showLogin();
            }
        });
    }

    private void loadUserData() {
        welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");

        List<Account> accounts = bankingService.getUserAccounts(currentUser.getUserId());
        DefaultTableModel accountsModel = (DefaultTableModel) accountsTable.getModel();
        accountsModel.setRowCount(0);

        BigDecimal totalBalance = BigDecimal.ZERO;
        for (Account account : accounts) {
            Object[] row = {
                    account.getAccountNumber(),
                    account.getAccountType(),
                    "$" + account.getBalance(),
                    account.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            };
            accountsModel.addRow(row);
            totalBalance = totalBalance.add(account.getBalance());
        }

        balanceLabel.setText("Total Balance: $" + totalBalance);

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
                    "$" + transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getStatus()
            };
            transactionsModel.addRow(row);
        }
    }

    private void openTransferDialog() {
        TransferDialog dialog = new TransferDialog(this, bankingService, currentUser);
        dialog.setVisible(true);

        if (dialog.isTransferCompleted()) {
            loadUserData();
        }
    }
}
