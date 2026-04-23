package com.asu.model;

/**
 * Customer Class - Extends User
 * Demonstrates: Inheritance, Polymorphism, Encapsulation
 *
 * The Customer role allows:
 * - Viewing personal service history
 * - Viewing personal payment history
 * - Accessing feedback for their appointments
 * - Providing comments and feedback
 */
public class Customer extends user {

    // Additional attributes specific to Customer
    private String email;
    private String address;
    private String vehicleNo;
    private String vehicleModel;
    private int totalAppointments;
    private double totalAmountSpent;
    private String membershipStatus;

    /**
     * Constructor - Initialize Customer with details
     * @param userId Unique customer identifier
     * @param name Customer's full name
     * @param phone Contact number
     * @param username Login username
     * @param password Login password
     * @param email Email address
     * @param address Residential address
     * @param vehicleNo Vehicle registration number
     * @param vehicleModel Vehicle model/type
     */
    public Customer(String userId, String name, String phone, String username,
                    String password, String email, String address,
                    String vehicleNo, String vehicleModel) {
        super(userId, name, phone, username, password, "Customer");
        this.email = email;
        this.address = address;
        this.vehicleNo = vehicleNo;
        this.vehicleModel = vehicleModel;
        this.totalAppointments = 0;
        this.totalAmountSpent = 0.0;
        this.membershipStatus = "Regular";
    }

    // ============== GETTER METHODS ==============
    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public double getTotalAmountSpent() {
        return totalAmountSpent;
    }

    public String getMembershipStatus() {
        return membershipStatus;
    }

    // ============== SETTER METHODS ==============
    public void setEmail(String email) {
        if (email != null && !email.isEmpty() && email.contains("@")) {
            this.email = email;
        }
    }

    public void setAddress(String address) {
        if (address != null && !address.isEmpty()) {
            this.address = address;
        }
    }

    public void setVehicleNo(String vehicleNo) {
        if (vehicleNo != null && !vehicleNo.isEmpty()) {
            this.vehicleNo = vehicleNo;
        }
    }

    public void setVehicleModel(String vehicleModel) {
        if (vehicleModel != null && !vehicleModel.isEmpty()) {
            this.vehicleModel = vehicleModel;
        }
    }

    // ============== SERVICE HISTORY METHODS ==============

    /**
     * View personal service history
     * Demonstrates: Data retrieval and display
     *
     * @return formatted service history
     */
    public String viewServiceHistory() {
        StringBuilder history = new StringBuilder();
        history.append("\n════════════════════════════════════════════════\n");
        history.append("   MY SERVICE HISTORY\n");
        history.append("════════════════════════════════════════════════\n");
        history.append("Customer: " + this.getName() + "\n");
        history.append("Vehicle: " + this.vehicleModel + " (" + this.vehicleNo + ")\n");
        history.append("Total Appointments: " + this.totalAppointments + "\n");
        history.append("Membership Status: " + this.membershipStatus + "\n");
        history.append("════════════════════════════════════════════════\n");
        history.append("[Retrieving service records from database...]\n");
        history.append("════════════════════════════════════════════════\n");

        System.out.println(history.toString());
        return history.toString();
    }

    /**
     * View detailed information about a specific appointment
     * Demonstrates: Individual record retrieval
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
        details.append("Customer: " + this.getName() + "\n");
        details.append("Vehicle: " + this.vehicleModel + " (" + this.vehicleNo + ")\n");
        details.append("════════════════════════════════════════════════\n");
        details.append("Service Information:\n");
        details.append("  [Retrieved from database...]\n");
        details.append("Technician Information:\n");
        details.append("  [Retrieved from database...]\n");
        details.append("Status: [Retrieved from database...]\n");
        details.append("════════════════════════════════════════════════\n");

        System.out.println(details.toString());
        return details.toString();
    }

    // ============== PAYMENT HISTORY METHODS ==============

    /**
     * View personal payment history
     * Demonstrates: Financial record retrieval
     *
     * @return formatted payment history
     */
    public String viewPaymentHistory() {
        StringBuilder history = new StringBuilder();
        history.append("\n════════════════════════════════════════════════\n");
        history.append("   MY PAYMENT HISTORY\n");
        history.append("════════════════════════════════════════════════\n");
        history.append("Customer: " + this.getName() + "\n");
        history.append("Customer ID: " + this.getUserId() + "\n");
        history.append("Total Amount Spent: RM " + String.format("%.2f", this.totalAmountSpent) + "\n");
        history.append("Total Transactions: " + this.totalAppointments + "\n");
        history.append("════════════════════════════════════════════════\n");
        history.append("[Retrieving payment records from database...]\n");
        history.append("════════════════════════════════════════════════\n");

        System.out.println(history.toString());
        return history.toString();
    }

    /**
     * View receipt for a specific payment
     * Demonstrates: Document retrieval
     *
     * @param appointmentId Associated appointment ID
     * @return receipt details
     */
    public String viewReceipt(String appointmentId) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return "";
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("\n════════════════════════════════════════════════\n");
        receipt.append("   SERVICE RECEIPT\n");
        receipt.append("════════════════════════════════════════════════\n");
        receipt.append("Receipt ID: RCP_" + appointmentId + "\n");
        receipt.append("Customer: " + this.getName() + "\n");
        receipt.append("Appointment ID: " + appointmentId + "\n");
        receipt.append("════════════════════════════════════════════════\n");
        receipt.append("[Retrieving receipt details from database...]\n");
        receipt.append("════════════════════════════════════════════════\n");

        System.out.println(receipt.toString());
        return receipt.toString();
    }

    // ============== FEEDBACK & COMMENTS METHODS ==============

    /**
     * View feedback for a specific appointment
     * Demonstrates: Feedback retrieval
     *
     * @param appointmentId ID of appointment
     * @return feedback from technician
     */
    public String viewAppointmentFeedback(String appointmentId) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return "";
        }

        StringBuilder feedback = new StringBuilder();
        feedback.append("\n════════════════════════════════════════════════\n");
        feedback.append("   TECHNICIAN FEEDBACK\n");
        feedback.append("════════════════════════════════════════════════\n");
        feedback.append("Appointment ID: " + appointmentId + "\n");
        feedback.append("Customer: " + this.getName() + "\n");
        feedback.append("Vehicle: " + this.vehicleModel + " (" + this.vehicleNo + ")\n");
        feedback.append("════════════════════════════════════════════════\n");
        feedback.append("[Retrieving technician feedback from database...]\n");
        feedback.append("════════════════════════════════════════════════\n");

        System.out.println(feedback.toString());
        return feedback.toString();
    }

    /**
     * Provide comment/feedback for counter staff and technician
     * Demonstrates: Data submission with validation
     *
     * @param appointmentId ID of appointment
     * @param comment Comment or feedback text
     * @param rating Rating for the service (1-5)
     * @return true if comment submitted successfully
     */
    public boolean provideComment(String appointmentId, String comment, int rating) {
        // Validation
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return false;
        }

        if (comment == null || comment.isEmpty()) {
            System.out.println("Error: Comment cannot be empty");
            return false;
        }

        if (comment.length() < 10) {
            System.out.println("Error: Comment must be at least 10 characters long");
            return false;
        }

        if (comment.length() > 500) {
            System.out.println("Error: Comment cannot exceed 500 characters");
            return false;
        }

        if (rating < 1 || rating > 5) {
            System.out.println("Error: Rating must be between 1 and 5");
            return false;
        }

        System.out.println("✓ Comment submitted successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Customer: " + this.getName());
        System.out.println("  - Rating: " + rating + "/5");
        System.out.println("  - Comment: " + comment);
        System.out.println("  - Timestamp: [System Date & Time]");

        return true;
    }

    /**
     * View all comments provided by this customer
     * Demonstrates: User history retrieval
     *
     * @return list of comments
     */
    public String viewMyComments() {
        StringBuilder comments = new StringBuilder();
        comments.append("\n════════════════════════════════════════════════\n");
        comments.append("   MY FEEDBACK & COMMENTS\n");
        comments.append("════════════════════════════════════════════════\n");
        comments.append("Customer: " + this.getName() + "\n");
        comments.append("Total Appointments: " + this.totalAppointments + "\n");
        comments.append("════════════════════════════════════════════════\n");
        comments.append("[Retrieving your feedback records...]\n");
        comments.append("════════════════════════════════════════════════\n");

        System.out.println(comments.toString());
        return comments.toString();
    }

    // ============== OVERRIDE ABSTRACT METHODS ==============

    @Override
    public void displayDashboard() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     CUSTOMER DASHBOARD                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Welcome, " + this.getName());
        System.out.println("║ Vehicle: " + this.vehicleModel);
        System.out.println("║ Registration: " + this.vehicleNo);
        System.out.println("║ Status: " + (this.isLoggedIn() ? "ONLINE" : "OFFLINE"));
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Account Statistics:                    ║");
        System.out.println("║ Total Appointments: " + totalAppointments);
        System.out.println("║ Total Spent: RM " + String.format("%.2f", totalAmountSpent));
        System.out.println("║ Membership: " + membershipStatus);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Available Functions:                   ║");
        System.out.println("║ 1. View Service History                ║");
        System.out.println("║ 2. View Payment History                ║");
        System.out.println("║ 3. View Appointment Feedback           ║");
        System.out.println("║ 4. Provide Comments & Ratings          ║");
        System.out.println("║ 5. View My Feedback Records            ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    @Override
    public void editProfile() {
        System.out.println("\n========== EDIT CUSTOMER PROFILE ==========");
        System.out.println("Current Name: " + this.getName());
        System.out.println("Current Phone: " + this.getPhone());
        System.out.println("Current Email: " + this.email);
        System.out.println("Current Address: " + this.address);
        System.out.println("Current Vehicle: " + this.vehicleModel + " (" + this.vehicleNo + ")");
        System.out.println("Profile editing in progress...");
        System.out.println("==========================================\n");
    }

    @Override
    public String getUserRole() {
        return "Customer - " + membershipStatus + " Member";
    }

    /**
     * Update membership status based on spending
     * Demonstrates: Business logic
     */
    public void updateMembershipStatus() {
        if (totalAmountSpent >= 5000) {
            membershipStatus = "Gold";
        } else if (totalAmountSpent >= 2000) {
            membershipStatus = "Silver";
        } else if (totalAmountSpent >= 500) {
            membershipStatus = "Bronze";
        } else {
            membershipStatus = "Regular";
        }
    }

    /**
     * Increment total appointments and amount spent
     * Demonstrates: Data update
     */
    public void recordNewAppointment(double amountSpent) {
        if (amountSpent > 0) {
            this.totalAppointments++;
            this.totalAmountSpent += amountSpent;
            updateMembershipStatus();
        }
    }

    /**
     * Override toString for file storage
     */
    @Override
    public String toString() {
        return super.toString() + "|" + email + "|" + address + "|" + vehicleNo + "|" + vehicleModel;
    }
}
