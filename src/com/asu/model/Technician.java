package com.asu.model;

/**
 * Technician Class - Extends User
 * Demonstrates: Inheritance, Polymorphism, Encapsulation
 *
 * The Technician role is responsible for:
 * - Viewing assigned appointments
 * - Updating appointment status to "Completed"
 * - Providing feedback on completed services
 * - Managing individual appointment details
 */
public class Technician extends user {

    // Additional attributes specific to Technician
    private String specialization;
    private int appointmentsCompleted;
    private double averageRating;
    private String certificationLevel;

    /**
     * Constructor - Initialize Technician with details
     * @param userId Unique technician identifier
     * @param name Technician's full name
     * @param phone Contact number
     * @param username Login username
     * @param password Login password
     * @param specialization Area of specialization
     * @param certificationLevel Level of certification
     */
    public Technician(String userId, String name, String phone, String username,
                      String password, String specialization, String certificationLevel) {
        super(userId, name, phone, username, password, "Technician");
        this.specialization = specialization;
        this.certificationLevel = certificationLevel;
        this.appointmentsCompleted = 0;
        this.averageRating = 0.0;
    }

    // ============== GETTER METHODS ==============
    public String getSpecialization() {
        return specialization;
    }

    public int getAppointmentsCompleted() {
        return appointmentsCompleted;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getCertificationLevel() {
        return certificationLevel;
    }

    // ============== SETTER METHODS ==============
    public void setSpecialization(String specialization) {
        if (specialization != null && !specialization.isEmpty()) {
            this.specialization = specialization;
        }
    }

    public void setCertificationLevel(String certificationLevel) {
        if (certificationLevel != null && !certificationLevel.isEmpty()) {
            this.certificationLevel = certificationLevel;
        }
    }

    // ============== APPOINTMENT MANAGEMENT METHODS ==============

    /**
     * View assigned appointments for the technician
     * Demonstrates: Data retrieval and display
     *
     * @return list of assigned appointments
     */
    public String viewAssignedAppointments() {
        StringBuilder appointments = new StringBuilder();
        appointments.append("\n════════════════════════════════════════════════\n");
        appointments.append("   MY ASSIGNED APPOINTMENTS\n");
        appointments.append("════════════════════════════════════════════════\n");
        appointments.append("Technician: " + this.getName() + "\n");
        appointments.append("Specialization: " + this.specialization + "\n");
        appointments.append("Status: " + (this.isLoggedIn() ? "ONLINE" : "OFFLINE") + "\n");
        appointments.append("════════════════════════════════════════════════\n");
        appointments.append("[Retrieving assigned appointments from database...]\n");
        appointments.append("════════════════════════════════════════════════\n");

        System.out.println(appointments.toString());
        return appointments.toString();
    }

    /**
     * View details of a specific appointment
     * Demonstrates: Detailed information retrieval
     *
     * @param appointmentId ID of appointment to view
     * @return appointment details
     */
    public String viewAppointmentDetails(String appointmentId) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return "";
        }

        StringBuilder details = new StringBuilder();
        details.append("\n════════════════════════════════════════════════\n");
        details.append("   APPOINTMENT DETAILS\n");
        details.append("════════════════════════════════════════════════\n");
        details.append("Appointment ID: " + appointmentId + "\n");
        details.append("Technician: " + this.getName() + "\n");
        details.append("Specialization: " + this.specialization + "\n");
        details.append("════════════════════════════════════════════════\n");
        details.append("Customer Information:\n");
        details.append("  [Retrieved from database...]\n");
        details.append("Vehicle Information:\n");
        details.append("  [Retrieved from database...]\n");
        details.append("Service Details:\n");
        details.append("  [Retrieved from database...]\n");
        details.append("════════════════════════════════════════════════\n");

        System.out.println(details.toString());
        return details.toString();
    }

    /**
     * Update appointment status
     * Demonstrates: Status management with validation
     *
     * @param appointmentId ID of appointment to update
     * @param newStatus New status (typically "Completed")
     * @return true if update successful
     */
    public boolean updateAppointmentStatus(String appointmentId, String newStatus) {
        // Validation
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return false;
        }

        if (newStatus == null || newStatus.isEmpty()) {
            System.out.println("Error: New status cannot be empty");
            return false;
        }

        // Validate status
        if (!isValidStatus(newStatus)) {
            System.out.println("Error: Invalid status. Accepted statuses: Pending, In-Progress, Completed, Cancelled");
            return false;
        }

        System.out.println("✓ Appointment status updated successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - New Status: " + newStatus);
        System.out.println("  - Updated by: " + this.getName());
        System.out.println("  - Timestamp: [System Date & Time]");

        // If completed, increment counter
        if (newStatus.equalsIgnoreCase("Completed")) {
            appointmentsCompleted++;
            System.out.println("  - Appointments Completed: " + appointmentsCompleted);
        }

        return true;
    }

    /**
     * Provide feedback on a completed appointment
     * Demonstrates: Complex data entry with validation
     *
     * @param appointmentId ID of appointment
     * @param feedbackComment Detailed feedback comment
     * @param rating Rating given (1-5)
     * @return true if feedback submitted successfully
     */
    public boolean provideFeedback(String appointmentId, String feedbackComment, int rating) {
        // Validation
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return false;
        }

        if (feedbackComment == null || feedbackComment.isEmpty()) {
            System.out.println("Error: Feedback comment cannot be empty");
            return false;
        }

        if (feedbackComment.length() < 10) {
            System.out.println("Error: Feedback must be at least 10 characters long");
            return false;
        }

        if (rating < 1 || rating > 5) {
            System.out.println("Error: Rating must be between 1 and 5");
            return false;
        }

        System.out.println("✓ Feedback submitted successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Submitted by: " + this.getName());
        System.out.println("  - Rating: " + rating + "/5");
        System.out.println("  - Comment: " + feedbackComment);
        System.out.println("  - Timestamp: [System Date & Time]");

        // Update average rating
        updateAverageRating(rating);

        return true;
    }

    /**
     * Check appointment feedback (view customer comments)
     * Demonstrates: Information retrieval
     *
     * @param appointmentId ID of appointment
     * @return feedback from customer
     */
    public String checkAppointmentFeedback(String appointmentId) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return "";
        }

        StringBuilder feedback = new StringBuilder();
        feedback.append("\n════════════════════════════════════════════════\n");
        feedback.append("   CUSTOMER FEEDBACK\n");
        feedback.append("════════════════════════════════════════════════\n");
        feedback.append("Appointment ID: " + appointmentId + "\n");
        feedback.append("════════════════════════════════════════════════\n");
        feedback.append("[Retrieving customer feedback from database...]\n");
        feedback.append("════════════════════════════════════════════════\n");

        System.out.println(feedback.toString());
        return feedback.toString();
    }

    // ============== OVERRIDE ABSTRACT METHODS ==============

    @Override
    public void displayDashboard() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    TECHNICIAN DASHBOARD                ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Welcome, " + this.getName());
        System.out.println("║ Specialization: " + this.specialization);
        System.out.println("║ Certification: " + this.certificationLevel);
        System.out.println("║ Status: " + (this.isLoggedIn() ? "ONLINE" : "OFFLINE"));
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Performance Metrics:                   ║");
        System.out.println("║ Appointments Completed: " + appointmentsCompleted);
        System.out.println("║ Average Rating: " + String.format("%.1f", averageRating) + "/5.0");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Available Functions:                   ║");
        System.out.println("║ 1. View Assigned Appointments          ║");
        System.out.println("║ 2. View Appointment Details            ║");
        System.out.println("║ 3. Update Appointment Status           ║");
        System.out.println("║ 4. Provide Feedback                    ║");
        System.out.println("║ 5. Check Customer Feedback             ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    @Override
    public void editProfile() {
        System.out.println("\n========== EDIT TECHNICIAN PROFILE ==========");
        System.out.println("Current Name: " + this.getName());
        System.out.println("Current Phone: " + this.getPhone());
        System.out.println("Current Specialization: " + this.specialization);
        System.out.println("Current Certification: " + this.certificationLevel);
        System.out.println("Profile editing in progress...");
        System.out.println("============================================\n");
    }

    @Override
    public String getUserRole() {
        return "Technician - " + specialization + " (" + certificationLevel + ")";
    }

    // ============== HELPER METHODS ==============

    /**
     * Validate appointment status
     */
    private boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("Pending") ||
                status.equalsIgnoreCase("In-Progress") ||
                status.equalsIgnoreCase("Completed") ||
                status.equalsIgnoreCase("Cancelled");
    }

    /**
     * Update average rating based on new feedback
     */
    private void updateAverageRating(int newRating) {
        // Calculate new average
        double totalRating = (averageRating * appointmentsCompleted) + newRating;
        averageRating = totalRating / (appointmentsCompleted + 1);
    }

    /**
     * Override toString for file storage
     */
    @Override
    public String toString() {
        return super.toString() + "|" + specialization + "|" + certificationLevel;
    }
}
