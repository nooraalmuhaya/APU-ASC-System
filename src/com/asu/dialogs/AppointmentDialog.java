package com.asu.dialogs;

import com.asu.ui.DialogUtils;
import com.asu.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentDialog.java
 *
 * Opened by Counter Staff to create a new appointment for a customer.
 * The dialog:
 *  1. Collects customer ID, service type, date, time, vehicle number, issue description.
 *  2. Loads available technicians by checking who is NOT already booked
 *     at the chosen date/time in appointments.txt.
 *  3. Writes the new appointment to appointments.txt.
 *
 * Service types (from assignment spec):
 *   Normal Service  = 1 hour
 *   Major Service   = 3 hours
 *
 * File format written:
 *   appointmentId|appointmentDate|appointmentTime|status|vehicleNo|issueDescription|technicianId
 *
 * INTEGRATION POINT (Week 3):
 *   Replace direct file reads with AppointmentDataManager and UserDataManager calls.
 */
public class AppointmentDialog extends JDialog {

    private static final String APPOINTMENTS_FILE = "data/appointments.txt";
    private static final String USERS_FILE        = "data/users.txt";

    // The Counter Staff's userId (used to track who created the appointment if needed)
    private final String staffId;

    // ── Form Fields ───────────────────────────────────────────────────────────
    private JTextField    customerIdField;
    private JComboBox<String> serviceTypeCombo;
    private JTextField    dateField;
    private JTextField    timeField;
    private JTextField    vehicleField;
    private JTextArea     issueArea;
    private JComboBox<String> technicianCombo;
    private JLabel        durationLabel;

    // ── Constructor ───────────────────────────────────────────────────────────

    public AppointmentDialog(Frame parent, String staffId) {
        super(parent, "Create New Appointment", true);
        this.staffId = staffId;
        setSize(480, 540);
        setResizable(false);
        setLocationRelativeTo(parent);
        buildUI();
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.BG);

        // Header
        JPanel header = new JPanel();
        header.setBackground(UIConstants.PRIMARY);
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel title = new JLabel("New Appointment");
        title.setFont(UIConstants.HEADER_FONT);
        title.setForeground(Color.WHITE);
        header.add(title);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PANEL_BG);
        form.setBorder(new EmptyBorder(16, 28, 16, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Customer ID
        customerIdField = new JTextField();
        customerIdField.setToolTipText("Enter the Customer's User ID (e.g. CUS-001)");
        addRow(form, gbc, 0, "Customer ID:", customerIdField);

        // Service Type
        serviceTypeCombo = new JComboBox<>(new String[]{"Normal Service", "Major Service"});
        serviceTypeCombo.setFont(UIConstants.LABEL_FONT);
        addRow(form, gbc, 1, "Service Type:", serviceTypeCombo);

        // Duration label (auto-updates when service type changes)
        durationLabel = new JLabel("Duration: 1 hour");
        durationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        durationLabel.setForeground(Color.GRAY);
        gbc.gridx = 1; gbc.gridy = 2;
        form.add(durationLabel, gbc);

        serviceTypeCombo.addActionListener(e -> {
            String selected = (String) serviceTypeCombo.getSelectedItem();
            durationLabel.setText("Duration: " +
                    ("Major Service".equals(selected) ? "3 hours" : "1 hour"));
            refreshTechnicianList(); // re-check availability
        });

        // Date
        dateField = new JTextField(LocalDate.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        dateField.setToolTipText("Format: DD-MM-YYYY");
        addRow(form, gbc, 3, "Date:", dateField);

        // Time
        timeField = new JTextField("09:00");
        timeField.setToolTipText("Format: HH:MM  (24-hour)");
        addRow(form, gbc, 4, "Time:", timeField);

        // Vehicle No
        vehicleField = new JTextField();
        vehicleField.setToolTipText("e.g. WXX 1234");
        addRow(form, gbc, 5, "Vehicle No:", vehicleField);

        // Issue Description
        issueArea = new JTextArea(3, 20);
        issueArea.setFont(UIConstants.LABEL_FONT);
        issueArea.setLineWrap(true);
        issueArea.setWrapStyleWord(true);
        issueArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane issueScroll = new JScrollPane(issueArea);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1; gbc.weightx = 0;
        form.add(UIConstants.makeFormLabel("Issue:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(issueScroll, gbc);

        // Technician (populated from available technicians)
        technicianCombo = new JComboBox<>();
        technicianCombo.setFont(UIConstants.LABEL_FONT);
        addRow(form, gbc, 7, "Assign Technician:", technicianCombo);

        // Refresh technician button
        JButton refreshBtn = new JButton("Check Availability");
        UIConstants.styleButton(refreshBtn, UIConstants.SECONDARY);
        refreshBtn.addActionListener(e -> refreshTechnicianList());
        gbc.gridx = 1; gbc.gridy = 8;
        form.add(refreshBtn, gbc);

        // Buttons
        JButton createBtn = new JButton("Create Appointment");
        JButton cancelBtn = new JButton("Cancel");
        UIConstants.styleButton(createBtn, UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(cancelBtn, UIConstants.DANGER);
        createBtn.addActionListener(e -> handleCreate());
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(createBtn);
        btnRow.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnRow, gbc);

        root.add(header, BorderLayout.NORTH);
        root.add(new JScrollPane(form), BorderLayout.CENTER);
        setContentPane(root);

        // Initial technician load
        refreshTechnicianList();
    }

    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(UIConstants.makeFormLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // ── Technician Availability ───────────────────────────────────────────────

    /**
     * Reads all technicians from users.txt and removes those who already
     * have an appointment at the same date and time in appointments.txt.
     *
     * INTEGRATION POINT (Week 3): Replace with
     *   AppointmentDataManager.getAvailableTechnicians(date, time, serviceType)
     */
    private void refreshTechnicianList() {
        technicianCombo.removeAllItems();

        String date = dateField.getText().trim();
        String time = timeField.getText().trim();

        // Step 1: Load all technicians from users.txt
        List<String[]> technicians = new ArrayList<>();
        File usersFile = new File(USERS_FILE);
        if (usersFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.trim().split("\\|");
                    if (p.length >= 6 && "Technician".equals(p[5])) {
                        technicians.add(p); // {userId, name, phone, username, password, Technician}
                    }
                }
            } catch (IOException ignored) {}
        }

        // Step 2: Find technician IDs already booked at this date/time
        List<String> bookedIds = new ArrayList<>();
        File apptFile = new File(APPOINTMENTS_FILE);
        if (apptFile.exists() && !date.isEmpty() && !time.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new FileReader(apptFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.trim().split("\\|");
                    // appointmentId|date|time|status|vehicleNo|issue|technicianId
                    if (p.length >= 7 && p[1].equals(date) && p[2].equals(time)
                            && !"Completed".equals(p[3])) {
                        bookedIds.add(p[6]);
                    }
                }
            } catch (IOException ignored) {}
        }

        // Step 3: Populate combo with only available technicians
        boolean anyAvailable = false;
        for (String[] tech : technicians) {
            if (!bookedIds.contains(tech[0])) {
                // Show "TEC-001 — Ahmad Razif" style
                technicianCombo.addItem(tech[0] + " — " + tech[1]);
                anyAvailable = true;
            }
        }

        if (!anyAvailable) {
            technicianCombo.addItem("— No technicians available at this time —");
        }
    }

    // ── Create Appointment ────────────────────────────────────────────────────

    private void handleCreate() {
        String customerId   = customerIdField.getText().trim();
        String serviceType  = (String) serviceTypeCombo.getSelectedItem();
        String date         = dateField.getText().trim();
        String time         = timeField.getText().trim();
        String vehicleNo    = vehicleField.getText().trim();
        String issue        = issueArea.getText().trim();
        String techSelected = (String) technicianCombo.getSelectedItem();

        // Validation
        if (customerId.isEmpty() || date.isEmpty() || time.isEmpty()
                || vehicleNo.isEmpty() || issue.isEmpty()) {
            DialogUtils.showError(this, "All fields are required.");
            return;
        }
        if (techSelected == null || techSelected.startsWith("—")) {
            DialogUtils.showError(this,
                    "No technician available. Change the date or time and try again.");
            return;
        }
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            DialogUtils.showError(this, "Invalid date format. Use DD-MM-YYYY.");
            return;
        }
        if (!time.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            DialogUtils.showError(this, "Invalid time format. Use HH:MM (24-hour).");
            return;
        }

        // Extract technicianId from the combo selection (format: "TEC-001 — Name")
        String techId = techSelected.split(" — ")[0].trim();

        // Build appointment record
        String apptId  = "APT-" + String.format("%03d", (int)(Math.random() * 900) + 100);
        String status  = "Pending";
        String record  = String.join("|", apptId, date, time, status,
                vehicleNo, issue, techId);

        // INTEGRATION POINT (Week 3): AppointmentDataManager.getInstance().addAppointment(...)
        writeAppointment(record);

        DialogUtils.showSuccess(this,
                "Appointment " + apptId + " created successfully.\n" +
                        "Assigned to: " + techSelected);
        dispose();
    }

    private void writeAppointment(String record) {
        File f = new File(APPOINTMENTS_FILE);
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, true))) {
            pw.println(record);
        } catch (IOException ex) {
            DialogUtils.showError(this,
                    "Could not save appointment: " + ex.getMessage());
        }
    }
}
