package com.asu.ui.panels;

import com.asu.dialogs.AppointmentDialog;
import com.asu.dialogs.PaymentDialog;
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
 * CounterStaffPanel.java
 *
 * Dashboard for Counter Staff. Three tabs:
 *   1. Customer Management  — CRUD on customers
 *   2. Appointments         — View all appointments, create new ones
 *   3. Payments             — Collect payment for a completed/pending appointment
 *
 * Counter Staff Permissions (from assignment Table 1.0):
 *   ✔ Edit own profile
 *   ✔ CRUD on customers
 *   ✔ Create and assign appointments (Normal=1hr, Major=3hr) to available technicians
 *   ✔ Collect payment and generate receipts
 *
 * INTEGRATION POINT (Week 3):
 *   Replace direct file reads with UserDataManager, AppointmentDataManager,
 *   PaymentDataManager from Namonje's data layer.
 */
public class CounterStaffPanel extends JPanel {

    private static final String USERS_FILE        = "data/users.txt";
    private static final String APPOINTMENTS_FILE = "data/appointments.txt";

    private final String[] currentUser;
    private final Runnable onLogout;

    // ── Customer tab ──────────────────────────────────────────────────────────
    private DefaultTableModel customerModel;
    private JTable            customerTable;

    // ── Appointments tab ──────────────────────────────────────────────────────
    private DefaultTableModel apptModel;
    private JTable            apptTable;

    // ── Constructor ───────────────────────────────────────────────────────────

    public CounterStaffPanel(String[] currentUser, Runnable onLogout) {
        this.currentUser = currentUser;
        this.onLogout    = onLogout;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG);
        buildUI();
    }

    // ── UI Construction ───────────────────────────────────────────────────────

    private void buildUI() {
        add(buildTopBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIConstants.LABEL_FONT);
        tabs.addTab("👥  Customer Management", buildCustomerTab());
        tabs.addTab("📅  Appointments",         buildAppointmentsTab());
        tabs.addTab("💳  Payments",             buildPaymentsTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.PRIMARY);
        bar.setBorder(new EmptyBorder(10, 16, 10, 16));
        JLabel welcome = new JLabel("Counter Staff Dashboard  |  " + currentUser[1]);
        welcome.setFont(UIConstants.HEADER_FONT);
        welcome.setForeground(Color.WHITE);
        JButton logoutBtn = new JButton("Logout");
        UIConstants.styleButton(logoutBtn, UIConstants.DANGER);
        logoutBtn.addActionListener(e -> onLogout.run());
        bar.add(welcome,   BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    // ── Tab 1: Customer Management ────────────────────────────────────────────

    private JPanel buildCustomerTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("Customer Records"), BorderLayout.NORTH);

        String[] cols = {"Customer ID", "Name", "Phone", "Username"};
        customerModel = new DefaultTableModel(cols, 0);
        customerTable = UIConstants.makeStyledTable(customerModel);
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Buttons
        JButton addBtn    = new JButton("Add Customer");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        UIConstants.styleButton(addBtn,    UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(deleteBtn, UIConstants.DANGER);
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);

        addBtn.addActionListener(e -> {
            // Counter Staff registers customers — role locked to "Customer"
            RegistrationDialog dlg = new RegistrationDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this), "Customer");
            dlg.setVisible(true);
            loadCustomerTable();
        });
        deleteBtn.addActionListener(e -> handleDeleteCustomer());
        refreshBtn.addActionListener(e -> loadCustomerTable());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(addBtn); btnRow.add(deleteBtn); btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        loadCustomerTable();
        return panel;
    }

    private void loadCustomerTable() {
        customerModel.setRowCount(0);
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 6 && "Customer".equals(p[5])) {
                    customerModel.addRow(new Object[]{p[0], p[1], p[2], p[3]});
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load customers: " + ex.getMessage());
        }
    }

    private void handleDeleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row < 0) { DialogUtils.showError(this, "Select a customer first."); return; }
        String userId = (String) customerModel.getValueAt(row, 0);
        String name   = (String) customerModel.getValueAt(row, 1);
        if (!DialogUtils.showConfirmation(this,
                "Delete customer \"" + name + "\"? This cannot be undone.")) return;
        // INTEGRATION POINT: UserDataManager.getInstance().removeUser(userId)
        deleteLineFromFile(USERS_FILE, userId);
        loadCustomerTable();
        DialogUtils.showSuccess(this, "Customer \"" + name + "\" removed.");
    }

    // ── Tab 2: Appointments ───────────────────────────────────────────────────

    private JPanel buildAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("Appointments"), BorderLayout.NORTH);

        String[] cols = {"Appt ID", "Date", "Time", "Status", "Vehicle No", "Technician ID"};
        apptModel = new DefaultTableModel(cols, 0);
        apptTable = UIConstants.makeStyledTable(apptModel);
        panel.add(new JScrollPane(apptTable), BorderLayout.CENTER);

        JButton newApptBtn  = new JButton("New Appointment");
        JButton refreshBtn  = new JButton("Refresh");
        UIConstants.styleButton(newApptBtn, UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);

        newApptBtn.addActionListener(e -> {
            AppointmentDialog dlg = new AppointmentDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this), currentUser[0]);
            dlg.setVisible(true);
            loadAppointmentTable();
        });
        refreshBtn.addActionListener(e -> loadAppointmentTable());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(newApptBtn); btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        loadAppointmentTable();
        return panel;
    }

    private void loadAppointmentTable() {
        apptModel.setRowCount(0);
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                // appointmentId|date|time|status|vehicleNo|issue|technicianId
                if (p.length >= 7) {
                    apptModel.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4], p[6]});
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load appointments: " + ex.getMessage());
        }
    }

    // ── Tab 3: Payments ───────────────────────────────────────────────────────

    private JPanel buildPaymentsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("Payment Collection"), BorderLayout.NORTH);

        // Re-use the appointment table to pick an appointment to pay
        String[] cols = {"Appt ID", "Date", "Time", "Status", "Vehicle No", "Tech ID"};
        DefaultTableModel payApptModel = new DefaultTableModel(cols, 0);
        JTable payApptTable = UIConstants.makeStyledTable(payApptModel);

        JLabel hint = new JLabel("Select an appointment below, then click 'Collect Payment'.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(Color.GRAY);
        hint.setBorder(new EmptyBorder(0, 0, 6, 0));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIConstants.PANEL_BG);
        top.add(UIConstants.makeHeader("Appointments"), BorderLayout.NORTH);
        top.add(hint, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(payApptTable), BorderLayout.CENTER);

        // Load appointments into this table too
        loadAppointmentsIntoModel(payApptModel);

        JButton collectBtn  = new JButton("Collect Payment");
        JButton refreshBtn2 = new JButton("Refresh");
        UIConstants.styleButton(collectBtn,  UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(refreshBtn2, UIConstants.PRIMARY);

        collectBtn.addActionListener(e -> {
            int row = payApptTable.getSelectedRow();
            if (row < 0) {
                DialogUtils.showError(this, "Select an appointment first.");
                return;
            }
            String apptId  = (String) payApptModel.getValueAt(row, 0);
            String status  = (String) payApptModel.getValueAt(row, 3);
            String vehicle = (String) payApptModel.getValueAt(row, 4);
            String techId  = (String) payApptModel.getValueAt(row, 5);

            // Determine service type from appointments file
            String[] apptDetails = findAppointmentDetails(apptId);
            // apptDetails: {id, date, time, status, vehicleNo, issue, techId}
            // Service type is not stored in appointments.txt directly — it will be
            // linked via the service when AppointmentDataManager is integrated.
            // For now, we pass a default; INTEGRATION POINT: fetch from service record.
            String serviceType = "Normal Service";

            PaymentDialog dlg = new PaymentDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    apptId, "N/A", serviceType, vehicle, techId);
            dlg.setVisible(true);
            loadAppointmentsIntoModel(payApptModel);
        });

        refreshBtn2.addActionListener(e -> loadAppointmentsIntoModel(payApptModel));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(collectBtn); btnRow.add(refreshBtn2);
        panel.add(btnRow, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAppointmentsIntoModel(DefaultTableModel model) {
        model.setRowCount(0);
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 7) {
                    model.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4], p[6]});
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load appointments: " + ex.getMessage());
        }
    }

    private String[] findAppointmentDetails(String apptId) {
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 7 && p[0].equals(apptId)) return p;
            }
        } catch (IOException ignored) {}
        return null;
    }

    // ── File utility ──────────────────────────────────────────────────────────

    private void deleteLineFromFile(String filePath, String id) {
        File f = new File(filePath);
        if (!f.exists()) return;
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length > 0 && !p[0].equals(id)) lines.add(line);
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "File read error: " + ex.getMessage()); return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            for (String l : lines) pw.println(l);
        } catch (IOException ex) {
            DialogUtils.showError(this, "File write error: " + ex.getMessage());
        }
    }
}
