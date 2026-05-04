package com.asu.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * ValidationUtils — centralised input validation for the APU-ASC system.
 * Demonstrates: Utility class pattern, Single Responsibility Principle.
 * All methods are static; instantiation is blocked.
 */
public class ValidationUtils {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private ValidationUtils() {}

    // =========================================================
    // NULL / EMPTY
    // =========================================================

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // =========================================================
    // NAME
    // =========================================================

    /** Full name: 2-100 printable characters. */
    public static boolean isValidName(String name) {
        if (isNullOrEmpty(name)) return false;
        int len = name.trim().length();
        return len >= 2 && len <= 100;
    }

    // =========================================================
    // PHONE  — Malaysian format: 01X-XXXXXXX or 01X-XXXXXXXX
    // =========================================================

    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return phone.matches("01[0-9]-[0-9]{7,8}");
    }

    // =========================================================
    // USERNAME  — 4-20 alphanumeric chars (no spaces/symbols)
    // =========================================================

    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username)) return false;
        return username.matches("[a-zA-Z0-9]{4,20}");
    }

    // =========================================================
    // PASSWORD  — minimum 6 characters
    // =========================================================

    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) return false;
        return password.length() >= 6;
    }

    // =========================================================
    // EMAIL
    // =========================================================

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");
    }

    // =========================================================
    // DATE  — format DD-MM-YYYY
    // =========================================================

    public static boolean isValidDate(String date) {
        if (isNullOrEmpty(date)) return false;
        try {
            LocalDate.parse(date, DATE_FMT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /** Returns true if the date is today or in the future. */
    public static boolean isFutureOrTodayDate(String date) {
        if (!isValidDate(date)) return false;
        return !LocalDate.parse(date, DATE_FMT).isBefore(LocalDate.now());
    }

    // =========================================================
    // TIME  — format HH:MM  (24-hour clock)
    // =========================================================

    public static boolean isValidTime(String time) {
        if (isNullOrEmpty(time)) return false;
        return time.matches("([01][0-9]|2[0-3]):[0-5][0-9]");
    }

    /** Business hours window: 08:00 – 18:00. */
    public static boolean isBusinessHour(String time) {
        if (!isValidTime(time)) return false;
        String[] parts = time.split(":");
        int totalMinutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        return totalMinutes >= 8 * 60 && totalMinutes <= 18 * 60;
    }

    // =========================================================
    // PAYMENT
    // =========================================================

    /** Amount must be positive and at most RM 99,999.99. */
    public static boolean isValidPaymentAmount(double amount) {
        return amount > 0 && amount <= 99_999.99;
    }

    /** Accepted methods: Cash, Card, Cheque, Online Transfer. */
    public static boolean isValidPaymentMethod(String method) {
        if (isNullOrEmpty(method)) return false;
        return method.equals("Cash") || method.equals("Card")
                || method.equals("Cheque") || method.equals("Online Transfer");
    }

    // =========================================================
    // SERVICE
    // =========================================================

    /**
     * Accepts both short ("Normal"/"Major") and display forms
     * ("Normal Service"/"Major Service").
     */
    public static boolean isValidServiceType(String serviceType) {
        if (isNullOrEmpty(serviceType)) return false;
        String s = serviceType.toLowerCase().trim();
        return s.equals("normal") || s.equals("major")
                || s.equals("normal service") || s.equals("major service");
    }

    /** Price must be positive and at most RM 99,999.99. */
    public static boolean isValidPrice(double price) {
        return price > 0 && price <= 99_999.99;
    }

    // =========================================================
    // RATING  — integer 1 to 5 inclusive
    // =========================================================

    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

    // =========================================================
    // COMMENT / FEEDBACK  — 10 to 500 characters
    // =========================================================

    public static boolean isValidComment(String comment) {
        if (isNullOrEmpty(comment)) return false;
        int len = comment.trim().length();
        return len >= 10 && len <= 500;
    }

    // =========================================================
    // ID FORMAT VALIDATORS
    // =========================================================

    public static boolean isValidManagerId(String id) {
        return id != null && id.matches("MGR-\\d{3,}");
    }

    public static boolean isValidStaffId(String id) {
        return id != null && id.matches("STF-\\d{3,}");
    }

    public static boolean isValidTechnicianId(String id) {
        return id != null && id.matches("TEC-\\d{3,}");
    }

    public static boolean isValidCustomerId(String id) {
        return id != null && id.matches("CUS-\\d{3,}");
    }

    /** Returns true if id matches any valid user role prefix. */
    public static boolean isValidUserId(String id) {
        return isValidManagerId(id) || isValidStaffId(id)
                || isValidTechnicianId(id) || isValidCustomerId(id);
    }

    public static boolean isValidAppointmentId(String id) {
        return id != null && id.matches("APT-\\d{3,}");
    }

    public static boolean isValidPaymentId(String id) {
        return id != null && id.matches("PAY-\\d{3,}");
    }

    /** Accepts both FBK- (technician) and CMT- (customer) prefixes. */
    public static boolean isValidFeedbackId(String id) {
        return id != null && (id.matches("FBK-\\d{3,}") || id.matches("CMT-\\d{3,}"));
    }

    // =========================================================
    // VEHICLE NUMBER  — Malaysian format e.g. WXX 1234 / ABC 5678
    // =========================================================

    public static boolean isValidVehicleNo(String vehicleNo) {
        if (isNullOrEmpty(vehicleNo)) return false;
        return vehicleNo.trim().matches("[A-Z]{1,3}\\s?[0-9]{1,4}[A-Z]?");
    }

    // =========================================================
    // NUMERIC HELPER
    // =========================================================

    /** Returns true if the string parses to a positive double. */
    public static boolean isPositiveNumber(String value) {
        if (isNullOrEmpty(value)) return false;
        try {
            return Double.parseDouble(value.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
