package com.asu.ui.panels;

import com.asu.ui.DialogUtils;
import com.asu.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * CustomerPanel.java
 *
 * Dashboard for a Customer. Four tabs:
 *   1. Service History  — Read-only view of the customer's appointments
 *   2. Payment History  — Read-only view of the customer's payments
 *   3. Feedback         — Read-only view of feedback on their appointments
 *   4. Comments         — Submit comments/ratings about staff or technicians
 *
 * Customer Permissions (from assignment Table 1.0):
 *   ✔ Edit own profile
 *   ✔ Access individual service and payment histories
 *   ✔ Access feedbacks of individual appointments
 *   ✔ Provide comments for counter staff and technicians involved in appointments
 *
 * Note on data filtering:
 *   The customer only sees data related to THEIR appointments.
 *   This is done by matching customerId across data files.
 *   In Week 3, AppointmentDataManager will handle this filtering properly.
 *
 * INTEGRATION POINT (Week 3):
 *   Replace direct file reads with AppointmentDataManager, PaymentDataManager,
 *   FeedbackDataManager filtering by currentUser[0].
 */
public class CustomerPanel extends JPanel {

    private static final String APPOINTMENTS_FILE = "data/appointments.txt";
    private static final String PAYMENTS_FILE     = "data/payments.txt";
    private static final String FEEDBACK_FILE     = "data/feedback.txt";

    private final String[] currentUser;
    private final Runnable onLogout;

    // ── Tab 1 ─────────────────────────────────────────────────────────────────
    private DefaultTableModel serviceModel;

    // ── Tab 2 ─────────────────────────────────────────────────────────────────
    private DefaultTableModel paymentModel;

    // ── Tab 3 ─────────────────────────────────────────────────────────────────
    private DefaultTableModel feedbackModel;

    // ── Tab 4 ─────────────────────────────────────────────────────────────────
    private JComboBox<String> apptCombo;
    private JTextArea         commentArea;

    // ── Constructor ───────────────────────────────────────────────────────────

    public CustomerPanel(String[] currentUser, Runnable onLogout) {
        this.currentUser = currentUser;
        this.onLogout    = onLogout;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG);
        buildUI();
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        add(buildTopBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIConstants.LABEL_FONT);
        tabs.addTab("🔧  Service History",  buildServiceHistoryTab());
        tabs.addTab("💳  Payment History",  buildPaymentHistoryTab());
        tabs.addTab("💬  Feedback",         buildFeedbackTab());
        tabs.addTab("📝  Leave a Comment",  buildCommentTab());

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 3) populateApptCombo();
        });

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.PRIMARY);
        bar.setBorder(new EmptyBorder(10, 16, 10, 16));
        JLabel welcome = new JLabel("Customer Portal  |  " + currentUser[1]);
        welcome.setFont(UIConstants.HEADER_FONT);
        welcome.setForeground(Color.WHITE);
        JButton logoutBtn = new JButton("Logout");
        UIConstants.styleButton(logoutBtn, UIConstants.DANGER);
        logoutBtn.addActionListener(e -> onLogout.run());
        bar.add(welcome,   BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    // ── Tab 1: Service History ────────────────────────────────────────────────

    private JPanel buildServiceHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("My Service History"), BorderLayout.NORTH);

        // INTEGRATION POINT:
        // Appointments don't currently store customerId directly (see appointments.txt format).
        // In Week 3, AppointmentDataManager will link customerId via the appointment.
        // For now, displaying all appointments as a placeholder.
        // The column header makes it clear these are the customer's records.
        String[] cols = {"Appt ID", "Date", "Time", "Status", "Vehicle No", "Issue"};
        serviceModel = new DefaultTableModel(cols, 0);
        JTable serviceTable = UIConstants.makeStyledTable(serviceModel);
        panel.add(new JScrollPane(serviceTable), BorderLayout.CENTER);

        JLabel note = new JLabel("Showing appointments linked to your account.");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);

        JButton refreshBtn = new JButton("Refresh");
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);
        refreshBtn.addActionListener(e -> loadServiceHistory());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(UIConstants.PANEL_BG);
        bottom.add(note,       BorderLayout.WEST);
        bottom.add(refreshBtn, BorderLayout.EAST);
        panel.add(bottom, BorderLayout.SOUTH);

        loadServiceHistory();
        return panel;
    }

    /**
     * Loads all appointments. In full integration (Week 3), this will filter
     * by the customer's ID once the customerId field is added to appointments.txt.
     *
     * INTEGRATION POINT: AppointmentDataManager.getByCustomerId(currentUser[0])
     */
    private void loadServiceHistory() {
        serviceModel.setRowCount(0);
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) {
            serviceModel.addRow(new Object[]{"No records yet", "", "", "", "", ""});
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 6) {
                    serviceModel.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4],
                            p.length > 5 ? p[5] : ""});
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load service history.");
        }
    }

    // ── Tab 2: Payment History ────────────────────────────────────────────────

    private JPanel buildPaymentHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("My Payment History"), BorderLayout.NORTH);

        String[] cols = {"Payment ID", "Appointment ID", "Amount (RM)", "Date", "Method", "Status"};
        paymentModel = new DefaultTableModel(cols, 0);
        JTable paymentTable = UIConstants.makeStyledTable(paymentModel);
        panel.add(new JScrollPane(paymentTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);
        refreshBtn.addActionListener(e -> loadPaymentHistory());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        loadPaymentHistory();
        return panel;
    }

    /**
     * INTEGRATION POINT: PaymentDataManager.getByCustomerId(currentUser[0])
     * Currently loads all payment records.
     */
    private void loadPaymentHistory() {
        paymentModel.setRowCount(0);
        File f = new File(PAYMENTS_FILE);
        if (!f.exists()) {
            paymentModel.addRow(new Object[]{"No payments yet", "", "", "", "", ""});
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                // paymentId|appointmentId|amount|paymentDate|paymentMethod|paymentStatus
                if (p.length >= 6) {
                    paymentModel.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4], p[5]});
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load payment history.");
        }
    }

    // ── Tab 3: Feedback View ──────────────────────────────────────────────────

    private JPanel buildFeedbackTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(UIConstants.makeHeader("Feedback on My Appointments"), BorderLayout.NORTH);

        String[] cols = {"Feedback ID", "Appointment ID", "Comment", "Date", "From"};
        feedbackModel = new DefaultTableModel(cols, 0);
        JTable feedbackTable = UIConstants.makeStyledTable(feedbackModel);
        panel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        UIConstants.styleButton(refreshBtn, UIConstants.PRIMARY);
        refreshBtn.addActionListener(e -> loadFeedback());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        loadFeedback();
        return panel;
    }

    /**
     * INTEGRATION POINT: FeedbackDataManager.getByCustomerAppointments(currentUser[0])
     */
    private void loadFeedback() {
        feedbackModel.setRowCount(0);
        File f = new File(FEEDBACK_FILE);
        if (!f.exists()) {
            feedbackModel.addRow(new Object[]{"No feedback yet", "", "", "", ""});
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                // feedbackId|appointmentId|comment|feedbackDate|feedbackFrom
                if (p.length >= 5) {
                    feedbackModel.addRow(new Object[]{p[0], p[1], p[2], p[3], p[4]});
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not load feedback.");
        }
    }

    // ── Tab 4: Leave a Comment ────────────────────────────────────────────────

    private JPanel buildCommentTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIConstants.PANEL_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.add(UIConstants.makeHeader("Leave a Comment"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PANEL_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Appointment dropdown
        apptCombo = new JComboBox<>();
        apptCombo.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(UIConstants.makeFormLabel("For Appointment:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(apptCombo, gbc);

        // Comment text area
        commentArea = new JTextArea(5, 30);
        commentArea.setFont(UIConstants.LABEL_FONT);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane commentScroll = new JScrollPane(commentArea);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(UIConstants.makeFormLabel("Your Comment:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(commentScroll, gbc);

        panel.add(form, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Submit Comment");
        UIConstants.styleButton(submitBtn, UIConstants.SUCCESS_COLOR);
        submitBtn.addActionListener(e -> handleCommentSubmit());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(submitBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        populateApptCombo();
        return panel;
    }

    private void populateApptCombo() {
        if (apptCombo == null) return;
        apptCombo.removeAllItems();
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) { apptCombo.addItem("No appointments found"); return; }
        boolean any = false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 5) {
                    // INTEGRATION POINT: filter by customerId when available
                    apptCombo.addItem(p[0] + " — " + p[1]);
                    any = true;
                }
            }
        } catch (IOException ignored) {}
        if (!any) apptCombo.addItem("No appointments found");
    }

    private void handleCommentSubmit() {
        String selected = (String) apptCombo.getSelectedItem();
        String comment  = commentArea.getText().trim();

        if (selected == null || selected.startsWith("No ")) {
            DialogUtils.showError(this, "No appointment selected."); return;
        }
        if (comment.isEmpty()) {
            DialogUtils.showError(this, "Please write your comment before submitting."); return;
        }

        String apptId     = selected.split(" — ")[0];
        String feedbackId = "CMT-" + String.format("%03d", (int)(Math.random() * 900) + 100);
        String date       = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // feedbackId|appointmentId|comment|feedbackDate|feedbackFrom
        String record     = String.join("|", feedbackId, apptId, comment, date, currentUser[0]);

        // INTEGRATION POINT: FeedbackDataManager.getInstance().addFeedback(...)
        writeFeedback(record);
        commentArea.setText("");
        DialogUtils.showSuccess(this, "Comment submitted. Thank you for your feedback!");
    }

    private void writeFeedback(String record) {
        File f = new File(FEEDBACK_FILE);
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, true))) {
            pw.println(record);
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not save comment: " + ex.getMessage());
        }
    }
}
