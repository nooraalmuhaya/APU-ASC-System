package com.asu.dialogs;

import com.asu.ui.DialogUtils;
import com.asu.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * PaymentDialog.java
 *
 * Opened by Counter Staff after selecting an appointment that is ready for payment.
 * The dialog:
 *  1. Displays the appointment summary and total amount.
 *  2. Lets the staff select a payment method (Cash, Card, Online Transfer).
 *  3. On confirming, writes the payment record to payments.txt.
 *  4. Shows a formatted receipt in a separate popup.
 *
 * File format written:
 *   paymentId|appointmentId|amount|paymentDate|paymentMethod|paymentStatus
 *
 * Pricing logic (from assignment spec):
 *   Normal Service  = RM 80.00
 *   Major Service   = RM 200.00
 *   (These will be overridden by Service prices set by the Manager in Week 3 integration.)
 *
 * INTEGRATION POINT (Week 3):
 *   - Load actual price from ServiceDataManager based on serviceType.
 *   - Write record via PaymentDataManager.getInstance().addPayment(...)
 */
public class PaymentDialog extends JDialog {

    private static final String PAYMENTS_FILE = "data/payments.txt";

    // Default prices — overridden by Manager-set prices in full integration
    private static final double NORMAL_PRICE = 80.00;
    private static final double MAJOR_PRICE  = 200.00;

    // Appointment details passed in from CounterStaffPanel
    private final String appointmentId;
    private final String customerId;
    private final String serviceType;
    private final String vehicleNo;
    private final String technicianId;
    private final double amount;

    // ── Components ─────────────────────────────────────────────────────────
    private JComboBox<String> paymentMethodCombo;

    // ── Constructor ─────────────────────────────────────────────────────────

    /**
     * @param appointmentId  the ID of the appointment being paid
     * @param customerId     the customer's userId
     * @param serviceType    "Normal Service" or "Major Service"
     * @param vehicleNo      the vehicle registration number
     * @param technicianId   the assigned technician's userId
     */
    public PaymentDialog(Frame parent, String appointmentId, String customerId,
                         String serviceType, String vehicleNo, String technicianId) {
        super(parent, "Collect Payment", true);
        this.appointmentId = appointmentId;
        this.customerId    = customerId;
        this.serviceType   = serviceType;
        this.vehicleNo     = vehicleNo;
        this.technicianId  = technicianId;
        // INTEGRATION POINT: Replace with ServiceDataManager.getPrice(serviceType)
        this.amount = "Major Service".equals(serviceType) ? MAJOR_PRICE : NORMAL_PRICE;

        setSize(420, 420);
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
        JLabel title = new JLabel("Payment Collection");
        title.setFont(UIConstants.HEADER_FONT);
        title.setForeground(Color.WHITE);
        header.add(title);

        // Summary card
        JPanel summary = new JPanel(new GridLayout(0, 2, 8, 8));
        summary.setBackground(UIConstants.PANEL_BG);
        summary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(16, 20, 16, 20)
        ));

        addSummaryRow(summary, "Appointment ID:", appointmentId);
        addSummaryRow(summary, "Customer ID:",    customerId);
        addSummaryRow(summary, "Service Type:",   serviceType);
        addSummaryRow(summary, "Vehicle No:",     vehicleNo);
        addSummaryRow(summary, "Technician ID:",  technicianId);

        // Amount row — highlighted
        JLabel amtLabel = new JLabel("Total Amount:");
        amtLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel amtValue = new JLabel(String.format("RM %.2f", amount));
        amtValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amtValue.setForeground(UIConstants.SUCCESS_COLOR);
        summary.add(amtLabel);
        summary.add(amtValue);

        // Payment method
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.setBackground(UIConstants.PANEL_BG);
        methodPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(UIConstants.LABEL_FONT);
        paymentMethodCombo = new JComboBox<>(
                new String[]{"Cash", "Card", "Online Transfer"});
        paymentMethodCombo.setFont(UIConstants.LABEL_FONT);
        methodPanel.add(methodLabel);
        methodPanel.add(paymentMethodCombo);

        // Buttons
        JButton processBtn = new JButton("Process Payment");
        JButton cancelBtn  = new JButton("Cancel");
        UIConstants.styleButton(processBtn, UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(cancelBtn,  UIConstants.DANGER);
        processBtn.addActionListener(e -> handlePayment());
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(processBtn);
        btnRow.add(cancelBtn);

        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(UIConstants.BG);
        centre.setBorder(new EmptyBorder(16, 16, 0, 16));
        centre.add(summary,     BorderLayout.NORTH);
        centre.add(methodPanel, BorderLayout.CENTER);
        centre.add(btnRow,      BorderLayout.SOUTH);

        root.add(header, BorderLayout.NORTH);
        root.add(centre, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void addSummaryRow(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.LABEL_FONT);
        JLabel val = new JLabel(value);
        val.setFont(UIConstants.LABEL_FONT);
        panel.add(lbl);
        panel.add(val);
    }

    // ── Payment Logic ─────────────────────────────────────────────────────────

    private void handlePayment() {
        if (!DialogUtils.showConfirmation(this,
                String.format("Confirm payment of RM %.2f via %s?",
                        amount, paymentMethodCombo.getSelectedItem()))) {
            return;
        }

        String paymentId  = "PAY-" + String.format("%03d", (int)(Math.random() * 900) + 100);
        String payDate    = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String method     = (String) paymentMethodCombo.getSelectedItem();
        String status     = "Paid";

        String record = String.join("|",
                paymentId, appointmentId,
                String.format("%.2f", amount),
                payDate, method, status);

        // INTEGRATION POINT (Week 3): PaymentDataManager.getInstance().addPayment(...)
        writePayment(record);

        // Show receipt popup
        showReceipt(paymentId, payDate, method);
        dispose();
    }

    private void writePayment(String record) {
        File f = new File(PAYMENTS_FILE);
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, true))) {
            pw.println(record);
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not save payment: " + ex.getMessage());
        }
    }

    /** Builds and shows a plain-text receipt in a scrollable popup. */
    private void showReceipt(String paymentId, String payDate, String method) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        String receipt =
                "===========================================\n" +
                        "        APU AUTOMOTIVE SERVICE CENTRE      \n" +
                        "===========================================\n" +
                        String.format("  Receipt ID  : %s%n", paymentId) +
                        String.format("  Payment Date: %s  %s%n", payDate, time) +
                        "-------------------------------------------\n" +
                        String.format("  Appointment : %s%n", appointmentId) +
                        String.format("  Customer    : %s%n", customerId) +
                        String.format("  Vehicle No  : %s%n", vehicleNo) +
                        String.format("  Service     : %s%n", serviceType) +
                        String.format("  Technician  : %s%n", technicianId) +
                        "-------------------------------------------\n" +
                        String.format("  Amount Paid : RM %.2f%n", amount) +
                        String.format("  Method      : %s%n", method) +
                        String.format("  Status      : PAID%n") +
                        "===========================================\n" +
                        "       Thank you for choosing APU-ASC      \n" +
                        "===========================================";

        JTextArea area = new JTextArea(receipt);
        area.setFont(new Font("Courier New", Font.PLAIN, 12));
        area.setEditable(false);
        area.setBackground(new Color(252, 252, 252));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 340));

        JOptionPane.showMessageDialog(
                this, scroll, "Receipt — " + paymentId,
                JOptionPane.PLAIN_MESSAGE);
    }
}
