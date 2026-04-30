package com.asu.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * UIConstants.java
 *
 * Central store for every colour, font, and size used across the application.
 * Any panel or dialog imports this class and calls its constants so the entire
 * UI looks consistent without duplicating styling code.
 */
public final class UIConstants {

    // ── Colours ──────────────────────────────────────────────────────────────
    public static final Color PRIMARY       = new Color(0, 70, 127);
    public static final Color SECONDARY     = new Color(0, 120, 215);
    public static final Color DANGER        = new Color(180, 30, 30);
    public static final Color SUCCESS_COLOR = new Color(30, 130, 60);
    public static final Color BG            = new Color(245, 247, 250);
    public static final Color PANEL_BG      = Color.WHITE;
    public static final Color TABLE_ALT     = new Color(230, 240, 255);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font TITLE_FONT  = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font LABEL_FONT  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font TABLE_FONT  = new Font("Segoe UI", Font.PLAIN, 12);

    // ── Sizes ─────────────────────────────────────────────────────────────────
    public static final Dimension BUTTON_SIZE = new Dimension(130, 32);
    public static final Dimension FIELD_SIZE  = new Dimension(220, 28);
    public static final int FRAME_W  = 1100;
    public static final int FRAME_H  = 700;
    public static final int PADDING  = 12;

    // Prevent instantiation — this class is a toolbox, not an object
    private UIConstants() {}

    /**
     * Applies consistent blue/danger/success styling to any JButton.
     * Pass UIConstants.PRIMARY, DANGER, or SUCCESS_COLOR as the bg argument.
     */
    public static void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(BUTTON_SIZE);
    }

    /**
     * Creates a bold blue section header label.
     */
    public static JLabel makeHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(HEADER_FONT);
        lbl.setForeground(PRIMARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        return lbl;
    }

    /**
     * Builds a non-editable JTable with a blue header and alternating row shading.
     * Wrap the returned table in a JScrollPane when adding to a panel.
     *
     * Usage:
     *   JTable table = UIConstants.makeStyledTable(model);
     *   panel.add(new JScrollPane(table));
     */
    public static JTable makeStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // all tables are read-only; editing goes through dialogs
            }

            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? PANEL_BG : TABLE_ALT);
                }
                return c;
            }
        };

        table.setFont(TABLE_FONT);
        table.setRowHeight(24);
        table.setSelectionBackground(SECONDARY);
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Style the header row
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer)
                table.getTableHeader().getDefaultRenderer();
        hr.setHorizontalAlignment(JLabel.CENTER);

        return table;
    }

    /**
     * Creates a consistent form row: label on the left, field on the right.
     * Used inside dialogs that use GridBagLayout.
     */
    public static JLabel makeFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(LABEL_FONT);
        return lbl;
    }
}
