package com.asu.data;

import com.asu.util.ValidationUtils;

/**
 * DataValidator — validates pipe-delimited record arrays before persistence.
 *
 * Each validate* method returns null on success, or a human-readable
 * error message string describing the first validation failure found.
 *
 * Demonstrates: Single Responsibility Principle, Utility class pattern,
 * Dependency on ValidationUtils (Dependency Inversion).
 */
public class DataValidator {

    private DataValidator() {}

    // =========================================================
    // USER RECORD
    // Format: userId | name | phone | username | password | userType
    // =========================================================

    /**
     * Validates a user record array (6 fields).
     *
     * @return null if valid, or an error message.
     */
    public static String validateUserRecord(String[] record) {
        if (record == null || record.length < 6)
            return "User record must have 6 fields (userId|name|phone|username|password|userType).";
        if (ValidationUtils.isNullOrEmpty(record[0]))
            return "User ID cannot be empty.";
        if (!ValidationUtils.isValidName(record[1]))
            return "Name must be 2-100 characters.";
        if (!ValidationUtils.isValidPhone(record[2]))
            return "Phone must follow Malaysian format (e.g. 012-3456789).";
        if (!ValidationUtils.isValidUsername(record[3]))
            return "Username must be 4-20 alphanumeric characters (no spaces or symbols).";
        if (!ValidationUtils.isValidPassword(record[4]))
            return "Password must be at least 6 characters.";
        if (ValidationUtils.isNullOrEmpty(record[5]))
            return "User type cannot be empty.";
        return null;
    }

    // =========================================================
    // APPOINTMENT RECORD
    // Format: appointmentId | date | time | status | vehicleNo | issue | technicianId
    // =========================================================

    /**
     * Validates an appointment record array (7 fields).
     *
     * @return null if valid, or an error message.
     */
    public static String validateAppointmentRecord(String[] record) {
        if (record == null || record.length < 7)
            return "Appointment record must have 7 fields.";
        if (!ValidationUtils.isValidAppointmentId(record[0]))
            return "Invalid appointment ID format (expected APT-NNN).";
        if (!ValidationUtils.isValidDate(record[1]))
            return "Invalid appointment date — use DD-MM-YYYY.";
        if (!ValidationUtils.isValidTime(record[2]))
            return "Invalid appointment time — use HH:MM (24-hour).";
        if (ValidationUtils.isNullOrEmpty(record[3]))
            return "Appointment status cannot be empty.";
        if (ValidationUtils.isNullOrEmpty(record[4]))
            return "Vehicle number cannot be empty.";
        if (ValidationUtils.isNullOrEmpty(record[5]))
            return "Issue description cannot be empty.";
        if (ValidationUtils.isNullOrEmpty(record[6]))
            return "Technician ID cannot be empty.";
        return null;
    }

    // =========================================================
    // PAYMENT RECORD
    // Format: paymentId | appointmentId | amount | paymentDate | paymentMethod | paymentStatus
    // =========================================================

    /**
     * Validates a payment record array (6 fields).
     *
     * @return null if valid, or an error message.
     */
    public static String validatePaymentRecord(String[] record) {
        if (record == null || record.length < 6)
            return "Payment record must have 6 fields.";
        if (!ValidationUtils.isValidPaymentId(record[0]))
            return "Invalid payment ID format (expected PAY-NNN).";
        if (ValidationUtils.isNullOrEmpty(record[1]))
            return "Appointment ID cannot be empty.";
        try {
            double amount = Double.parseDouble(record[2]);
            if (!ValidationUtils.isValidPaymentAmount(amount))
                return "Payment amount must be greater than 0 and at most RM 99,999.99.";
        } catch (NumberFormatException e) {
            return "Amount must be a valid number (e.g. 80.00).";
        }
        if (!ValidationUtils.isValidDate(record[3]))
            return "Invalid payment date — use DD-MM-YYYY.";
        if (!ValidationUtils.isValidPaymentMethod(record[4]))
            return "Payment method must be Cash, Card, Cheque, or Online Transfer.";
        if (ValidationUtils.isNullOrEmpty(record[5]))
            return "Payment status cannot be empty.";
        return null;
    }

    // =========================================================
    // SERVICE RECORD
    // Format: serviceId | serviceName | serviceType | price | duration
    // =========================================================

    /**
     * Validates a service record array (5 fields).
     *
     * @return null if valid, or an error message.
     */
    public static String validateServiceRecord(String[] record) {
        if (record == null || record.length < 5)
            return "Service record must have 5 fields.";
        if (ValidationUtils.isNullOrEmpty(record[0]))
            return "Service ID cannot be empty.";
        if (ValidationUtils.isNullOrEmpty(record[1]))
            return "Service name cannot be empty.";
        if (!ValidationUtils.isValidServiceType(record[2]))
            return "Service type must be 'Normal' or 'Major'.";
        try {
            double price = Double.parseDouble(record[3]);
            if (!ValidationUtils.isValidPrice(price))
                return "Service price must be greater than 0 and at most RM 99,999.99.";
        } catch (NumberFormatException e) {
            return "Price must be a valid number (e.g. 80.00).";
        }
        try {
            int duration = Integer.parseInt(record[4]);
            if (duration <= 0)
                return "Service duration must be a positive integer (minutes).";
        } catch (NumberFormatException e) {
            return "Duration must be a whole number (minutes).";
        }
        return null;
    }

    // =========================================================
    // FEEDBACK RECORD
    // Format: feedbackId | appointmentId | comment | feedbackDate | givenById
    // =========================================================

    /**
     * Validates a feedback record array (5 fields).
     *
     * @return null if valid, or an error message.
     */
    public static String validateFeedbackRecord(String[] record) {
        if (record == null || record.length < 5)
            return "Feedback record must have 5 fields.";
        if (!ValidationUtils.isValidFeedbackId(record[0]))
            return "Invalid feedback ID format (expected FBK-NNN or CMT-NNN).";
        if (ValidationUtils.isNullOrEmpty(record[1]))
            return "Appointment ID cannot be empty.";
        if (!ValidationUtils.isValidComment(record[2]))
            return "Comment must be between 10 and 500 characters.";
        if (!ValidationUtils.isValidDate(record[3]))
            return "Invalid feedback date — use DD-MM-YYYY.";
        if (ValidationUtils.isNullOrEmpty(record[4]))
            return "Giver ID cannot be empty.";
        return null;
    }
}
