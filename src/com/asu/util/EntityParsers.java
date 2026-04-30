package com.asu.data;
import com.asu.model.*
public class EntityParsers {

    // ================= USER =================
    public static String userToString(User u) {
        return u.getUserId() + "|" +
               u.getName() + "|" +
               u.getPhone() + "|" +
               u.getUsername() + "|" +
               u.getPassword() + "|" +
               u.getUserType();
    }

    public static User stringToUser(String line) {
        String[] p = line.split("\\|");
        return new User(p[0], p[1], p[2], p[3], p[4], p[5]);
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
        String[] p = line.split("\\|");
        return new Appointment(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
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
        String[] p = line.split("\\|");
        return new Service(p[0], p[1], p[2], Double.parseDouble(p[3]), p[4]);
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