package com.banking;

import com.banking.model.User;
import com.banking.ui.DashboardFrame;
import com.banking.ui.LoginFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BankingApplication {
    private LoginFrame loginFrame;
    private DashboardFrame dashboardFrame;

    public BankingApplication() {
        initializeApplication();
    }

    private void initializeApplication() {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        showLogin();
    }

    public void showLogin() {
        if (loginFrame != null) {
            loginFrame.dispose();
        }
        loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
    }

    public void showDashboard(User user) {
        if (dashboardFrame != null) {
            dashboardFrame.dispose();
        }
        dashboardFrame = new DashboardFrame(user);
        dashboardFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Ensure database connection is available
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new BankingApplication();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, 
                        "Failed to start application: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });
    }
}
