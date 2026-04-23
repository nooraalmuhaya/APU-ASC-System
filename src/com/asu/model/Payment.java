package com.asu.model;

/**
 * Payment Class
 * Demonstrates: Encapsulation, Data Abstraction, Business Transaction Pattern
 *
 * The Payment class handles:
 * - Payment details and status tracking
 * - Payment processing with validation
 * - Payment history and records
 * - Multiple payment methods support
 */
public class Payment {

    // Private attributes - Demonstrates Encapsulation
    private String paymentId;
    private String appointmentId;
    private double amount;
    private String paymentDate;
    private String paymentMethod;    // Cash, Card, Cheque
    private String paymentStatus;    // Pending, Completed, Failed, Refunded
    private String transactionId;
    private String remarks;

    /**
     * Constructor - Initialize Payment with required details
     * @param paymentId Unique payment identifier
     * @param appointmentId Associated appointment ID
     * @param amount Payment amount
     * @param paymentDate Date of payment
     * @param paymentMethod Payment method (Cash, Card, Cheque)
     */
    public Payment(String paymentId, String appointmentId, double amount,
                   String paymentDate, String paymentMethod) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "Pending";
        this.transactionId = "TXN_" + System.currentTimeMillis();
        this.remarks = "";
    }

    // ============== GETTER METHODS ==============
    public String getPaymentId() {
        return paymentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getRemarks() {
        return remarks;
    }

    // ============== SETTER METHODS ==============
    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            if (isValidPaymentMethod(paymentMethod)) {
                this.paymentMethod = paymentMethod;
            } else {
                System.out.println("Error: Invalid payment method. Accepted: Cash, Card, Cheque");
            }
        }
    }

    public void setRemarks(String remarks) {
        if (remarks != null) {
            this.remarks = remarks;
        }
    }

    // ============== PAYMENT PROCESSING METHODS ==============

    /**
     * Process the payment
     * Demonstrates: Complex transaction logic with validation
     *
     * @return true if payment processed successfully
     */
    public boolean processPayment() {
        // Validation
        if (!isValidPaymentData()) {
            System.out.println("Error: Invalid payment data");
            return false;
        }

        // Attempt payment processing based on method
        boolean processResult = false;

        switch (paymentMethod.toLowerCase()) {
            case "cash":
                processResult = processCashPayment();
                break;
            case "card":
                processResult = processCardPayment();
                break;
            case "cheque":
                processResult = processChequePayment();
                break;
            default:
                System.out.println("Error: Unknown payment method");
                return false;
        }

        if (processResult) {
            this.paymentStatus = "Completed";
            System.out.println("✓ Payment processed successfully");
        } else {
            this.paymentStatus = "Failed";
            System.out.println("✗ Payment processing failed");
        }

        return processResult;
    }

    /**
     * Process cash payment
     * Demonstrates: Specific payment method handling
     *
     * @return true if cash payment successful
     */
    private boolean processCashPayment() {
        System.out.println("Processing Cash Payment...");
        System.out.println("  - Amount: RM " + String.format("%.2f", amount));
        System.out.println("  - Please count the cash amount");
        System.out.println("  - Verifying amount...");

        // Simulate verification
        System.out.println("  ✓ Cash amount verified");
        return true;
    }

    /**
     * Process card payment
     * Demonstrates: Payment gateway simulation
     *
     * @return true if card payment successful
     */
    private boolean processCardPayment() {
        System.out.println("Processing Card Payment...");
        System.out.println("  - Amount: RM " + String.format("%.2f", amount));
        System.out.println("  - Connecting to payment gateway...");
        System.out.println("  - Processing transaction...");

        // Simulate card processing (in real system, connect to actual gateway)
        System.out.println("  ✓ Card processed successfully");
        System.out.println("  - Transaction ID: " + transactionId);
        return true;
    }

    /**
     * Process cheque payment
     * Demonstrates: Delayed payment processing
     *
     * @return true if cheque registered successfully
     */
    private boolean processChequePayment() {
        System.out.println("Processing Cheque Payment...");
        System.out.println("  - Amount: RM " + String.format("%.2f", amount));
        System.out.println("  - Registering cheque...");
        System.out.println("  - Cheque will be cleared in 3-5 working days");

        this.paymentStatus = "Pending";  // Cheque payment remains pending until cleared
        return true;
    }

    /**
     * Refund a payment
     * Demonstrates: Reverse transaction logic
     *
     * @param refundAmount Amount to refund (must be <= original amount)
     * @param refundReason Reason for refund
     * @return true if refund processed successfully
     */
    public boolean refundPayment(double refundAmount, String refundReason) {
        // Validation
        if (refundAmount <= 0) {
            System.out.println("Error: Refund amount must be greater than 0");
            return false;
        }

        if (refundAmount > amount) {
            System.out.println("Error: Refund amount cannot exceed original payment amount");
            System.out.println("Original Amount: RM " + String.format("%.2f", amount));
            System.out.println("Refund Requested: RM " + String.format("%.2f", refundAmount));
            return false;
        }

        if (!paymentStatus.equalsIgnoreCase("Completed")) {
            System.out.println("Error: Can only refund completed payments");
            return false;
        }

        System.out.println("✓ Refund processed successfully:");
        System.out.println("  - Payment ID: " + paymentId);
        System.out.println("  - Refund Amount: RM " + String.format("%.2f", refundAmount));
        System.out.println("  - Original Amount: RM " + String.format("%.2f", amount));
        System.out.println("  - Remaining Balance: RM " + String.format("%.2f", (amount - refundAmount)));
        System.out.println("  - Refund Reason: " + refundReason);
        System.out.println("  - Status: REFUNDED");

        if (refundAmount == amount) {
            this.paymentStatus = "Refunded";
        } else {
            this.paymentStatus = "Partially Refunded";
        }

        return true;
    }

    // ============== VALIDATION & VERIFICATION METHODS ==============

    /**
     * Validate all payment data
     * Demonstrates: Comprehensive input validation
     *
     * @return true if all payment data is valid
     */
    private boolean isValidPaymentData() {
        if (paymentId == null || paymentId.isEmpty()) {
            System.out.println("Error: Payment ID cannot be empty");
            return false;
        }

        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Error: Payment amount must be greater than 0");
            return false;
        }

        if (!isValidPaymentMethod(paymentMethod)) {
            System.out.println("Error: Invalid payment method");
            return false;
        }

        if (paymentDate == null || paymentDate.isEmpty()) {
            System.out.println("Error: Payment date cannot be empty");
            return false;
        }

        return true;
    }

    /**
     * Validate payment method
     */
    private boolean isValidPaymentMethod(String method) {
        if (method == null || method.isEmpty()) {
            return false;
        }
        return method.equalsIgnoreCase("Cash") ||
                method.equalsIgnoreCase("Card") ||
                method.equalsIgnoreCase("Cheque");
    }

    /**
     * Verify payment status
     * Demonstrates: Status checking
     *
     * @return true if payment is completed
     */
    public boolean isPaymentCompleted() {
        return paymentStatus.equalsIgnoreCase("Completed");
    }

    // ============== PAYMENT INFORMATION METHODS ==============

    /**
     * Display payment details
     * Demonstrates: Information presentation
     */
    public void displayPaymentDetails() {
        System.out.println("\n════════════════════════════════════════════════");
        System.out.println("   PAYMENT DETAILS");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Amount: RM " + String.format("%.2f", amount));
        System.out.println("Payment Date: " + paymentDate);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Payment Status: " + paymentStatus);
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Remarks: " + (remarks.isEmpty() ? "None" : remarks));
        System.out.println("════════════════════════════════════════════════\n");
    }

    /**
     * Get payment summary
     * Demonstrates: Summary generation
     */
    public String getPaymentSummary() {
        return "Payment ID: " + paymentId +
                ", Appointment: " + appointmentId +
                ", Amount: RM " + String.format("%.2f", amount) +
                ", Status: " + paymentStatus;
    }

    /**
     * Calculate total with tax
     * Demonstrates: Calculation logic
     *
     * @param taxPercent Tax percentage to apply
     * @return total amount including tax
     */
    public double calculateTotalWithTax(double taxPercent) {
        if (taxPercent < 0 || taxPercent > 100) {
            System.out.println("Error: Tax percentage must be between 0 and 100");
            return amount;
        }

        double tax = (amount * taxPercent) / 100;
        return amount + tax;
    }

    /**
     * Calculate change
     * Demonstrates: Cash handling calculation
     *
     * @param amountGiven Amount given by customer
     * @return change amount
     */
    public double calculateChange(double amountGiven) {
        if (amountGiven < amount) {
            System.out.println("Error: Amount given is less than payment amount");
            return 0;
        }

        return amountGiven - amount;
    }

    // ============== UTILITY METHODS ==============

    /**
     * Convert payment object to string format for file storage
     * Format: paymentId|appointmentId|amount|paymentDate|paymentMethod|paymentStatus
     */
    @Override
    public String toString() {
        return paymentId + "|" + appointmentId + "|" + amount + "|" + paymentDate + "|" + paymentMethod + "|" + paymentStatus;
    }

    /**
     * Detailed string representation for display
     */
    public String toDetailedString() {
        return String.format("Payment{id='%s', appointment='%s', amount=%.2f, method='%s', status='%s'}",
                paymentId, appointmentId, amount, paymentMethod, paymentStatus);
    }
}
