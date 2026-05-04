package com.asu.util;
import com.asu.model.*;
public class EntityParsers {

    // ================= USER =================
    public static String userToString(user u) {
        return u.getUserId() + "|" +
               u.getName() + "|" +
               u.getPhone() + "|" +
               u.getUsername() + "|" +
               u.getPassword() + "|" +
               u.getUserType();
    }

    public static user stringToUser(String line) {
        // user is abstract — caller must create the appropriate subclass.
        // This method is a placeholder; use UserDataManager for real lookups.
        throw new UnsupportedOperationException("Use UserDataManager to parse users.");
    }

    // ================= APPOINTMENT =================
    public static String appointmentToString(Appointment a) {
        return a.getAppointmentId() + "|" +
               a.getAppointmentDate() + "|" +
               a.getAppointmentTime() + "|" +
               a.getStatus() + "|" +
               a.getVehicleNo() + "|" +
               a.getIssueDescription() + "|" +
               a.getTechnicianId();
    }

    public static Appointment stringToAppointment(String line) {
        throw new UnsupportedOperationException("Use AppointmentDataManager to parse appointments.");
    }

    // ================= SERVICE =================
    public static String serviceToString(Service s) {
        return s.getServiceId() + "|" +
               s.getServiceName() + "|" +
               s.getServiceType() + "|" +
               s.getPrice() + "|" +
               s.getDuration();
    }

    public static Service stringToService(String line) {
        throw new UnsupportedOperationException("Use ServiceDataManager to parse services.");
    }

    // ================= PAYMENT =================
    public static String paymentToString(Payment p) {
        return p.getPaymentId() + "|" +
               p.getAppointmentId() + "|" +
               p.getAmount() + "|" +
               p.getPaymentDate() + "|" +
               p.getPaymentMethod() + "|" +
               p.getPaymentStatus();
    }

    public static Payment stringToPayment(String line) {
        String[] p = line.split("\\|");

        Payment payment = new Payment(
                p[0],
                p[1],
                Double.parseDouble(p[2]),
                p[3],
                p[4]
        );

        return payment;
    }
}