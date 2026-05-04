package com.asu.util;

import com.asu.data.FileHandler;
import com.asu.model.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BusinessLogic — centralised domain rules for the APU-ASC system.
 *
 * Responsibilities:
 *   - Service management   (create / read / update price / delete)
 *   - Appointment validation (slot availability, data correctness)
 *   - Payment calculation   (price lookup, tax, change, membership discount)
 *   - Payment processing    (write record to file)
 *   - Sequential ID generation for all entity types
 *
 * Demonstrates: Single Responsibility, Open/Closed, Dependency Inversion
 * principles; Utility class (static methods, no instantiation).
 */
public class BusinessLogic {

    // 6 % SST applied on top of service price
    private static final double TAX_RATE = 0.06;

    private static final String SERVICES_FILE     = "data/services.txt";
    private static final String APPOINTMENTS_FILE = "data/appointments.txt";
    private static final String PAYMENTS_FILE     = "data/payments.txt";
    private static final String USERS_FILE        = "data/users.txt";
    private static final String FEEDBACK_FILE     = "data/feedback.txt";

    private BusinessLogic() {}

    // =========================================================
    // SERVICE MANAGEMENT  (Create / Read / Update / Delete)
    // =========================================================

    /**
     * Creates a new service record and appends it to services.txt.
     *
     * @return true if the service was added, false if it already exists
     *         or if any argument fails validation.
     */
    public static boolean createService(String serviceId, String name,
                                        String type, double price, int duration) {
        if (!ValidationUtils.isValidServiceType(type)) return false;
        if (!ValidationUtils.isValidPrice(price))      return false;
        if (duration <= 0)                              return false;
        if (ValidationUtils.isNullOrEmpty(serviceId))  return false;

        // Duplicate check
        for (String line : FileHandler.readFile(SERVICES_FILE)) {
            if (line.startsWith(serviceId + "|")) return false;
        }

        String record = serviceId + "|" + name + "|" + type + "|"
                + String.format("%.2f", price) + "|" + duration;
        FileHandler.writeToFile(SERVICES_FILE, record);
        return true;
    }

    /** Returns the Service object for the given ID, or null if not found. */
    public static Service getService(String serviceId) {
        for (String line : FileHandler.readFile(SERVICES_FILE)) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 5 && p[0].equals(serviceId)) {
                try {
                    return new Service(p[0], p[1], p[2],
                            Double.parseDouble(p[3]), Integer.parseInt(p[4]));
                } catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }

    /** Returns all services stored in services.txt. */
    public static List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        for (String line : FileHandler.readFile(SERVICES_FILE)) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 5) {
                try {
                    list.add(new Service(p[0], p[1], p[2],
                            Double.parseDouble(p[3]), Integer.parseInt(p[4])));
                } catch (NumberFormatException ignored) {}
            }
        }
        return list;
    }

    /**
     * Updates the price of all records whose service type matches
     * the given type (case-insensitive: "Normal" or "Major").
     *
     * @return true if at least one record was updated.
     */
    public static boolean updateServicePrice(String serviceType, double newPrice) {
        if (!ValidationUtils.isValidPrice(newPrice)) return false;

        List<String> lines   = FileHandler.readFile(SERVICES_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 5 && p[2].equalsIgnoreCase(serviceType)) {
                p[3] = String.format("%.2f", newPrice);
                updated.add(String.join("|", p));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) FileHandler.overwriteFile(SERVICES_FILE, updated);
        return found;
    }

    /**
     * Removes the service record with the given ID from services.txt.
     *
     * @return true if the record existed and was removed.
     */
    public static boolean deleteService(String serviceId) {
        List<String> lines   = FileHandler.readFile(SERVICES_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (p.length > 0 && p[0].equals(serviceId)) {
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) FileHandler.overwriteFile(SERVICES_FILE, updated);
        return found;
    }

    // =========================================================
    // APPOINTMENT VALIDATION
    // =========================================================

    /**
     * Validates all appointment data fields before creating a record.
     * Checks: customer ID format, vehicle format, service type,
     * date format, date is not in the past, time format.
     */
    public static boolean isValidAppointmentData(String customerId, String vehicleNo,
                                                  String serviceType, String date, String time) {
        return ValidationUtils.isValidCustomerId(customerId)
                && ValidationUtils.isValidVehicleNo(vehicleNo)
                && ValidationUtils.isValidServiceType(serviceType)
                && ValidationUtils.isValidDate(date)
                && ValidationUtils.isFutureOrTodayDate(date)
                && ValidationUtils.isValidTime(time);
    }

    /**
     * Returns true if the given technician has no active (non-cancelled,
     * non-completed) appointment at the same date and time.
     *
     * File format assumed: appointmentId|date|time|status|vehicleNo|issue|technicianId
     */
    public static boolean isTechnicianAvailable(String technicianId, String date, String time) {
        for (String line : FileHandler.readFile(APPOINTMENTS_FILE)) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 7
                    && p[6].equals(technicianId)
                    && p[1].equals(date)
                    && p[2].equals(time)) {
                String status = p[3];
                if (!status.equalsIgnoreCase("Completed")
                        && !status.equalsIgnoreCase("Cancelled")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns String arrays from users.txt for technicians who are
     * not booked at the given date and time slot.
     *
     * Each array: {userId, name, phone, username, password, "Technician"}
     */
    public static List<String[]> getAvailableTechnicians(String date, String time) {
        Set<String> booked = new HashSet<>();
        for (String line : FileHandler.readFile(APPOINTMENTS_FILE)) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 7 && p[1].equals(date) && p[2].equals(time)
                    && !p[3].equalsIgnoreCase("Completed")
                    && !p[3].equalsIgnoreCase("Cancelled")) {
                booked.add(p[6]);
            }
        }

        List<String[]> available = new ArrayList<>();
        for (String line : FileHandler.readFile(USERS_FILE)) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 6 && "Technician".equals(p[5]) && !booked.contains(p[0])) {
                available.add(p);
            }
        }
        return available;
    }

    // =========================================================
    // PAYMENT CALCULATION
    // =========================================================

    /**
     * Looks up the current price of a service type from services.txt.
     * Falls back to hardcoded defaults (RM 80 / RM 200) if file is unavailable.
     *
     * @param serviceType "Normal", "Major", "Normal Service", or "Major Service"
     */
    public static double getServicePrice(String serviceType) {
        for (String line : FileHandler.readFile(SERVICES_FILE)) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 4 && matchesServiceType(p[2], serviceType)) {
                try { return Double.parseDouble(p[3]); }
                catch (NumberFormatException ignored) {}
            }
        }
        // Hardcoded fallback
        return (serviceType != null && serviceType.toLowerCase().contains("major"))
                ? 200.00 : 80.00;
    }

    /** Returns the 6 % SST amount for the given subtotal. */
    public static double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    /** Returns subtotal + 6 % SST. */
    public static double calculateTotal(double subtotal) {
        return subtotal + calculateTax(subtotal);
    }

    /**
     * Returns the change due (amountPaid − totalDue).
     * Returns 0.0 if amountPaid is less than totalDue.
     */
    public static double calculateChange(double amountPaid, double totalDue) {
        double change = amountPaid - totalDue;
        return change < 0 ? 0.0 : change;
    }

    /**
     * Applies a membership tier discount to the subtotal before tax:
     *   Gold   → 15 % off
     *   Silver → 10 % off
     *   Bronze →  5 % off
     *   Regular → no discount
     */
    public static double applyMembershipDiscount(double subtotal, String membershipStatus) {
        if (membershipStatus == null) return subtotal;
        switch (membershipStatus) {
            case "Gold":   return subtotal * 0.85;
            case "Silver": return subtotal * 0.90;
            case "Bronze": return subtotal * 0.95;
            default:       return subtotal;
        }
    }

    // =========================================================
    // PAYMENT PROCESSING
    // =========================================================

    /**
     * Validates and writes a completed payment record to payments.txt.
     *
     * Record format: paymentId|appointmentId|amount|paymentDate|paymentMethod|Paid
     *
     * @return true if the record was written successfully.
     */
    public static boolean processPayment(String paymentId, String appointmentId,
                                          double amount, String method, String date) {
        if (!ValidationUtils.isValidPaymentAmount(amount))  return false;
        if (!ValidationUtils.isValidPaymentMethod(method))  return false;
        if (ValidationUtils.isNullOrEmpty(appointmentId))   return false;

        String record = paymentId + "|" + appointmentId + "|"
                + String.format("%.2f", amount) + "|" + date + "|" + method + "|Paid";
        FileHandler.writeToFile(PAYMENTS_FILE, record);
        return true;
    }

    // =========================================================
    // SEQUENTIAL ID GENERATION
    // =========================================================

    /** Generates the next APT-NNN appointment ID. */
    public static String generateNextAppointmentId() {
        return nextId(APPOINTMENTS_FILE, "APT-");
    }

    /** Generates the next PAY-NNN payment ID. */
    public static String generateNextPaymentId() {
        return nextId(PAYMENTS_FILE, "PAY-");
    }

    /**
     * Generates the next feedback ID.
     *
     * @param isCustomerComment true → CMT- prefix, false → FBK- prefix
     */
    public static String generateNextFeedbackId(boolean isCustomerComment) {
        String prefix = isCustomerComment ? "CMT-" : "FBK-";
        return nextId(FEEDBACK_FILE, prefix);
    }

    /**
     * Generates the next user ID for the given role.
     *
     * @param rolePrefix "MGR-", "STF-", "TEC-", or "CUS-"
     */
    public static String generateNextUserId(String rolePrefix) {
        return nextId(USERS_FILE, rolePrefix);
    }

    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    /** Scans a file for IDs starting with prefix and returns the next sequential one. */
    private static String nextId(String filePath, String prefix) {
        int max = 0;
        for (String line : FileHandler.readFile(filePath)) {
            String[] p = line.trim().split("\\|");
            if (p.length > 0 && p[0].startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(p[0].substring(prefix.length()));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    /**
     * Returns true when the file-stored service type (e.g. "Normal") matches
     * the user-supplied form (e.g. "Normal Service").
     */
    private static boolean matchesServiceType(String stored, String input) {
        if (input == null) return false;
        String lower = input.toLowerCase().trim();
        return (stored.equalsIgnoreCase("Normal")
                    && (lower.equals("normal") || lower.equals("normal service")))
                || (stored.equalsIgnoreCase("Major")
                    && (lower.equals("major") || lower.equals("major service")));
    }
}
