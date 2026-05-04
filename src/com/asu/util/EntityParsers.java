package com.asu.util;

import com.asu.model.*;

/**
 * EntityParsers — marshals domain objects to/from the pipe-delimited
 * text format used in the data files.
 *
 * Demonstrates: Utility class pattern, Single Responsibility Principle.
 *
 * Parsing note for user subclasses:
 *   The data/users.txt file stores only the 6 base fields shared by all
 *   user types. Role-specific fields (e.g. Manager's department) are not
 *   persisted to that file and are initialised with "N/A" defaults when
 *   reconstructing objects from the file.
 */
public class EntityParsers {

    private EntityParsers() {}

    // =========================================================
    // USER  —  userId | name | phone | username | password | userType
    // =========================================================

    /** Serialises any user subclass to the 6-field pipe-delimited format. */
    public static String userToString(user u) {
        return u.getUserId() + "|" + u.getName() + "|" + u.getPhone() + "|"
                + u.getUsername() + "|" + u.getPassword() + "|" + u.getUserType();
    }

    /**
     * Deserialises a pipe-delimited user record into the correct subclass.
     * Role-specific fields not stored in users.txt are set to "N/A" / 0.
     *
     * @param line a 6-field pipe-delimited line from users.txt
     * @return the appropriate user subclass, or null if the line is malformed.
     */
    public static user stringToUser(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.trim().split("\\|");
        if (p.length < 6) return null;

        String userId   = p[0];
        String name     = p[1];
        String phone    = p[2];
        String username = p[3];
        String password = p[4];
        String userType = p[5];

        switch (userType) {
            case "Manager":
                return new Manager(userId, name, phone, username, password,
                        "N/A", "N/A");
            case "CounterStaff":
                return new CounterStaff(userId, name, phone, username, password,
                        "N/A", "N/A");
            case "Technician":
                return new Technician(userId, name, phone, username, password,
                        "N/A", "N/A");
            case "Customer":
                return new Customer(userId, name, phone, username, password,
                        "N/A", "N/A", "N/A", "N/A");
            default:
                return null;
        }
    }

    // =========================================================
    // APPOINTMENT  — appointmentId | date | time | status | vehicleNo | issue | technicianId
    // =========================================================

    /** Serialises an Appointment to the 7-field file format. */
    public static String appointmentToString(Appointment a) {
        return a.getAppointmentId() + "|" + a.getAppointmentDate() + "|"
                + a.getAppointmentTime() + "|" + a.getStatus() + "|"
                + a.getVehicleNo() + "|" + a.getIssueDescription() + "|"
                + (a.getTechnicianId() != null ? a.getTechnicianId() : "Unassigned");
    }

    /**
     * Deserialises a pipe-delimited appointment record (7 fields).
     * Fields not stored in this format (customerName, vehicleModel, cost, etc.)
     * are initialised to empty / 0 defaults.
     *
     * @param line a 7-field pipe-delimited line from appointments.txt
     * @return an Appointment object, or null if the line is malformed.
     */
    public static Appointment stringToAppointment(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.trim().split("\\|");
        if (p.length < 7) return null;

        Appointment a = new Appointment(
                p[0],           // appointmentId
                "N/A",          // customerId (not stored in this format)
                "N/A",          // customerName
                p[4],           // vehicleNo
                "N/A",          // vehicleModel
                p[5],           // issueDescription
                p[1],           // appointmentDate
                p[2],           // appointmentTime
                "Normal"        // serviceType default; update if stored separately
        );

        // Restore status (requires reflection workaround — set via updateStatus)
        // Status is index 3; use the Appointment state machine
        if (!p[3].equals("Pending")) {
            // transition to the correct status
            a.updateStatus(p[3]);
        }

        // Assign technician if present
        if (!p[6].equals("Unassigned") && !p[6].isEmpty()) {
            a.assignTechnician(p[6], "N/A");
        }

        return a;
    }

    // =========================================================
    // SERVICE  —  serviceId | serviceName | serviceType | price | duration
    // =========================================================

    /** Serialises a Service to the 5-field file format. */
    public static String serviceToString(Service s) {
        return s.getServiceId() + "|" + s.getServiceName() + "|"
                + s.getServiceType() + "|"
                + String.format("%.2f", s.getPrice()) + "|" + s.getDuration();
    }

    /**
     * Deserialises a pipe-delimited service record (5 fields).
     *
     * @param line a 5-field pipe-delimited line from services.txt
     * @return a Service object, or null if the line is malformed.
     */
    public static Service stringToService(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.trim().split("\\|");
        if (p.length < 5) return null;
        try {
            return new Service(
                    p[0],                          // serviceId
                    p[1],                          // serviceName
                    p[2],                          // serviceType
                    Double.parseDouble(p[3]),      // price
                    Integer.parseInt(p[4])         // duration (minutes)
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // =========================================================
    // PAYMENT  —  paymentId | appointmentId | amount | date | method | status
    // =========================================================

    /** Serialises a Payment to the 6-field file format. */
    public static String paymentToString(Payment p) {
        return p.getPaymentId() + "|" + p.getAppointmentId() + "|"
                + String.format("%.2f", p.getAmount()) + "|" + p.getPaymentDate() + "|"
                + p.getPaymentMethod() + "|" + p.getPaymentStatus();
    }

    /**
     * Deserialises a pipe-delimited payment record (6 fields).
     *
     * @param line a 6-field pipe-delimited line from payments.txt
     * @return a Payment object, or null if the line is malformed.
     */
    public static Payment stringToPayment(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.trim().split("\\|");
        if (p.length < 6) return null;
        try {
            return new Payment(
                    p[0],                          // paymentId
                    p[1],                          // appointmentId
                    Double.parseDouble(p[2]),      // amount
                    p[3],                          // paymentDate
                    p[4]                           // paymentMethod
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
