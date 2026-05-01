package com.asu.ui.panels;

import com.asu.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TechnicianPanel.java
 *
 * Dashboard for a Technician. Three tabs:
 *   1. My Appointments       — Table of appointments assigned to this technician
 *   2. Appointment Details   — Full detail view of a selected appointment + update status
 *   3. Provide Feedback      — Submit feedback text for a selected appointment
 *
 * Technician Permissions (from assignment Table 1.0):
 *   ✔ Edit own profile
 *   ✔ Check comments and details of individual assigned appointments
 *   ✔ Update individual assigned appointments to "Completed"
 *   ✔ Provide feedbacks for individual assigned appointments
 *
 * INTEGRATION POINT (Week 3):
 *   Replace file reads with AppointmentDataManager and FeedbackDataManager.
 */
public class TechnicianPanel extends JPanel {

    private static final String APPOINTMENTS_FILE = "data/appointments.txt";
    private static final String FEEDBACK_FILE     = "data/feedback.txt";

    private final String[] currentUser; // includes userId at index 0
    private final Runnable onLogout;

    // ── Tab 1 ─────────────────────────────────────────────────────────────────
    private DefaultTableModel apptModel;
    private JTable            apptTable;

    // ── Tab 2 ─────────────────────────────────────────────────────────────────
    private JLabel apptIdLabel, dateLabel, timeLabel, vehicleLabel, issueLabel, statusLabel;
    private JButton updateStatusBtn;

    // ── Tab 3 ─────────────────────────────────────────────────────────────────
    private JComboBox<String> apptCombo;
    private JTextArea         feedbackArea;

    // ── Constructor ───────────────────────────────────────────────────────────

    public TechnicianPanel(String[] currentUser, Runnable onLogout) {
        this.currentUser = currentUser;
        this.onLogout    = onLogout;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG);
        buildUI();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Inline popup helpers — no external DialogUtils dependency
    // ─────────────────────────────────────────────────────────────────────────

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean showConfirmation(String message) {
        return JOptionPane.showConfirmDialog(
                this, message, "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        add(buildTopBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIConstants.LABEL_FONT);
        tabs.addTab("📋  My Appointments",  buildAppointmentsTab());
        tabs.addTab("🔍  Details & Status", buildDetailsTab());
        tabs.addTab("💬  Provide Feedback", buildFeedbackTab());

        // When the user switches to tab 2 or 3, refresh the appt combo
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 2) populateFeedbackCombo();
        });

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.PRIMARY);
        bar.setBorder(new EmptyBorder(10, 16, 10, 16));
        JLabel welcome = new JLabel("Technician Dashboard  |  " + currentUser[1]);
        welcome.setFont(UIConstants.HEADER_FONT);
        welcome.setForeground(Color.WHITE);
        JButton logoutBtn = new JButton("Logout");
        UIConstants.styleButton(logoutBtn, UIConstants.DANGER);
        logoutBtn.addActionListener(e -> onLogout.run());
        bar.add(welcome,   BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    // ── Tab 1: My Appointments ────────────────────────────────────────────────

    private JPanel buildAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("My Assigned Appointments"), BorderLayout.NORTH);

        String[] cols = {"Appt ID", "Date", "Time", "Status", "Vehicle No"};
        apptModel = new DefaultTableModel(cols, 0);
        apptTable = UIConstants.makeStyledTable(apptModel);
        panel.add(new JScrollPane(apptTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);
        refreshBtn.addActionListener(e -> loadMyAppointments());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        loadMyAppointments();
        return panel;
    }

    /**
     * Reads appointments.txt and shows only those assigned to the current technician.
     * INTEGRATION POINT: AppointmentDataManager.getByTechnicianId(currentUser[0])
     */
    private void loadMyAppointments() {
        apptModel.setRowCount(0);
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                // format: appointmentId|date|time|status|vehicleNo|issue|technicianId
                if (p.length >= 7 && p[6].equals(currentUser[0])) {
                    apptModel.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4]});
                }
            }
        } catch (IOException ex) {
            showError("Could not load appointments: " + ex.getMessage());
        }
    }

    // ── Tab 2: Details & Status Update ───────────────────────────────────────

    private JPanel buildDetailsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("Appointment Details"), BorderLayout.NORTH);

        // Instruction label
        JLabel hint = new JLabel(
                "Select an appointment in the 'My Appointments' tab, then click 'Load Details'.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(Color.GRAY);

        // Details card
        JPanel details = new JPanel(new GridLayout(0, 2, 8, 10));
        details.setBackground(UIConstants.PANEL_BG);
        details.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(16, 20, 16, 20)
        ));

        apptIdLabel  = makeDetailValue("—");
        dateLabel    = makeDetailValue("—");
        timeLabel    = makeDetailValue("—");
        vehicleLabel = makeDetailValue("—");
        statusLabel  = makeDetailValue("—");
        issueLabel   = makeDetailValue("—");
        issueLabel.setFont(UIConstants.LABEL_FONT);

        addDetailRow(details, "Appointment ID:", apptIdLabel);
        addDetailRow(details, "Date:",           dateLabel);
        addDetailRow(details, "Time:",           timeLabel);
        addDetailRow(details, "Vehicle No:",     vehicleLabel);
        addDetailRow(details, "Status:",         statusLabel);
        addDetailRow(details, "Issue:",          issueLabel);

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setBackground(UIConstants.PANEL_BG);
        top.add(hint,    BorderLayout.NORTH);
        top.add(details, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);

        // Buttons
        JButton loadBtn   = new JButton("Load Details");
        updateStatusBtn   = new JButton("Mark as Completed");
        updateStatusBtn.setEnabled(false);
        UIConstants.styleButton(loadBtn,         UIConstants.SECONDARY);
        UIConstants.styleButton(updateStatusBtn, UIConstants.SUCCESS_COLOR);

        loadBtn.addActionListener(e -> loadSelectedDetails());
        updateStatusBtn.addActionListener(e -> markCompleted());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(loadBtn);
        btnRow.add(updateStatusBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel makeDetailValue(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.LABEL_FONT);
        return lbl;
    }

    private void addDetailRow(JPanel panel, String label, JLabel value) {
        JLabel lbl = UIConstants.makeFormLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl);
        panel.add(value);
    }

    private void loadSelectedDetails() {
        int row = apptTable.getSelectedRow();
        if (row < 0) {
            showError("Go to 'My Appointments' tab and select an appointment first.");
            return;
        }
        String apptId = (String) apptModel.getValueAt(row, 0);
        String[] p    = findAppointmentById(apptId);
        if (p == null) {
            showError("Appointment not found."); return;
        }
        apptIdLabel.setText(p[0]);
        dateLabel.setText(p[1]);
        timeLabel.setText(p[2]);
        statusLabel.setText(p[3]);
        vehicleLabel.setText(p[4]);
        issueLabel.setText(p.length > 5 ? p[5] : "—");

        // Only allow marking complete if not already completed
        boolean canComplete = !"Completed".equals(p[3]);
        updateStatusBtn.setEnabled(canComplete);
    }

    private void markCompleted() {
        String apptId = apptIdLabel.getText();
        if ("—".equals(apptId)) return;
        if (!showConfirmation("Mark appointment " + apptId + " as Completed?")) return;

        // INTEGRATION POINT: AppointmentDataManager.getInstance().updateStatus(apptId, "Completed")
        updateStatusInFile(apptId, "Completed");
        statusLabel.setText("Completed");
        updateStatusBtn.setEnabled(false);
        loadMyAppointments();
        showSuccess("Appointment " + apptId + " marked as Completed.");
    }

    // ── Tab 3: Provide Feedback ───────────────────────────────────────────────

    private JPanel buildFeedbackTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.add(UIConstants.makeHeader("Submit Feedback for Appointment"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PANEL_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Appointment selector
        apptCombo = new JComboBox<>();
        apptCombo.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(UIConstants.makeFormLabel("Select Appointment:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(apptCombo, gbc);

        // Feedback text area
        feedbackArea = new JTextArea(5, 30);
        feedbackArea.setFont(UIConstants.LABEL_FONT);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(UIConstants.makeFormLabel("Feedback:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(feedbackScroll, gbc);

        panel.add(form, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Submit Feedback");
        UIConstants.styleButton(submitBtn, UIConstants.SUCCESS_COLOR);
        submitBtn.addActionListener(e -> handleFeedbackSubmit());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(submitBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        populateFeedbackCombo();
        return panel;
    }

    private void populateFeedbackCombo() {
        if (apptCombo == null) return;
        apptCombo.removeAllItems();
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) { apptCombo.addItem("No appointments found"); return; }
        boolean any = false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 7 && p[6].equals(currentUser[0])) {
                    apptCombo.addItem(p[0] + " — " + p[1] + " " + p[2]);
                    any = true;
                }
            }
        } catch (IOException ignored) {}
        if (!any) apptCombo.addItem("No appointments assigned");
    }

    private void handleFeedbackSubmit() {
        String selected = (String) apptCombo.getSelectedItem();
        String comment  = feedbackArea.getText().trim();

        if (selected == null || selected.startsWith("No ")) {
            showError("No appointment selected."); return;
        }
        if (comment.isEmpty()) {
            showError("Please enter your feedback before submitting."); return;
        }

        String apptId    = selected.split(" — ")[0];
        String feedbackId = "FBK-" + String.format("%03d", (int)(Math.random() * 900) + 100);
        String date       = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // feedbackId|appointmentId|comment|feedbackDate|feedbackFrom
        String record = String.join("|", feedbackId, apptId, comment, date, currentUser[0]);

        // INTEGRATION POINT: FeedbackDataManager.getInstance().addFeedback(...)
        writeFeedback(record);
        feedbackArea.setText("");
        showSuccess("Feedback submitted for appointment " + apptId + ".");
    }

    // ── File utilities ────────────────────────────────────────────────────────

    private String[] findAppointmentById(String apptId) {
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

    private void updateStatusInFile(String apptId, String newStatus) {
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) return;
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 7 && p[0].equals(apptId)) {
                    p[3] = newStatus;
                    lines.add(String.join("|", p));
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            showError("Read error: " + ex.getMessage()); return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            for (String l : lines) pw.println(l);
        } catch (IOException ex) {
            showError("Write error: " + ex.getMessage());
        }
    }

    private void writeFeedback(String record) {
        File f = new File(FEEDBACK_FILE);
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, true))) {
            pw.println(record);
        } catch (IOException ex) {
            showError("Could not save feedback: " + ex.getMessage());
        }
    }
}
