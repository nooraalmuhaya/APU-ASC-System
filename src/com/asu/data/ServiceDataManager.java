package com.asu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * ServiceDataManager — Singleton managing CRUD on data/services.txt.
 *
 * File format (pipe-delimited):
 *   serviceId | serviceName | serviceType | price | duration(minutes)
 *
 * serviceType values: "Normal" (1 hr = 60 min), "Major" (3 hrs = 180 min)
 *
 * Demonstrates: Singleton pattern, DataPersistence interface, Encapsulation.
 */
public class ServiceDataManager implements DataPersistence<String[]> {

    private static final String SERVICES_FILE = "data/services.txt";
    private static ServiceDataManager instance;

    private ServiceDataManager() {}

    public static ServiceDataManager getInstance() {
        if (instance == null) {
            instance = new ServiceDataManager();
        }
        return instance;
    }

    // =========================================================
    // DataPersistence<String[]> implementation
    // =========================================================

    /**
     * Appends a new service record to services.txt.
     * Expected: {serviceId, serviceName, serviceType, price, duration}
     */
    @Override
    public void create(String[] service) {
        FileHandler.writeToFile(SERVICES_FILE, String.join("|", service));
    }

    /** Returns all service records with at least 5 fields. */
    @Override
    public List<String[]> readAll() {
        List<String[]> list = new ArrayList<>();
        for (String line : FileHandler.readFile(SERVICES_FILE)) {
            String[] parts = line.trim().split("\\|");
            if (parts.length >= 5) list.add(parts);
        }
        return list;
    }

    /** Replaces the record whose serviceId (index 0) matches service[0]. */
    @Override
    public void update(String[] service) {
        List<String> lines   = FileHandler.readFile(SERVICES_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            updated.add((p.length >= 1 && p[0].equals(service[0]))
                    ? String.join("|", service) : line);
        }
        FileHandler.overwriteFile(SERVICES_FILE, updated);
    }

    /** Removes the record with the given serviceId from services.txt. */
    @Override
    public void delete(String serviceId) {
        List<String> lines   = FileHandler.readFile(SERVICES_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (!(p.length >= 1 && p[0].equals(serviceId))) updated.add(line);
        }
        FileHandler.overwriteFile(SERVICES_FILE, updated);
    }

    // =========================================================
    // Domain-specific query methods
    // =========================================================

    /**
     * Returns the price for a given service type.
     * Accepts "Normal", "Major", "Normal Service", "Major Service" (case-insensitive).
     * Falls back to RM 80 (Normal) or RM 200 (Major) if not found.
     */
    public double getPrice(String serviceType) {
        for (String[] s : readAll()) {
            if (s.length >= 4 && typeMatches(s[2], serviceType)) {
                try { return Double.parseDouble(s[3]); }
                catch (NumberFormatException ignored) {}
            }
        }
        return (serviceType != null && serviceType.toLowerCase().contains("major"))
                ? 200.00 : 80.00;
    }

    /**
     * Updates the price field for all records whose serviceType matches.
     *
     * @return true if at least one record was updated.
     */
    public boolean updatePrice(String serviceType, double newPrice) {
        List<String> lines   = FileHandler.readFile(SERVICES_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (p.length >= 5 && typeMatches(p[2], serviceType)) {
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

    /** Returns the first record whose serviceType matches, or null. */
    public String[] getByType(String serviceType) {
        for (String[] s : readAll()) {
            if (s.length >= 3 && typeMatches(s[2], serviceType)) return s;
        }
        return null;
    }

    // =========================================================
    // Private helper
    // =========================================================

    private boolean typeMatches(String stored, String input) {
        if (input == null) return false;
        String lower = input.toLowerCase().trim();
        return (stored.equalsIgnoreCase("Normal")
                    && (lower.equals("normal") || lower.equals("normal service")))
                || (stored.equalsIgnoreCase("Major")
                    && (lower.equals("major") || lower.equals("major service")));
    }
}
