package com.asu.dialogs;

import com.asu.ui.DialogUtils;
import com.asu.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.UUID;

/**
 * RegistrationDialog.java
 *
 * A modal JDialog that collects details for a new user and appends the record
 * to users.txt in the pipe-delimited format:
 *   userId|name|phone|username|password|userType
 *
 * Who opens this dialog:
 *  - Customers can self-register from the LoginFrame (userType is locked to "Customer")
 *  - The same dialog is reused by Counter Staff (creates customers) and Manager (creates staff)
 *    by passing a different allowedRole parameter.
 *
 * INTEGRATION POINT (Week 3):
 *   Replace the appendToFile() call with:
 *   UserDataManager.getInstance().addUser(newUser);
 */
public class RegistrationDialog extends JDialog {

    private static final String USERS_FILE = "data/users.txt";

    // If non-null, the role combo is locked to this value
    private final String lockedRole;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField     nameField;
    private JTextField     phoneField;
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleCombo;

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Opens the dialog with role locked to "Customer" (self-registration from login). */
    public RegistrationDialog(Frame parent) {
        this(parent, "Customer");
    }

    /**
     * Opens the dialog with role optionally locked.
     * Pass null as lockedRole to allow all roles (for Manager use).
     */
    public RegistrationDialog(Frame parent, String lockedRole) {
        super(parent, "Register New User", true); // true = modal
        this.lockedRole = lockedRole;
        setSize(UIConstants.DIALOG_WIDTH + 30, 420);
        setResizable(false);
        setLocationRelativeTo(parent);
        buildUI();
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.BG);

        // Header
        JPanel header = new JPanel();
        header.setBackground(UIConstants.PRIMARY);
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel title = new JLabel("New User Registration");
        title.setFont(UIConstants.HEADER_FONT);
        title.setForeground(Color.WHITE);
        header.add(title);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PANEL_BG);
        form.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        nameField = new JTextField();
        addRow(form, gbc, 0, "Full Name:", nameField);

        // Phone
        phoneField = new JTextField();
        phoneField.setToolTipText("Format: 01X-XXXXXXX");
        addRow(form, gbc, 1, "Phone:", phoneField);

        // Username
        usernameField = new JTextField();
        usernameField.setToolTipText("4-20 alphanumeric characters");
        addRow(form, gbc, 2, "Username:", usernameField);

        // Password
        passwordField = new JPasswordField();
        addRow(form, gbc, 3, "Password:", passwordField);

        // Confirm Password
        confirmPasswordField = new JPasswordField();
        addRow(form, gbc, 4, "Confirm Password:", confirmPasswordField);

        // Role
        String[] roles = {"Customer", "CounterStaff", "Technician", "Manager"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setFont(UIConstants.LABEL_FONT);
        if (lockedRole != null) {
            roleCombo.setSelectedItem(lockedRole);
            roleCombo.setEnabled(false); // user cannot change the role
        }
        addRow(form, gbc, 5, "User Type:", roleCombo);

        // Buttons
        JButton submitBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Cancel");
        UIConstants.styleButton(submitBtn, UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(cancelBtn, UIConstants.DANGER);

        submitBtn.addActionListener(e -> handleSubmit());
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(submitBtn);
        btnRow.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnRow, gbc);

        root.add(header, BorderLayout.NORTH);
        root.add(form,   BorderLayout.CENTER);
        setContentPane(root);
    }

    /** Adds a label + component pair to the GridBagLayout form. */
    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int row, String labelText, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(UIConstants.makeFormLabel(labelText), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // ── Submission logic ──────────────────────────────────────────────────────

    private void handleSubmit() {
        String name     = nameField.getText().trim();
        String phone    = phoneField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm  = new String(confirmPasswordField.getPassword()).trim();
        String role     = (String) roleCombo.getSelectedItem();

        // ── Validation ─────────────────────────────────────────────────────
        if (name.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
            DialogUtils.showError(this, "All fields are required.");
            return;
        }
        if (!DialogUtils.isValidPhone(phone)) {
            DialogUtils.showError(this,
                    "Invalid phone number.\nExpected format: 01X-XXXXXXX  (e.g. 012-3456789)");
            return;
        }
        if (!DialogUtils.isValidUsername(username)) {
            DialogUtils.showError(this,
                    "Username must be 4–20 alphanumeric characters (no spaces or symbols).");
            return;
        }
        if (!password.equals(confirm)) {
            DialogUtils.showError(this, "Passwords do not match.");
            passwordField.setText("");
            confirmPasswordField.setText("");
            return;
        }
        if (password.length() < 6) {
            DialogUtils.showError(this, "Password must be at least 6 characters.");
            return;
        }
        if (isUsernameTaken(username)) {
            DialogUtils.showError(this,
                    "Username \"" + username + "\" is already taken. Choose another.");
            return;
        }

        // ── Build record ────────────────────────────────────────────────────
        String userId = generateUserId(role);
        String record = String.join("|", userId, name, phone, username, password, role);

        // INTEGRATION POINT (Week 3): Replace appendToFile() with:
        //   UserDataManager.getInstance().addUser(
        //       new User(userId, name, phone, username, password, role));
        if (appendToFile(record)) {
            DialogUtils.showSuccess(this,
                    "Registration successful!\nYou may now log in as: " + username);
            dispose();
        }
    }

    // ── File helpers ──────────────────────────────────────────────────────────

    /** Checks whether the username already exists in users.txt. */
    private boolean isUsernameTaken(String username) {
        File file = new File(USERS_FILE);
        if (!file.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 4 && parts[3].equals(username)) return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    /** Appends the new record as a new line in users.txt. */
    private boolean appendToFile(String record) {
        File file = new File(USERS_FILE);
        file.getParentFile().mkdirs(); // create data/ if it doesn't exist
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.println(record);
            return true;
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not save user: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Generates a simple prefixed ID, e.g. MGR-001, CST-002.
     * INTEGRATION POINT: Replace with UserDataManager's ID generation.
     */
    private String generateUserId(String role) {
        String prefix;
        switch (role) {
            case "Manager":      prefix = "MGR"; break;
            case "CounterStaff": prefix = "STF"; break;
            case "Technician":   prefix = "TEC"; break;
            default:             prefix = "CUS"; break;
        }
        return prefix + "-" + String.format("%03d", (int)(Math.random() * 900) + 100);
    }
}
