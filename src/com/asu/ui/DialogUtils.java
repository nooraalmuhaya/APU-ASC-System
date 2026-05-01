package com.asu.ui;

import javax.swing.*;
import java.awt.*;

/**
 * DialogUtils.java
 *
 * A collection of static helper methods for showing standard popups.
 * Instead of writing JOptionPane code repeatedly in every panel, any class
 * can call DialogUtils.showError("...") or DialogUtils.showSuccess("...").
 *
 * This class cannot be instantiated — all methods are static.
 */
public final class DialogUtils {

    private DialogUtils() {}

    /**
     * Shows a red error popup with a custom message.
     * Use for: failed login, validation errors, file not found, etc.
     *
     * @param parent  the component the dialog is centred over (pass null to centre on screen)
     * @param message the error message to display
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Shows a green success popup.
     * Use for: successful save, appointment created, payment processed, etc.
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Shows a Yes/No confirmation dialog.
     * Returns true if the user clicked Yes, false otherwise.
     * Use for: confirm delete, confirm logout, confirm payment.
     */
    public static boolean showConfirmation(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                message,
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Shows a plain information popup (blue icon).
     * Use for: general notices, tips, non-critical info.
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Checks whether a JTextField (or JPasswordField) is empty after trimming.
     * Convenience method used before any form submission.
     */
    public static boolean isEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    /**
     * Validates that a string matches a basic Malaysian phone format (e.g. 01X-XXXXXXX).
     * Returns true if valid.
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^01[0-9]-[0-9]{7,8}$");
    }

    /**
     * Validates that a username contains only alphanumeric characters
     * and is between 4 and 20 characters long.
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{4,20}$");
    }
}
