package com.asu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * UserDataManager — Singleton that manages CRUD operations on data/users.txt.
 *
 * File format (pipe-delimited):
 *   userId | name | phone | username | password | userType
 *
 * Demonstrates: Singleton pattern, DataPersistence interface, Encapsulation.
 */
public class UserDataManager implements DataPersistence<String[]> {

    private static final String USERS_FILE = "data/users.txt";
    private static UserDataManager instance;

    private UserDataManager() {}

    /** Returns the single shared instance (lazy initialisation, not thread-safe). */
    public static UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    // =========================================================
    // DataPersistence<String[]> implementation
    // =========================================================

    /**
     * Appends a new user record to users.txt.
     * Expected array: {userId, name, phone, username, password, userType}
     */
    @Override
    public void create(String[] user) {
        FileHandler.writeToFile(USERS_FILE, String.join("|", user));
    }

    /** Returns all user records as String arrays. */
    @Override
    public List<String[]> readAll() {
        List<String[]> users = new ArrayList<>();
        for (String line : FileHandler.readFile(USERS_FILE)) {
            String[] parts = line.trim().split("\\|");
            if (parts.length >= 6) users.add(parts);
        }
        return users;
    }

    /**
     * Replaces the record whose userId (index 0) matches user[0].
     * Does nothing if no matching record is found.
     */
    @Override
    public void update(String[] user) {
        List<String> lines   = FileHandler.readFile(USERS_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            updated.add((p.length >= 1 && p[0].equals(user[0]))
                    ? String.join("|", user) : line);
        }
        FileHandler.overwriteFile(USERS_FILE, updated);
    }

    /** Removes the record with the given userId from users.txt. */
    @Override
    public void delete(String userId) {
        List<String> lines   = FileHandler.readFile(USERS_FILE);
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.trim().split("\\|");
            if (!(p.length >= 1 && p[0].equals(userId))) updated.add(line);
        }
        FileHandler.overwriteFile(USERS_FILE, updated);
    }

    // =========================================================
    // Domain-specific query methods
    // =========================================================

    /**
     * Validates login credentials.
     *
     * @return the matching user record array, or null if no match.
     */
    public String[] validateLogin(String username, String password) {
        for (String[] user : readAll()) {
            if (user[3].equals(username) && user[4].equals(password)) return user;
        }
        return null;
    }

    /**
     * Returns all user records whose userType (index 5) equals the given role.
     * Example roles: "Manager", "CounterStaff", "Technician", "Customer".
     */
    public List<String[]> getByRole(String role) {
        List<String[]> result = new ArrayList<>();
        for (String[] user : readAll()) {
            if (user.length >= 6 && user[5].equals(role)) result.add(user);
        }
        return result;
    }

    /** Returns the first record whose userId matches, or null. */
    public String[] getById(String userId) {
        for (String[] user : readAll()) {
            if (user.length >= 1 && user[0].equals(userId)) return user;
        }
        return null;
    }

    /** Returns true if a user with the given username already exists. */
    public boolean usernameExists(String username) {
        for (String[] user : readAll()) {
            if (user.length >= 4 && user[3].equals(username)) return true;
        }
        return false;
    }

    /**
     * Returns all staff records (Manager, CounterStaff, Technician)
     * — excludes Customers.
     */
    public List<String[]> getAllStaff() {
        List<String[]> result = new ArrayList<>();
        for (String[] user : readAll()) {
            if (user.length >= 6 && !user[5].equals("Customer")) result.add(user);
        }
        return result;
    }
}
