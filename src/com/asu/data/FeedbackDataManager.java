package com.asu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedbackDataManager — Singleton managing CRUD on data/feedback.txt.
 *
 * File format (pipe-delimited):
 *   feedbackId | appointmentId | comment | feedbackDate | givenById
 *
 * feedbackId prefix convention:
 *   FBK-NNN  → technician feedback on an appointment
 *   CMT-NNN  → customer comment on an appointment
 *
 * Demonstrates: Singleton pattern, DataPersistence interface, Encapsulation.
 */
public class FeedbackDataManager implements DataPersistence<String[]> {

    private static final String FEEDBACK_FILE = "data/feedback.txt";
    private static FeedbackDataManager instance;

    private FeedbackDataManager() {}

    public static FeedbackDataManager getInstance() {
        if (instance == null) {
            instance = new FeedbackDataManager();
        }
        return instance;
    }

    // =========================================================
    // DataPersistence<String[]> implementation
    // =========================================================

    /**
     * Appends a new feedback record to feedback.txt.
     * Expected: {feedbackId, appointmentId, comment, feedbackDate, givenById}
     */
    @Override
    public void create(String[] feedback) {
        FileHandler.writeToFile(FEEDBACK_FILE, String.join("|", feedback));
    }

    /** Returns all feedback records with at least 5 fields. */
    @Override
    public List<String[]> readAll() {
        List<String[]> list = new ArrayList<>();
        for (String line : FileHandler.readFile(FEEDBACK_FILE)) {
            String[] parts = line.trim().split("\\|");
            if (parts.length >= 5) list.add(parts);
        }
        return list;
    }

    /** Replaces the record whose feedbackId (index 0) matches feedback[0]. */
    @Override
    public void update(String[] feedback) {
        List<String> lines   = FileHandler.readFile(FEEDBACK_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            updated.add((p.length >= 1 && p[0].equals(feedback[0]))
                    ? String.join("|", feedback) : line);
        }
        FileHandler.overwriteFile(FEEDBACK_FILE, updated);
    }

    /** Removes the record with the given feedbackId from feedback.txt. */
    @Override
    public void delete(String feedbackId) {
        List<String> lines   = FileHandler.readFile(FEEDBACK_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (!(p.length >= 1 && p[0].equals(feedbackId))) updated.add(line);
        }
        FileHandler.overwriteFile(FEEDBACK_FILE, updated);
    }

    // =========================================================
    // Domain-specific query methods
    // =========================================================

    /** Returns all feedback records linked to the given appointmentId (index 1). */
    public List<String[]> getByAppointmentId(String appointmentId) {
        List<String[]> result = new ArrayList<>();
        for (String[] f : readAll()) {
            if (f.length >= 2 && f[1].equals(appointmentId)) result.add(f);
        }
        return result;
    }

    /**
     * Returns all feedback records submitted by the given user ID (index 4).
     * Works for both technicians (FBK-) and customers (CMT-).
     */
    public List<String[]> getByGiverId(String giverId) {
        List<String[]> result = new ArrayList<>();
        for (String[] f : readAll()) {
            if (f.length >= 5 && f[4].equals(giverId)) result.add(f);
        }
        return result;
    }

    /** Returns only technician feedback records (ID starts with "FBK-"). */
    public List<String[]> getTechnicianFeedbacks() {
        List<String[]> result = new ArrayList<>();
        for (String[] f : readAll()) {
            if (f.length >= 1 && f[0].startsWith("FBK-")) result.add(f);
        }
        return result;
    }

    /** Returns only customer comment records (ID starts with "CMT-"). */
    public List<String[]> getCustomerComments() {
        List<String[]> result = new ArrayList<>();
        for (String[] f : readAll()) {
            if (f.length >= 1 && f[0].startsWith("CMT-")) result.add(f);
        }
        return result;
    }

    /** Returns the total number of feedback records in the file. */
    public int getTotalCount() {
        return readAll().size();
    }
}
