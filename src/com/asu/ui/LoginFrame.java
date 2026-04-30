package com.asu.ui;

import com.asu.ui.panels.*;
import com.asu.dialogs.RegistrationDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LoginFrame.java
 *
 * The first window the user sees when the application starts.
 * It presents username and password fields, validates them against users.txt,
 * determines the user's role, and then opens the correct role panel.
 *
 * It also provides a "Register" button that opens RegistrationDialog,
 * which is only intended for new Customer self-registration.
 *
 * HOW TO RUN:
 *   This class contains the main() method — it is the application entry point.
 *   Compile all files, then run:  java com.asu.ui.LoginFrame
 *
 * INTEGRATION NOTE:
 *   The credential check currently reads users.txt directly.
 *   In Week 3, replace the readUsersFile() call with:
 *   UserDataManager.getInstance().validateLogin(username, password)
 */
public class LoginFrame extends JFrame {

    // ── Path to data file (relative to where the JVM is launched from) ───────
    private static final String USERS_FILE = "data/users.txt";

    // ── Components ────────────────────────────────────────────────────────────
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JButton        loginButton;
    private JButton        registerButton;

    // ── Constructor ───────────────────────────────────────────────────────────
    public LoginFrame() {
        setTitle("APU Automotive Service Centre — Login");
        setSize(440, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // centre on screen

        buildUI();
        attachListeners();
    }

    // ── UI Construction ───────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.BG);

        // ── Top banner ──────────────────────────────────────────────────────
        JPanel banner = new JPanel();
        banner.setBackground(UIConstants.PRIMARY);
        banner.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("APU Automotive Service Centre");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(Color.WHITE);
        banner.add(title);

        // ── Centre form ─────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIConstants.PANEL_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(24, 32, 24, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 6, 8, 6);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        // Username row
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(UIConstants.makeFormLabel("Username:"), gbc);

        usernameField = new JTextField();
        usernameField.setPreferredSize(UIConstants.FIELD_SIZE);
        usernameField.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(usernameField, gbc);

        // Password row
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(UIConstants.makeFormLabel("Password:"), gbc);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(UIConstants.FIELD_SIZE);
        passwordField.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(passwordField, gbc);

        // Buttons row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(UIConstants.PANEL_BG);

        loginButton    = new JButton("Login");
        registerButton = new JButton("Register");
        UIConstants.styleButton(loginButton,    UIConstants.PRIMARY);
        UIConstants.styleButton(registerButton, UIConstants.SECONDARY);

        btnRow.add(loginButton);
        btnRow.add(registerButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnRow, gbc);

        // ── Wrapper to centre the form card ─────────────────────────────────
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setBackground(UIConstants.BG);
        centre.setBorder(new EmptyBorder(24, 48, 24, 48));
        centre.add(form);

        root.add(banner, BorderLayout.NORTH);
        root.add(centre, BorderLayout.CENTER);
        setContentPane(root);
    }

    // ── Event Listeners ───────────────────────────────────────────────────────

    private void attachListeners() {
        // Allow pressing Enter in the password field to trigger login
        passwordField.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) attemptLogin();
            }
        });

        loginButton.addActionListener(e -> attemptLogin());

        registerButton.addActionListener(e -> {
            RegistrationDialog dialog = new RegistrationDialog(this);
            dialog.setVisible(true);
        });
    }

    // ── Login Logic ───────────────────────────────────────────────────────────

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // ── Input validation ────────────────────────────────────────────────
        if (username.isEmpty() || password.isEmpty()) {
            DialogUtils.showError(this, "Please enter both username and password.");
            return;
        }

        // ── Credential check ────────────────────────────────────────────────
        // INTEGRATION POINT (Week 3):
        // Replace readUsersFile() with:
        //   String[] user = UserDataManager.getInstance().validateLogin(username, password);
        String[] user = validateCredentials(username, password);

        if (user == null) {
            DialogUtils.showError(this, "Invalid username or password. Please try again.");
            passwordField.setText("");
            return;
        }

        // user[0]=userId, user[1]=name, user[2]=phone, user[3]=username,
        // user[4]=password (not needed here), user[5]=userType
        String userType = user[5];

        // ── Launch the correct panel ─────────────────────────────────────────
        setVisible(false);
        launchRolePanel(user);
    }

    /**
     * Reads users.txt and returns the matching user record as a String array,
     * or null if no match is found.
     *
     * File format: userId|name|phone|username|password|userType
     *
     * INTEGRATION POINT (Week 3): Delete this method and use UserDataManager instead.
     */
    private String[] validateCredentials(String username, String password) {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            DialogUtils.showError(this,
                    "users.txt not found at: " + file.getAbsolutePath() +
                    "\nPlease ensure the data folder exists next to your project.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 6) continue;
                // parts[3]=username, parts[4]=password
                if (parts[3].equals(username) && parts[4].equals(password)) {
                    return parts; // match found
                }
            }
        } catch (IOException ex) {
            DialogUtils.showError(this, "Could not read users.txt: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Creates a host JFrame and loads the panel that belongs to the user's role.
     * The Runnable onLogout callback hides the host frame and shows LoginFrame again
     * WITHOUT calling System.exit() — the system must run continuously.
     *
     * INTEGRATION POINT (Week 3): Replace this wrapper JFrame with MainApplicationFrame.
     */
    private void launchRolePanel(String[] user) {
        JFrame host = new JFrame("APU-ASC  |  Welcome, " + user[1] + "  [" + user[5] + "]");
        host.setSize(UIConstants.FRAME_W, UIConstants.FRAME_H);
        host.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        host.setLocationRelativeTo(null);

        // Logout callback: hides host, shows login again — NO System.exit()
        Runnable onLogout = () -> {
            if (DialogUtils.showConfirmation(host, "Are you sure you want to log out?")) {
                host.dispose();
                usernameField.setText("");
                passwordField.setText("");
                setVisible(true);
            }
        };

        // Intercept window-close button to also return to login
        host.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                onLogout.run();
            }
        });

        // Select panel based on userType
        JPanel rolePanel;
        switch (user[5]) {
            case "Manager":      rolePanel = new ManagerPanel(user, onLogout);      break;
            case "CounterStaff": rolePanel = new CounterStaffPanel(user, onLogout); break;
            case "Technician":   rolePanel = new TechnicianPanel(user, onLogout);   break;
            case "Customer":     rolePanel = new CustomerPanel(user, onLogout);     break;
            default:
                DialogUtils.showError(this, "Unknown user type: " + user[5]);
                setVisible(true);
                return;
        }

        host.setContentPane(rolePanel);
        host.setVisible(true);
    }

    // ── Entry Point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        // Ensure all Swing components are created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
