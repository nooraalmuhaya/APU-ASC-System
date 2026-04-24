package com.asu.model;

/**
 * Receipt Class
 * Demonstrates: Encapsulation, Document Generation Pattern, Composition
 *
 * The Receipt class handles:
 * - Receipt creation and management
 * - Receipt generation from payment
 * - Receipt printing and formatting
 * - Receipt archiving
 */
public class receipt {

    // Private attributes - Demonstrates Encapsulation
    private String receiptId;
    private String receiptDate;
    private String receiptTime;
    private String appointmentId;
    private String paymentId;
    private String customerName;
    private String customerId;
    private double serviceAmount;
    private double taxAmount;
    private double totalAmount;
    private String paymentMethod;
    private String counterStaffName;
    private String counterNumber;
    private StringBuilder receiptContent;
    private boolean isPrinted;

    /**
     * Constructor - Initialize Receipt
     * @param receiptId Unique receipt identifier
     * @param receiptDate Date of receipt
     * @param appointmentId Associated appointment
     * @param paymentId Associated payment
     */
    public receipt(String receiptId, String receiptDate, String appointmentId, String paymentId) {
        this.receiptId = receiptId;
        this.receiptDate = receiptDate;
        this.receiptTime = "[System Time]";
        this.appointmentId = appointmentId;
        this.paymentId = paymentId;
        this.receiptContent = new StringBuilder();
        this.isPrinted = false;
    }

    // ============== GETTER METHODS ==============
    public String getReceiptId() {
        return receiptId;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isPrinted() {
        return isPrinted;
    }

    public String getReceiptContent() {
        return receiptContent.toString();
    }

    // ============== SETTER METHODS ==============
    public void setCustomerName(String customerName) {
        if (customerName != null && !customerName.isEmpty()) {
            this.customerName = customerName;
        }
    }

    public void setCustomerId(String customerId) {
        if (customerId != null && !customerId.isEmpty()) {
            this.customerId = customerId;
        }
    }

    public void setServiceAmount(double serviceAmount) {
        if (serviceAmount >= 0) {
            this.serviceAmount = serviceAmount;
        }
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            this.paymentMethod = paymentMethod;
        }
    }

    public void setCounterStaffName(String counterStaffName) {
        if (counterStaffName != null && !counterStaffName.isEmpty()) {
            this.counterStaffName = counterStaffName;
        }
    }

    public void setCounterNumber(String counterNumber) {
        if (counterNumber != null && !counterNumber.isEmpty()) {
            this.counterNumber = counterNumber;
        }
    }

    // ============== RECEIPT GENERATION METHODS ==============

    /**
     * Generate receipt content
     * Demonstrates: Document generation with formatting
     *
     * @return true if receipt generated successfully
     */
    public boolean generateReceipt() {
        // Validation
        if (!isValidReceiptData()) {
            System.out.println("Error: Cannot generate receipt with incomplete data");
            return false;
        }

        // Clear previous content
        receiptContent.setLength(0);

        // Generate receipt content
        receiptContent.append("╔════════════════════════════════════════════════╗\n");
        receiptContent.append("║     APU AUTOMOTIVE SERVICE CENTRE             ║\n");
        receiptContent.append("║           SERVICE RECEIPT                      ║\n");
        receiptContent.append("╚════════════════════════════════════════════════╝\n\n");

        // Receipt Header
        receiptContent.append("Receipt No.: ").append(receiptId).append("\n");
        receiptContent.append("Date: ").append(receiptDate).append("\n");
        receiptContent.append("Time: ").append(receiptTime).append("\n");
        receiptContent.append("────────────────────────────────────────────────\n\n");

        // Customer Information
        receiptContent.append("CUSTOMER INFORMATION\n");
        receiptContent.append("Customer Name: ").append(customerName).append("\n");
        receiptContent.append("Customer ID: ").append(customerId).append("\n");
        receiptContent.append("────────────────────────────────────────────────\n\n");

        // Service Information
        receiptContent.append("SERVICE INFORMATION\n");
        receiptContent.append("Appointment ID: ").append(appointmentId).append("\n");
        receiptContent.append("Payment ID: ").append(paymentId).append("\n");
        receiptContent.append("────────────────────────────────────────────────\n\n");

        // Amount Details
        receiptContent.append("PAYMENT DETAILS\n");
        receiptContent.append("Service Amount:      RM ").append(String.format("%.2f", serviceAmount)).append("\n");

        // Calculate tax (assuming 6% Service Tax)
        this.taxAmount = (serviceAmount * 6) / 100;
        receiptContent.append("Service Tax (6%):    RM ").append(String.format("%.2f", taxAmount)).append("\n");
        receiptContent.append("─────────────────────────────────\n");

        // Total
        this.totalAmount = serviceAmount + taxAmount;
        receiptContent.append("TOTAL AMOUNT:        RM ").append(String.format("%.2f", totalAmount)).append("\n");
        receiptContent.append("────────────────────────────────────────────────\n\n");

        // Payment Information
        receiptContent.append("PAYMENT METHOD: ").append(paymentMethod).append("\n\n");

        // Counter Staff Information
        receiptContent.append("Counter Staff: ").append(counterStaffName).append("\n");
        receiptContent.append("Counter No.: ").append(counterNumber).append("\n");
        receiptContent.append("────────────────────────────────────────────────\n\n");

        // Footer
        receiptContent.append("Thank you for choosing APU Automotive Service Centre!\n");
        receiptContent.append("We appreciate your business.\n\n");
        receiptContent.append("====================================================\n");

        System.out.println("✓ Receipt generated successfully (ID: " + receiptId + ")");
        return true;
    }

    /**
     * Print the receipt
     * Demonstrates: Document output
     *
     * @return true if receipt printed successfully
     */
    public boolean printReceipt() {
        if (receiptContent.length() == 0) {
            System.out.println("Error: Receipt has not been generated yet. Please generate first.");
            return false;
        }

        System.out.println("\n");
        System.out.println(receiptContent.toString());

        this.isPrinted = true;
        System.out.println("✓ Receipt printed successfully\n");
        return true;
    }

    /**
     * Display receipt on screen without printing
     * Demonstrates: Display vs Print distinction
     */
    public void displayReceipt() {
        if (receiptContent.length() == 0) {
            System.out.println("Error: Receipt has not been generated yet");
            return;
        }

        System.out.println("\n" + receiptContent.toString());
    }

    /**
     * Export receipt as text
     * Demonstrates: Document export
     *
     * @return receipt content as string
     */
    public String exportReceiptAsText() {
        if (receiptContent.length() == 0) {
            System.out.println("Error: Receipt has not been generated yet");
            return "";
        }

        return receiptContent.toString();
    }

    /**
     * Email receipt to customer
     * Demonstrates: Delivery method
     *
     * @param customerEmail Customer's email address
     * @return true if email sent successfully
     */
    public boolean emailReceipt(String customerEmail) {
        if (receiptContent.length() == 0) {
            System.out.println("Error: Receipt has not been generated yet");
            return false;
        }

        if (customerEmail == null || !customerEmail.contains("@")) {
            System.out.println("Error: Invalid email address");
            return false;
        }

        System.out.println("✓ Receipt sent via email");
        System.out.println("  - Recipient: " + customerEmail);
        System.out.println("  - Receipt ID: " + receiptId);
        System.out.println("  - Status: Sent");

        return true;
    }

    // ============== VALIDATION & VERIFICATION ==============

    /**
     * Validate receipt data
     * Demonstrates: Comprehensive validation
     *
     * @return true if all required data is present
     */
    private boolean isValidReceiptData() {
        if (receiptId == null || receiptId.isEmpty()) {
            System.out.println("Error: Receipt ID is required");
            return false;
        }

        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID is required");
            return false;
        }

        if (paymentId == null || paymentId.isEmpty()) {
            System.out.println("Error: Payment ID is required");
            return false;
        }

        if (customerName == null || customerName.isEmpty()) {
            System.out.println("Error: Customer name is required");
            return false;
        }

        if (customerId == null || customerId.isEmpty()) {
            System.out.println("Error: Customer ID is required");
            return false;
        }

        if (serviceAmount <= 0) {
            System.out.println("Error: Service amount must be greater than 0");
            return false;
        }

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            System.out.println("Error: Payment method is required");
            return false;
        }

        if (counterStaffName == null || counterStaffName.isEmpty()) {
            System.out.println("Error: Counter staff name is required");
            return false;
        }

        if (counterNumber == null || counterNumber.isEmpty()) {
            System.out.println("Error: Counter number is required");
            return false;
        }

        return true;
    }

    /**
     * Verify receipt integrity
     * Demonstrates: Data verification
     *
     * @return true if receipt data is consistent
     */
    public boolean verifyReceipt() {
        if (totalAmount != (serviceAmount + taxAmount)) {
            System.out.println("Error: Receipt totals do not match");
            return false;
        }

        System.out.println("✓ Receipt verification successful");
        return true;
    }

    // ============== RECEIPT INFORMATION METHODS ==============

    /**
     * Display receipt summary
     * Demonstrates: Summary information
     */
    public void displayReceiptSummary() {
        System.out.println("\n════════════════════════════════════════════════");
        System.out.println("   RECEIPT SUMMARY");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("Receipt ID: " + receiptId);
        System.out.println("Date: " + receiptDate);
        System.out.println("Customer: " + customerName);
        System.out.println("Appointment: " + appointmentId);
        System.out.println("Total Amount: RM " + String.format("%.2f", totalAmount));
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Status: " + (isPrinted ? "PRINTED" : "NOT PRINTED"));
        System.out.println("════════════════════════════════════════════════\n");
    }

    /**
     * Get receipt summary as string
     */
    public String getReceiptSummary() {
        return "Receipt ID: " + receiptId +
                ", Customer: " + customerName +
                ", Total: RM " + String.format("%.2f", totalAmount) +
                ", Status: " + (isPrinted ? "Printed" : "Not Printed");
    }

    // ============== UTILITY METHODS ==============

    /**
     * Convert receipt object to string format for file storage
     * Format: receiptId|receiptDate|appointmentId|paymentId|totalAmount|paymentMethod
     */
    @Override
    public String toString() {
        return receiptId + "|" + receiptDate + "|" + appointmentId + "|" + paymentId + "|" + totalAmount + "|" + paymentMethod;
    }

    /**
     * Detailed string representation
     */
    public String toDetailedString() {
        return String.format("Receipt{id='%s', date='%s', customer='%s', total=%.2f, method='%s'}",
                receiptId, receiptDate, customerName, totalAmount, paymentMethod);
    }
}
 /**
 * noora
 */

