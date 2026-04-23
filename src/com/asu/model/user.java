package com.asu.model;

import java.io.*;
import java.util.*;

/**
 * Abstract User class - Base class for all user types in the system
 * Demonstrates: Abstraction, Encapsulation, Inheritance
 *
 * This class serves as the foundation for all user types (Manager, CounterStaff, Technician, Customer)
 * and implements core authentication and profile management functionality.
 */
public abstract class user implements Serializable {

    // Private attributes - demonstrates Encapsulation
    private String userId;
    private String name;
    private String phone;
    private String username;
    private String password;
    private String userType;
    private boolean isLoggedIn;

    /**
     * Constructor - Initialize user with required credentials
     * @param userId Unique identifier for the user
     * @param name Full name of the user
     * @param phone Contact number
     * @param username Username for login
     * @param password Password for login
     * @param userType Type of user (Manager, CounterStaff, Technician, Customer)
     */
    public user(String userId, String name, String phone, String username, String password, String userType) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.isLoggedIn = false;
    }

    // ============== GETTER METHODS ==============
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // ============== SETTER METHODS ==============
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    public void setPhone(String phone) {
        if (phone != null && !phone.isEmpty()) {
            this.phone = phone;
        }
    }

    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 6) {
            this.password = password;
        }
    }

    // ============== AUTHENTICATION METHODS ==============

    /**
     * Login method - Validates user credentials
     * Demonstrates: Method implementation with validation
     *
     * @param username Username to validate
     * @param password Password to validate
     * @return true if credentials match, false otherwise
     */
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            System.out.println("Username and password cannot be null");
            return false;
        }

        if (this.username.equals(username) && this.password.equals(password)) {
            this.isLoggedIn = true;
            System.out.println("Login successful for user: " + this.name);
            return true;
        } else {
            System.out.println("Invalid username or password");
            return false;
        }
    }

    /**
     *
     * @return true if logout successful
     */
    public boolean logout() {
        if (isLoggedIn) {
            this.isLoggedIn = false;
            System.out.println("User " + this.name + " logged out successfully");
            return true;
        }
        System.out.println("User is not logged in");
        return false;
    }

    // ============== ABSTRACT METHODS ==============
    // Demonstrates: Abstraction - Each user type must implement these methods

    /**
     * Abstract method to display user dashboard
     * Must be implemented by all subclasses
     */
    public abstract void displayDashboard();

    /**
     * Abstract method to edit personal profile
     * Demonstrates: Polymorphism - Different implementations for different user types
     */
    public abstract void editProfile();

    /**
     * Abstract method to perform role-specific actions
     */
    public abstract String getUserRole();

    // ============== UTILITY METHODS ==============

    /**
     * Convert user object to string format for file storage
     * Format: userId|name|phone|username|password|userType
     */
    @Override
    public String toString() {
        return userId + "|" + name + "|" + phone + "|" + username + "|" + password + "|" + userType;
    }

    /**
     * Display user information
     */
    public void displayUserInfo() {
        System.out.println("\n========== USER INFORMATION ==========");
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Username: " + username);
        System.out.println("User Type: " + userType);
        System.out.println("Status: " + (isLoggedIn ? "Logged In" : "Logged Out"));
        System.out.println("=====================================\n");
    }
}

