package com.asu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentDataManager — Singleton managing CRUD on data/appointments.txt.
 *
 * File format (pipe-delimited):
 *   appointmentId | date | time | status | vehicleNo | issueDescription | technicianId
 *
 * Status values: Pending, Scheduled, In-Progress, Completed, Cancelled
 *
 * Demonstrates: Singleton pattern, DataPersistence interface, Encapsulation.
 */
public class AppointmentDataManager implements DataPersistence<String[]> {

    private static final String APPOINTMENTS_FILE = "data/appointments.txt";
    private static AppointmentDataManager instance;

    private AppointmentDataManager() {}

    public static AppointmentDataManager getInstance() {
        if (instance == null) {
            instance = new AppointmentDataManager();
        }
        return instance;
    }

    // =========================================================
    // DataPersistence<String[]> implementation
    // =========================================================

    /**
     * Appends a new appointment record to appointments.txt.
     * Expected: {appointmentId, date, time, status, vehicleNo, issue, technicianId}
     */
    @Override
    public void create(String[] appointment) {
        FileHandler.writeToFile(APPOINTMENTS_FILE, String.join("|", appointment));
    }

    /** Returns all appointment records with at least 7 fields. */
    @Override
    public List<String[]> readAll() {
        List<String[]> list = new ArrayList<>();
        for (String line : FileHandler.readFile(APPOINTMENTS_FILE)) {
            String[] parts = line.trim().split("\\|");
            if (parts.length >= 7) list.add(parts);
        }
        return list;
    }

    /**
     * Replaces the record whose appointmentId (index 0) matches appointment[0].
     * Does nothing if not found.
     */
    @Override
    public void update(String[] appointment) {
        List<String> lines   = FileHandler.readFile(APPOINTMENTS_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            updated.add((p.length >= 1 && p[0].equals(appointment[0]))
                    ? String.join("|", appointment) : line);
        }
        FileHandler.overwriteFile(APPOINTMENTS_FILE, updated);
    }

    /** Removes the record with the given appointmentId from file. */
    @Override
    public void delete(String appointmentId) {
        List<String> lines   = FileHandler.readFile(APPOINTMENTS_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (!(p.length >= 1 && p[0].equals(appointmentId))) updated.add(line);
        }
        FileHandler.overwriteFile(APPOINTMENTS_FILE, updated);
    }

    // =========================================================
    // Domain-specific query methods
    // =========================================================

    /** Returns the appointment record for the given ID, or null. */
    public String[] getById(String appointmentId) {
        for (String[] a : readAll()) {
            if (a[0].equals(appointmentId)) return a;
        }
        return null;
    }

    /**
     * Returns all appointments assigned to the given technician ID (index 6).
     */
    public List<String[]> getByTechnicianId(String technicianId) {
        List<String[]> result = new ArrayList<>();
        for (String[] a : readAll()) {
            if (a.length >= 7 && a[6].equals(technicianId)) result.add(a);
        }
        return result;
    }

    /** Returns all appointments whose status (index 3) matches the given value. */
    public List<String[]> getByStatus(String status) {
        List<String[]> result = new ArrayList<>();
        for (String[] a : readAll()) {
            if (a.length >= 4 && a[3].equalsIgnoreCase(status)) result.add(a);
        }
        return result;
    }

    /**
     * Updates only the status field (index 3) of an appointment.
     *
     * @return true if the record was found and updated.
     */
    public boolean updateStatus(String appointmentId, String newStatus) {
        List<String> lines   = FileHandler.readFile(APPOINTMENTS_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 4 && p[0].equals(appointmentId)) {
                p[3] = newStatus;
                updated.add(String.join("|", p));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) FileHandler.overwriteFile(APPOINTMENTS_FILE, updated);
        return found;
    }

    /**
     * Returns technician IDs that are already booked at the given date and time
     * (excludes Completed and Cancelled appointments).
     */
    public List<String> getBookedTechnicianIds(String date, String time) {
        List<String> booked = new ArrayList<>();
        for (String[] a : readAll()) {
            if (a.length >= 7 && a[1].equals(date) && a[2].equals(time)
                    && !a[3].equalsIgnoreCase("Completed")
                    && !a[3].equalsIgnoreCase("Cancelled")) {
                booked.add(a[6]);
            }
        }
        return booked;
    }
}
