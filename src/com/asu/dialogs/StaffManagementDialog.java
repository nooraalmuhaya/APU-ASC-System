package com.asu.dialogs;

import com.asu.ui.DialogUtils;
import com.asu.ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StaffManagementDialog.java
 *
 * Used by the Manager to ADD a new staff member or EDIT an existing one.
 * Staff roles are limited to CounterStaff and Technician (Managers create
 * other managers through the same dialog if needed).
 *
 * Two modes, controlled by the constructor:
 *   ADD  — all fields empty, generates a new userId on submit
 *   EDIT — fields pre-filled from the selected staff row
 *
 * File format written/updated: userId|name|phone|username|password|userType
 *
 * INTEGRATION POINT (Week 3):
 *   Replace file operations with UserDataManager.addUser() / updateUser()
 */
public class StaffManagementDialog extends JDialog {

    private static final String USERS_FILE = "data/users.txt";

    public enum Mode { ADD, EDIT }

    private final Mode     mode;
    private final String[] existingData; // null when mode == ADD

    // ── Fields ─────────────────────────────────────────────────────────────
    private JTextField     nameField;
    private JTextField     phoneField;
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    // ── Constructors ────────────────────────────────────────────────────────

    /** ADD mode — blank form. */
    public StaffManagementDialog(Frame parent) {
        this(parent, Mode.ADD, null);
    }

    /**
     * EDIT mode — form pre-filled with existingData.
     * existingData format: {userId, name, phone, username, password, userType}
     */
    public StaffManagementDialog(Frame parent, Mode mode, String[] existingData) {
        super(parent, mode == Mode.ADD ? "Add New Staff" : "Edit Staff", true);
        this.mode         = mode;
        this.existingData = existingData;
        setSize(420, 380);
        setResizable(false);
        setLocationRelativeTo(parent);
        buildUI();
    }

    // ── UI ───────────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.BG);

        // Header
        JPanel header = new JPanel();
        header.setBackground(UIConstants.PRIMARY);
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel title = new JLabel(mode == Mode.ADD ? "Add Staff Member" : "Edit Staff Member");
        title.setFont(UIConstants.HEADER_FONT);
        title.setForeground(Color.WHITE);
        header.add(title);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PANEL_BG);
        form.setBorder(new EmptyBorder(18, 28, 18, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField     = new JTextField();
        phoneField    = new JTextField();
        phoneField.setToolTipText("Format: 01X-XXXXXXX");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        // If EDIT mode, pre-fill all fields
        if (mode == Mode.EDIT && existingData != null) {
            nameField.setText(existingData[1]);
            phoneField.setText(existingData[2]);
            usernameField.setText(existingData[3]);
            // Password shown as blank for security; must be re-entered to change
            usernameField.setEditable(false); // username should not change in edit mode
        }

        addRow(form, gbc, 0, "Full Name:",  nameField);
        addRow(form, gbc, 1, "Phone:",      phoneField);
        addRow(form, gbc, 2, "Username:",   usernameField);
        addRow(form, gbc, 3, "Password:",   passwordField);

        if (mode == Mode.EDIT) {
            JLabel hint = new JLabel("(Leave password blank to keep current)");
            hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            hint.setForeground(Color.GRAY);
            gbc.gridx = 1; gbc.gridy = 4;
            form.add(hint, gbc);
        }

        String[] staffRoles = {"CounterStaff", "Technician"};
        roleCombo = new JComboBox<>(staffRoles);
        roleCombo.setFont(UIConstants.LABEL_FONT);
        if (mode == Mode.EDIT && existingData != null) {
            roleCombo.setSelectedItem(existingData[5]);
        }
        addRow(form, gbc, 5, "Role:", roleCombo);

        // Buttons
        JButton saveBtn   = new JButton(mode == Mode.ADD ? "Add Staff" : "Save Changes");
        JButton cancelBtn = new JButton("Cancel");
        UIConstants.styleButton(saveBtn,   UIConstants.SUCCESS_COLOR);
        UIConstants.styleButton(cancelBtn, UIConstants.DANGER);
        saveBtn.addActionListener(e -> handleSave());
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);
        btnRow.add(saveBtn);
        btnRow.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnRow, gbc);

        root.add(header, BorderLayout.NORTH);
        root.add(form,   BorderLayout.CENTER);
        setContentPane(root);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(UIConstants.makeFormLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // ── Save Logic ────────────────────────────────────────────────────────────

    private void handleSave() {
        String name     = nameField.getText().trim();
        String phone    = phoneField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role     = (String) roleCombo.getSelectedItem();

        // Validation
        if (name.isEmpty() || phone.isEmpty() || username.isEmpty()) {
            DialogUtils.showError(this, "Name, phone, and username are required.");
            return;
        }
        if (!DialogUtils.isValidPhone(phone)) {
            DialogUtils.showError(this,
                    "Invalid phone. Expected format: 01X-XXXXXXX");
            return;
        }
        if (mode == Mode.ADD) {
            if (password.isEmpty()) {
                DialogUtils.showError(this, "Password is required for a new staff member.");
                return;
            }
            if (!DialogUtils.isValidUsername(username)) {
                DialogUtils.showError(this,
                        "Username must be 4–20 alphanumeric characters.");
                return;
            }
            // INTEGRATION POINT: replace with UserDataManager.isUsernameTaken()
            if (isUsernameTaken(username)) {
                DialogUtils.showError(this, "Username already exists. Choose another.");
                return;
            }
            String userId = generateId(role);
            String record = String.join("|", userId, name, phone, username, password, role);
            // INTEGRATION POINT: UserDataManager.getInstance().addUser(...)
            appendToFile(record);
            DialogUtils.showSuccess(this, "Staff member \"" + name + "\" added successfully.");

        } else {
            // EDIT — update the matching line in users.txt
            String userId          = existingData[0];
            String effectivePass   = password.isEmpty() ? existingData[4] : password;
            String updatedRecord   = String.join("|",
                    userId, name, phone, username, effectivePass, role);
            // INTEGRATION POINT: UserDataManager.getInstance().updateUser(...)
            updateInFile(userId, updatedRecord);
            DialogUtils.showSuccess(this, "Staff member \"" + name + "\" updated.");
        }
        dispose();
    }

    // ── File helpers ──────────────────────────────────────────────────────────

    private boolean isUsernameTaken(String username) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                if (p.length >= 4 && p[3].equals(username)) return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    private void appendToFile(String record) {
        File f = new File(USERS_FILE);
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, true))) {
            pw.println(record);
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not write to users.txt: " + ex.getMessage());
        }
    }

    private void updateInFile(String userId, String newRecord) {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.trim().split("\\|");
                // Replace the line whose userId matches
                lines.add(p.length > 0 && p[0].equals(userId) ? newRecord : line);
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not read users.txt: " + ex.getMessage());
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            for (String l : lines) pw.println(l);
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not update users.txt: " + ex.getMessage());
        }
    }

    private String generateId(String role) {
        String prefix;
        switch (role) {
            case "CounterStaff": prefix = "STF"; break;
            case "Technician":   prefix = "TEC"; break;
            case "Manager":      prefix = "MGR"; break;
            default:             prefix = "USR"; break;
        }
        return prefix + "-" + String.format("%03d", (int)(Math.random() * 900) + 100);
    }
}
