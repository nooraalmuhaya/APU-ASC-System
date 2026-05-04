package com.asu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * PaymentDataManager — Singleton managing CRUD on data/payments.txt.
 *
 * File format (pipe-delimited):
 *   paymentId | appointmentId | amount | paymentDate | paymentMethod | paymentStatus
 *
 * paymentMethod values : Cash, Card, Cheque, Online Transfer
 * paymentStatus values : Pending, Paid, Failed, Refunded
 *
 * Demonstrates: Singleton pattern, DataPersistence interface, Encapsulation.
 */
public class PaymentDataManager implements DataPersistence<String[]> {

    private static final String PAYMENTS_FILE = "data/payments.txt";
    private static PaymentDataManager instance;

    private PaymentDataManager() {}

    public static PaymentDataManager getInstance() {
        if (instance == null) {
            instance = new PaymentDataManager();
        }
        return instance;
    }

    // =========================================================
    // DataPersistence<String[]> implementation
    // =========================================================

    /**
     * Appends a new payment record to payments.txt.
     * Expected: {paymentId, appointmentId, amount, paymentDate, paymentMethod, paymentStatus}
     */
    @Override
    public void create(String[] payment) {
        FileHandler.writeToFile(PAYMENTS_FILE, String.join("|", payment));
    }

    /** Returns all payment records with at least 6 fields. */
    @Override
    public List<String[]> readAll() {
        List<String[]> list = new ArrayList<>();
        for (String line : FileHandler.readFile(PAYMENTS_FILE)) {
            String[] parts = line.trim().split("\\|");
            if (parts.length >= 6) list.add(parts);
        }
        return list;
    }

    /** Replaces the record whose paymentId (index 0) matches payment[0]. */
    @Override
    public void update(String[] payment) {
        List<String> lines   = FileHandler.readFile(PAYMENTS_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            updated.add((p.length >= 1 && p[0].equals(payment[0]))
                    ? String.join("|", payment) : line);
        }
        FileHandler.overwriteFile(PAYMENTS_FILE, updated);
    }

    /** Removes the record with the given paymentId from payments.txt. */
    @Override
    public void delete(String paymentId) {
        List<String> lines   = FileHandler.readFile(PAYMENTS_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (!(p.length >= 1 && p[0].equals(paymentId))) updated.add(line);
        }
        FileHandler.overwriteFile(PAYMENTS_FILE, updated);
    }

    // =========================================================
    // Domain-specific query methods
    // =========================================================

    /** Returns the payment record for the given paymentId, or null. */
    public String[] getById(String paymentId) {
        for (String[] p : readAll()) {
            if (p[0].equals(paymentId)) return p;
        }
        return null;
    }

    /**
     * Returns the payment record linked to the given appointmentId (index 1),
     * or null if no payment exists for that appointment.
     */
    public String[] getByAppointmentId(String appointmentId) {
        for (String[] p : readAll()) {
            if (p.length >= 2 && p[1].equals(appointmentId)) return p;
        }
        return null;
    }

    /**
     * Sums the amounts of all records whose status is "Paid".
     *
     * @return total revenue collected.
     */
    public double getTotalRevenue() {
        double total = 0;
        for (String[] p : readAll()) {
            if (p.length >= 6 && p[5].equalsIgnoreCase("Paid")) {
                try { total += Double.parseDouble(p[2]); }
                catch (NumberFormatException ignored) {}
            }
        }
        return total;
    }

    /** Returns all payment records with the given status (e.g. "Paid"). */
    public List<String[]> getByStatus(String status) {
        List<String[]> result = new ArrayList<>();
        for (String[] p : readAll()) {
            if (p.length >= 6 && p[5].equalsIgnoreCase(status)) result.add(p);
        }
        return result;
    }
}
