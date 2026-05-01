package com.asu.ui.panels;

import com.asu.dialogs.StaffManagementDialog;
import com.asu.dialogs.RegistrationDialog;
import com.asu.ui.DialogUtils;
import com.asu.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ManagerPanel.java
 *
 * The main dashboard for a Manager. Divided into 4 tabs:
 *   1. Staff Management    — View, add, edit, delete Counter Staff & Technicians
 *   2. Service Prices      — View and update prices for Normal and Major Service
 *   3. Feedback & Comments — Read-only view of all feedback from users
 *   4. Reports             — Generate a summary report of the system data
 *
 * Manager Permissions (from assignment Table 1.0):
 *   ✔ CRUD on managers, counter staff, and technicians
 *   ✔ Set service prices
 *   ✔ View all feedbacks and comments
 *   ✔ Analyse reports
 *
 * INTEGRATION POINT (Week 3):
 *   Replace direct file reads in loadStaffTable(), loadFeedbackTable(),
 *   and generateReport() with calls to UserDataManager, FeedbackDataManager,
 *   ServiceDataManager, and Noora's generateReport() method.
 */
public class ManagerPanel extends JPanel {

    private static final String USERS_FILE    = "data/users.txt";
    private static final String FEEDBACK_FILE = "data/feedback.txt";
    private static final String SERVICES_FILE = "data/services.txt";

    private final String[]  currentUser; // {userId, name, phone, username, pass, userType}
    private final Runnable  onLogout;

    // ── Staff tab components ──────────────────────────────────────────────────
    private DefaultTableModel staffModel;
    private JTable            staffTable;

    // ── Services tab ──────────────────────────────────────────────────────────
    private JTextField normalPriceField;
    private JTextField majorPriceField;

    // ── Feedback tab ─────────────────────────────────────────────────────────
    private DefaultTableModel feedbackModel;

    // ── Reports tab ──────────────────────────────────────────────────────────
    private JTextArea reportArea;

    // ── Constructor ───────────────────────────────────────────────────────────

    public ManagerPanel(String[] currentUser, Runnable onLogout) {
        this.currentUser = currentUser;
        this.onLogout    = onLogout;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG);
        buildUI();
    }

    // ── UI Construction ───────────────────────────────────────────────────────

    private void buildUI() {
        // Top bar with welcome message and logout
        add(buildTopBar(), BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIConstants.LABEL_FONT);
        tabs.addTab("👤  Staff Management",  buildStaffTab());
        tabs.addTab("💲  Service Prices",    buildServicesTab());
        tabs.addTab("💬  Feedback",          buildFeedbackTab());
        tabs.addTab("📊  Reports",           buildReportTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.PRIMARY);
        bar.setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel welcome = new JLabel("Manager Dashboard  |  " + currentUser[1]);
        welcome.setFont(UIConstants.HEADER_FONT);
        welcome.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        UIConstants.styleButton(logoutBtn, UIConstants.DANGER);
        logoutBtn.addActionListener(e -> onLogout.run());

        bar.add(welcome,   BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    // ── Tab 1: Staff Management ───────────────────────────────────────────────

    private JPanel buildStaffTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        panel.add(UIConstants.makeHeader("Staff Members"), BorderLayout.NORTH);

        // Table
        String[] cols = {"User ID", "Name", "Phone", "Username", "Role"};
        staffModel = new DefaultTableModel(cols, 0);
        staffTable = UIConstants.makeStyledTable(staffModel);
        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);

        // Button row
        JButton addBtn    = new JButton("Add Staff");
        JButton editBtn   = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        UIConstants.styleButton(addBtn,    UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(editBtn,   UIConstants.SECONDARY);
        UIConstants.styleButton(deleteBtn, UIConstants.DANGER);
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);

        addBtn.addActionListener(e -> {
            StaffManagementDialog dlg = new StaffManagementDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
            loadStaffTable(); // refresh after add
        });

        editBtn.addActionListener(e -> {
            int row = staffTable.getSelectedRow();
            if (row < 0) { DialogUtils.showError(this, "Select a staff member first."); return; }
            String[] rowData = getRowData(staffModel, row);
            StaffManagementDialog dlg = new StaffManagementDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    StaffManagementDialog.Mode.EDIT,
                    findFullUserRecord(rowData[0])
            );
            dlg.setVisible(true);
            loadStaffTable();
        });

        deleteBtn.addActionListener(e -> handleDeleteStaff());
        refreshBtn.addActionListener(e -> loadStaffTable());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(addBtn); btnRow.add(editBtn);
        btnRow.add(deleteBtn); btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        loadStaffTable();
        return panel;
    }

    /**
     * Reads users.txt and populates the staff table with CounterStaff and Technicians.
     * INTEGRATION POINT (Week 3): UserDataManager.getInstance().getAllStaff()
     */
    private void loadStaffTable() {
        staffModel.setRowCount(0);
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length < 6) continue;
                // Show Manager, CounterStaff, Technician — not Customer
                String role = p[5];
                if ("Customer".equals(role)) continue;
                // Columns: userId, name, phone, username, role (password hidden)
                staffModel.addRow(new Object[]{p[0], p[1], p[2], p[3], p[5]});
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load staff: " + ex.getMessage());
        }
    }

    private void handleDeleteStaff() {
        int row = staffTable.getSelectedRow();
        if (row < 0) { DialogUtils.showError(this, "Select a staff member first."); return; }
        String userId = (String) staffModel.getValueAt(row, 0);
        String name   = (String) staffModel.getValueAt(row, 1);
        if (!DialogUtils.showConfirmation(this,
                "Delete staff member \"" + name + "\" (" + userId + ")?\nThis cannot be undone.")) {
            return;
        }
        // INTEGRATION POINT: UserDataManager.getInstance().removeUser(userId)
        deleteUserFromFile(userId);
        loadStaffTable();
        DialogUtils.showSuccess(this, "\"" + name + "\" has been removed.");
    }

    // ── Tab 2: Service Prices ─────────────────────────────────────────────────

    private JPanel buildServicesTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(UIConstants.makeHeader("Set Service Prices"), gbc);

        // Normal Service price
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(UIConstants.makeFormLabel("Normal Service (1 hr) — RM:"), gbc);
        normalPriceField = new JTextField("80.00", 10);
        normalPriceField.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 1;
        panel.add(normalPriceField, gbc);

        // Major Service price
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(UIConstants.makeFormLabel("Major Service (3 hrs) — RM:"), gbc);
        majorPriceField = new JTextField("200.00", 10);
        majorPriceField.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 1;
        panel.add(majorPriceField, gbc);

        // Save button
        JButton saveBtn = new JButton("Update Prices");
        UIConstants.styleButton(saveBtn, UIConstants.SUCCESS_COLOR);
        saveBtn.addActionListener(e -> handleUpdatePrices());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveBtn, gbc);

        loadServicePrices();
        return panel;
    }

    private void loadServicePrices() {
        // INTEGRATION POINT: ServiceDataManager.getInstance().getPrice(...)
        File f = new File(SERVICES_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length < 4) continue;
                if ("Normal".equalsIgnoreCase(p[2])) normalPriceField.setText(p[3]);
                if ("Major".equalsIgnoreCase(p[2]))  majorPriceField.setText(p[3]);
            }
        } catch (IOException ignored) {}
    }

    private void handleUpdatePrices() {
        String normalStr = normalPriceField.getText().trim();
        String majorStr  = majorPriceField.getText().trim();
        try {
            double normalPrice = Double.parseDouble(normalStr);
            double majorPrice  = Double.parseDouble(majorStr);
            if (normalPrice <= 0 || majorPrice <= 0) throw new NumberFormatException();
            // INTEGRATION POINT: ServiceDataManager.getInstance().updatePrice(...)
            writePricesToFile(normalPrice, majorPrice);
            DialogUtils.showSuccess(this,
                    "Prices updated:\n" +
                    "Normal Service: RM " + String.format("%.2f", normalPrice) + "\n" +
                    "Major Service:  RM " + String.format("%.2f", majorPrice));
        } catch (NumberFormatException ex) {
            DialogUtils.showError(this, "Invalid price. Enter a positive number (e.g. 80.00).");
        }
    }

    private void writePricesToFile(double normal, double major) {
        File f = new File(SERVICES_FILE);
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            pw.println("SVC-001|Normal Service|Normal|" + String.format("%.2f", normal) + "|60");
            pw.println("SVC-002|Major Service|Major|"  + String.format("%.2f", major)  + "|180");
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not save prices: " + ex.getMessage());
        }
    }

    // ── Tab 3: Feedback ───────────────────────────────────────────────────────

    private JPanel buildFeedbackTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        panel.add(UIConstants.makeHeader("All Feedback & Comments"), BorderLayout.NORTH);

        String[] cols = {"Feedback ID", "Appointment ID", "Comment", "Date", "From"};
        feedbackModel = new DefaultTableModel(cols, 0);
        JTable feedbackTable = UIConstants.makeStyledTable(feedbackModel);
        panel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);
        refreshBtn.addActionListener(e -> loadFeedbackTable());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        loadFeedbackTable();
        return panel;
    }

    /**
     * INTEGRATION POINT (Week 3): FeedbackDataManager.getInstance().getAllFeedback()
     */
    private void loadFeedbackTable() {
        feedbackModel.setRowCount(0);
        File f = new File(FEEDBACK_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 5) {
                    feedbackModel.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4]});
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load feedback: " + ex.getMessage());
        }
    }

    // ── Tab 4: Reports ────────────────────────────────────────────────────────

    private JPanel buildReportTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        panel.add(UIConstants.makeHeader("System Report"), BorderLayout.NORTH);

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        reportArea.setEditable(false);
        reportArea.setBackground(new Color(252, 252, 252));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton generateBtn = new JButton("Generate Report");
        UIConstants.styleButton(generateBtn, UIConstants.PRIMARY);
        generateBtn.addActionListener(e -> generateReport());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(generateBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Counts records in each data file and builds a summary report.
     * INTEGRATION POINT (Week 3): Replace with Noora's Report.generateReport() method.
     */
    private void generateReport() {
        String date = java.time.LocalDate.now().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("  APU-ASC SYSTEM REPORT\n");
        sb.append("  Generated: ").append(date).append("\n");
        sb.append("  Generated by: ").append(currentUser[1]).append("\n");
        sb.append("============================================\n\n");
        sb.append("  Total Staff Members : ").append(countLinesExcluding(USERS_FILE, "Customer")).append("\n");
        sb.append("  Total Customers     : ").append(countLinesMatching(USERS_FILE, "Customer")).append("\n");
        sb.append("  Total Appointments  : ").append(countLines("data/appointments.txt")).append("\n");
        sb.append("  Total Payments      : ").append(countLines("data/payments.txt")).append("\n");
        sb.append("  Total Feedback      : ").append(countLines(FEEDBACK_FILE)).append("\n\n");
        sb.append("============================================\n");
        reportArea.setText(sb.toString());
    }

    // ── Utility helpers ───────────────────────────────────────────────────────

    private int countLines(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) return 0;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            while (br.readLine() != null) count++;
        } catch (IOException ignored) {}
        return count;
    }

    private int countLinesMatching(String filePath, String role) {
        File f = new File(filePath);
        if (!f.exists()) return 0;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 6 && p[5].equals(role)) count++;
            }
        } catch (IOException ignored) {}
        return count;
    }

    private int countLinesExcluding(String filePath, String role) {
        File f = new File(filePath);
        if (!f.exists()) return 0;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 6 && !p[5].equals(role)) count++;
            }
        } catch (IOException ignored) {}
        return count;
    }

    private String[] getRowData(DefaultTableModel model, int row) {
        String[] data = new String[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            data[i] = (String) model.getValueAt(row, i);
        }
        return data;
    }

    /** Returns the full pipe-delimited record for a userId (including password). */
    private String[] findFullUserRecord(String userId) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 6 && p[0].equals(userId)) return p;
            }
        } catch (IOException ignored) {}
        return null;
    }

    private void deleteUserFromFile(String userId) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length > 0 && !p[0].equals(userId)) lines.add(line);
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not read file: " + ex.getMessage());
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            for (String l : lines) pw.println(l);
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not update file: " + ex.getMessage());
        }
    }
}
