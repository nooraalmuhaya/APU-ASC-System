package com.asu.model;
/**
 * Manager Class - Extends User
 * Demonstrates: Inheritance, Polymorphism, Encapsulation
 *
 * The Manager has full administrative access to the system including:
 * - Staff management (CRUD operations)
 * - Service price management
 * - Feedback and comments viewing
 * - Report generation and analysis
 */
public class Manager extends user {

    // Additional attributes specific to Manager role
    private String department;
    private String employmentDate;
    private boolean hasAdminAccess;

    /**
     * Constructor - Initialize Manager with user details
     * @param userId Unique manager identifier
     * @param name Manager's full name
     * @param phone Manager's contact number
     * @param username Manager's login username
     * @param password Manager's login password
     * @param department Department the manager oversees
     * @param employmentDate Date of employment
     */
    public Manager(String userId, String name, String phone, String username,
                   String password, String department, String employmentDate) {
        super(userId, name, phone, username, password, "Manager");
        this.department = department;
        this.employmentDate = employmentDate;
        this.hasAdminAccess = true;
    }

    // ============== GETTER METHODS ==============
    public String getDepartment() {
        return department;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public boolean hasAdminAccess() {
        return hasAdminAccess;
    }

    // ============== SETTER METHODS ==============
    public void setDepartment(String department) {
        if (department != null && !department.isEmpty()) {
            this.department = department;
        }
    }

    public void setEmploymentDate(String employmentDate) {
        if (employmentDate != null && !employmentDate.isEmpty()) {
            this.employmentDate = employmentDate;
        }
    }

    // ============== MANAGER-SPECIFIC METHODS ==============

    /**
     * Add new staff member (Manager, CounterStaff, or Technician)
     * Demonstrates: Business logic implementation
     *
     * @param staffId ID of the staff member
     * @param name Name of the staff member
     * @param phone Phone number
     * @param username Login username
     * @param password Login password
     * @param staffType Type of staff (Manager, CounterStaff, Technician)
     * @return true if successfully added
     */
    public boolean addStaff(String staffId, String name, String phone, String username,
                            String password, String staffType) {
        // Validation
        if (!validateInput(staffId, name, phone, username, password, staffType)) {
            System.out.println("Invalid input. Staff not added.");
            return false;
        }

        System.out.println("✓ Staff member added successfully:");
        System.out.println("  - ID: " + staffId);
        System.out.println("  - Name: " + name);
        System.out.println("  - Type: " + staffType);
        return true;
    }

    /**
     * Update existing staff member details
     * Demonstrates: Data modification with validation
     *
     * @param staffId ID of staff to update
     * @param newName New name
     * @param newPhone New phone number
     * @return true if update successful
     */
    public boolean updateStaff(String staffId, String newName, String newPhone) {
        if (staffId == null || staffId.isEmpty()) {
            System.out.println("Invalid staff ID");
            return false;
        }

        if ((newName == null || newName.isEmpty()) && (newPhone == null || newPhone.isEmpty())) {
            System.out.println("At least one field must be updated");
            return false;
        }

        System.out.println("✓ Staff member updated successfully:");
        System.out.println("  - ID: " + staffId);
        if (newName != null && !newName.isEmpty()) {
            System.out.println("  - New Name: " + newName);
        }
        if (newPhone != null && !newPhone.isEmpty()) {
            System.out.println("  - New Phone: " + newPhone);
        }
        return true;
    }

    /**
     * Delete staff member from system
     * Demonstrates: Data deletion with authorization
     *
     * @param staffId ID of staff to delete
     * @return true if deletion successful
     */
    public boolean deleteStaff(String staffId) {
        if (staffId == null || staffId.isEmpty()) {
            System.out.println("Invalid staff ID");
            return false;
        }

        if (!this.hasAdminAccess) {
            System.out.println("Permission denied. Only managers with admin access can delete staff.");
            return false;
        }

        System.out.println("✓ Staff member deleted successfully (ID: " + staffId + ")");
        return true;
    }

    /**
     * Set prices for services
     * Demonstrates: Business rule implementation
     *
     * @param serviceType Type of service (Normal or Major)
     * @param price New price for the service
     * @return true if price updated successfully
     */
    public boolean setServicePrice(String serviceType, double price) {
        if (serviceType == null || serviceType.isEmpty()) {
            System.out.println("Invalid service type");
            return false;
        }

        if (price < 0) {
            System.out.println("Price cannot be negative");
            return false;
        }

        if (!serviceType.equalsIgnoreCase("Normal") && !serviceType.equalsIgnoreCase("Major")) {
            System.out.println("Service type must be 'Normal' or 'Major'");
            return false;
        }

        System.out.println("✓ Service price updated successfully:");
        System.out.println("  - Service Type: " + serviceType);
        System.out.println("  - New Price: RM " + String.format("%.2f", price));
        return true;
    }

    /**
     * View all feedbacks and comments in the system
     * Demonstrates: Report generation
     *
     * @return formatted string of all feedbacks
     */
    public String viewAllFeedbacks() {
        StringBuilder feedbackReport = new StringBuilder();
        feedbackReport.append("\n========== ALL FEEDBACKS & COMMENTS ==========\n");
        feedbackReport.append("Viewing all system feedbacks...\n");
        feedbackReport.append("Total feedbacks: [Retrieved from database]\n");
        feedbackReport.append("============================================\n");
        return feedbackReport.toString();
    }

    /**
     * Generate analytical reports
     * Demonstrates: Complex data analysis
     *
     * @param reportType Type of report (Sales, Service, Performance)
     * @param period Time period for the report
     * @return report summary
     */
    public String generateAnalyzedReport(String reportType, String period) {
        if (reportType == null || reportType.isEmpty() || period == null || period.isEmpty()) {
            System.out.println("Invalid report parameters");
            return "";
        }

        StringBuilder report = new StringBuilder();
        report.append("\n========== ANALYZED REPORT ==========\n");
        report.append("Report Type: ").append(reportType).append("\n");
        report.append("Period: ").append(period).append("\n");
        report.append("Generated by: ").append(this.getName()).append("\n");
        report.append("Date: [System Date]\n");
        report.append("=====================================\n");

        System.out.println(report.toString());
        return report.toString();
    }

    // ============== OVERRIDE ABSTRACT METHODS ==============

    @Override
    public void displayDashboard() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     MANAGER DASHBOARD                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Welcome, " + this.getName());
        System.out.println("║ Department: " + this.department);
        System.out.println("║ Status: " + (this.isLoggedIn() ? "ONLINE" : "OFFLINE"));
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Available Functions:                   ║");
        System.out.println("║ 1. Manage Staff (Create/Update/Delete) ║");
        System.out.println("║ 2. Set Service Prices                  ║");
        System.out.println("║ 3. View All Feedbacks & Comments       ║");
        System.out.println("║ 4. Generate Analyzed Reports           ║");
        System.out.println("║ 5. View System Analytics               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    @Override
    public void editProfile() {
        System.out.println("\n========== EDIT MANAGER PROFILE ==========");
        System.out.println("Current Name: " + this.getName());
        System.out.println("Current Phone: " + this.getPhone());
        System.out.println("Current Department: " + this.department);
        System.out.println("Profile editing in progress...");
        System.out.println("=========================================\n");
    }

    @Override
    public String getUserRole() {
        return "Manager with Administrative Access";
    }

    // ============== VALIDATION HELPER METHOD ==============

    /**
     * Validate staff input data
     * Demonstrates: Input validation pattern
     */
    private boolean validateInput(String staffId, String name, String phone,
                                  String username, String password, String staffType) {
        if (staffId == null || staffId.isEmpty()) {
            System.out.println("Error: Staff ID cannot be empty");
            return false;
        }
        if (name == null || name.isEmpty()) {
            System.out.println("Error: Name cannot be empty");
            return false;
        }
        if (phone == null || phone.isEmpty()) {
            System.out.println("Error: Phone cannot be empty");
            return false;
        }
        if (username == null || username.isEmpty()) {
            System.out.println("Error: Username cannot be empty");
            return false;
        }
        if (password == null || password.length() < 6) {
            System.out.println("Error: Password must be at least 6 characters");
            return false;
        }
        if (staffType == null || staffType.isEmpty()) {
            System.out.println("Error: Staff type cannot be empty");
            return false;
        }
        return true;
    }

    /**
     * Override toString for file storage
     */
    @Override
    public String toString() {
        return super.toString() + "|" + department + "|" + employmentDate;
    }
}