package com.banking.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class UITheme {
    public static final Color PRIMARY = new Color(0, 123, 255);
    public static final Color PRIMARY_DARK = new Color(0, 92, 191);
    public static final Color SUCCESS = new Color(40, 167, 69);
    public static final Color DANGER = new Color(220, 53, 69);
    public static final Color TEXT_DARK = new Color(33, 37, 41);
    public static final Color TEXT_MUTED = new Color(108, 117, 125);
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(248, 249, 250);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    private UITheme() {}

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        stylePrimaryButton(b);
        return b;
    }

    public static void stylePrimaryButton(AbstractButton b) {
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(BUTTON_FONT);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setRolloverEnabled(true);
    }

    public static JPanel paddedPanel(LayoutManager layout, int top, int left, int bottom, int right) {
        JPanel p = new JPanel(layout);
        p.setBorder(new EmptyBorder(top, left, bottom, right));
        p.setBackground(SURFACE);
        return p;
    }

    public static void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setBackground(SURFACE_ALT);
        header.setForeground(TEXT_DARK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(TITLE_FONT);
        l.setForeground(TEXT_DARK);
        return l;
    }
}


