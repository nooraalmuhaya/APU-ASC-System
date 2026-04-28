package com.asu.model;

/**
 * Appointment Class
 * Demonstrates: Encapsulation, Composition, Association
 *
 * The Appointment class handles:
 * - Appointment scheduling and management
 * - Technician assignment
 * - Status tracking throughout appointment lifecycle
 * - Appointment details and queries
 *
 * Association: Each appointment is associated with a customer, technician, and service
 * Composition: Appointments contain feedback and payment records
 */
public class Appointment {

    // Private attributes - Demonstrates Encapsulation
    private String appointmentId;
    private String customerId;
    private String customerName;
    private String vehicleNo;
    private String vehicleModel;
    private String issueDescription;
    private String appointmentDate;
    private String appointmentTime;
    private String serviceType;              // Normal (1hr) or Major (3hrs)
    private String status;                   // Pending, Scheduled, In-Progress, Completed, Cancelled
    private String technicianId;
    private String technicianName;
    private double estimatedCost;
    private double actualCost;
    private String notes;
    private boolean feedbackProvided;

    /**
     * Constructor - Initialize Appointment with basic details
     * @param appointmentId Unique appointment identifier
     * @param customerId ID of the customer
     * @param customerName Name of the customer
     * @param vehicleNo Vehicle registration number
     * @param vehicleModel Vehicle model/type
     * @param issueDescription Description of the issue
     * @param appointmentDate Date of appointment
     * @param appointmentTime Time of appointment
     * @param serviceType Type of service (Normal or Major)
     */
    public Appointment(String appointmentId, String customerId, String customerName,
                       String vehicleNo, String vehicleModel, String issueDescription,
                       String appointmentDate, String appointmentTime, String serviceType) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.vehicleNo = vehicleNo;
        this.vehicleModel = vehicleModel;
        this.issueDescription = issueDescription;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.serviceType = serviceType;
        this.status = "Pending";
        this.notes = "";
        this.feedbackProvided = false;
        this.technicianId = null;
        this.technicianName = null;
    }

    // ============== GETTER METHODS ==============
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getStatus() {
        return status;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public double getActualCost() {
        return actualCost;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isFeedbackProvided() {
        return feedbackProvided;
    }

    // ============== SETTER METHODS ==============
    public void setEstimatedCost(double estimatedCost) {
        if (estimatedCost >= 0) {
            this.estimatedCost = estimatedCost;
        }
    }

    public void setActualCost(double actualCost) {
        if (actualCost >= 0) {
            this.actualCost = actualCost;
        }
    }

    public void setNotes(String notes) {
        if (notes != null) {
            this.notes = notes;
        }
    }

    public void setFeedbackProvided(boolean feedbackProvided) {
        this.feedbackProvided = feedbackProvided;
    }

    // ============== APPOINTMENT MANAGEMENT METHODS ==============

    /**
     * View complete appointment details
     * Demonstrates: Information retrieval and display
     *
     * @return formatted appointment details
     */
    public String viewAppointmentDetails() {
        StringBuilder details = new StringBuilder();

        details.append("\n════════════════════════════════════════════════\n");
        details.append("   APPOINTMENT DETAILS\n");
        details.append("════════════════════════════════════════════════\n");
        details.append("Appointment ID: ").append(appointmentId).append("\n");
        details.append("Status: ").append(status).append("\n");
        details.append("Date: ").append(appointmentDate).append("\n");
        details.append("Time: ").append(appointmentTime).append("\n");
        details.append("Duration: ").append(getServiceDuration()).append("\n\n");

        details.append("CUSTOMER INFORMATION\n");
        details.append("Customer Name: ").append(customerName).append("\n");
        details.append("Customer ID: ").append(customerId).append("\n\n");

        details.append("VEHICLE INFORMATION\n");
        details.append("Vehicle Model: ").append(vehicleModel).append("\n");
        details.append("Registration: ").append(vehicleNo).append("\n\n");

        details.append("SERVICE INFORMATION\n");
        details.append("Service Type: ").append(serviceType).append("\n");
        details.append("Issue Description: ").append(issueDescription).append("\n\n");

        if (technicianId != null && !technicianId.isEmpty()) {
            details.append("TECHNICIAN ASSIGNMENT\n");
            details.append("Technician Name: ").append(technicianName).append("\n");
            details.append("Technician ID: ").append(technicianId).append("\n\n");
        }

        details.append("COST INFORMATION\n");
        details.append("Estimated Cost: RM ").append(String.format("%.2f", estimatedCost)).append("\n");
        if (actualCost > 0) {
            details.append("Actual Cost: RM ").append(String.format("%.2f", actualCost)).append("\n");
        }
        details.append("════════════════════════════════════════════════\n");

        return details.toString();
    }

    /**
     * Assign technician to appointment
     * Demonstrates: Association and relationship management
     *
     * @param technicianId ID of technician to assign
     * @param technicianName Name of technician
     * @return true if assignment successful
     */
    public boolean assignTechnician(String technicianId, String technicianName) {
        // Validation
        if (technicianId == null || technicianId.isEmpty()) {
            System.out.println("Error: Technician ID cannot be empty");
            return false;
        }

        if (technicianName == null || technicianName.isEmpty()) {
            System.out.println("Error: Technician name cannot be empty");
            return false;
        }

        // Check if appointment is in valid state for assignment
        if (!status.equalsIgnoreCase("Pending") && !status.equalsIgnoreCase("Scheduled")) {
            System.out.println("Error: Can only assign technician to Pending or Scheduled appointments");
            return false;
        }

        this.technicianId = technicianId;
        this.technicianName = technicianName;
        this.status = "Scheduled";

        System.out.println("✓ Technician assigned successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Technician: " + technicianName + " (ID: " + technicianId + ")");
        System.out.println("  - Status: " + status);
        System.out.println("  - Date & Time: " + appointmentDate + " at " + appointmentTime);
        System.out.println("  - Duration: " + getServiceDuration());

        return true;
    }

    /**
     * Update appointment status
     * Demonstrates: State management and lifecycle
     *
     * @param newStatus New status for the appointment
     * @return true if status update successful
     */
    public boolean updateStatus(String newStatus) {
        // Validation
        if (newStatus == null || newStatus.isEmpty()) {
            System.out.println("Error: New status cannot be empty");
            return false;
        }

        // Validate status value
        if (!isValidStatus(newStatus)) {
            System.out.println("Error: Invalid status. Must be Pending, Scheduled, In-Progress, Completed, or Cancelled");
            return false;
        }

        // Validate status transitions
        if (!isValidStatusTransition(status, newStatus)) {
            System.out.println("Error: Invalid status transition from " + status + " to " + newStatus);
            return false;
        }

        String oldStatus = this.status;
        this.status = newStatus;

        System.out.println("✓ Appointment status updated successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Previous Status: " + oldStatus);
        System.out.println("  - New Status: " + newStatus);
        System.out.println("  - Updated: [System Date & Time]");

        return true;
    }

    /**
     * Provide feedback on appointment
     * Demonstrates: Feedback mechanism
     *
     * @param feedback Feedback comment
     * @return true if feedback recorded successfully
     */
    public boolean provideFeedback(String feedback) {
        if (!status.equalsIgnoreCase("Completed")) {
            System.out.println("Error: Can only provide feedback for completed appointments");
            return false;
        }

        if (feedback == null || feedback.isEmpty()) {
            System.out.println("Error: Feedback cannot be empty");
            return false;
        }

        if (feedback.length() < 10) {
            System.out.println("Error: Feedback must be at least 10 characters");
            return false;
        }

        this.feedbackProvided = true;
        this.notes = feedback;

        System.out.println("✓ Feedback recorded successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Feedback: " + feedback);
        System.out.println("  - Recorded: [System Date & Time]");

        return true;
    }

    /**
     * Cancel appointment
     * Demonstrates: Lifecycle management
     *
     * @param cancellationReason Reason for cancellation
     * @return true if cancellation successful
     */
    public boolean cancelAppointment(String cancellationReason) {
        if (status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("Cancelled")) {
            System.out.println("Error: Cannot cancel " + status + " appointment");
            return false;
        }

        this.status = "Cancelled";
        this.notes = "Cancelled - " + (cancellationReason != null ? cancellationReason : "No reason provided");

        System.out.println("✓ Appointment cancelled successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Customer: " + customerName);
        System.out.println("  - Reason: " + cancellationReason);
        System.out.println("  - Cancelled: [System Date & Time]");

        return true;
    }

    /**
     * Reschedule appointment
     * Demonstrates: Appointment modification
     *
     * @param newDate New appointment date
     * @param newTime New appointment time
     * @return true if rescheduling successful
     */
    public boolean rescheduleAppointment(String newDate, String newTime) {
        if (status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("Cancelled")) {
            System.out.println("Error: Cannot reschedule " + status + " appointment");
            return false;
        }

        if (newDate == null || newDate.isEmpty()) {
            System.out.println("Error: New date cannot be empty");
            return false;
        }

        if (newTime == null || newTime.isEmpty()) {
            System.out.println("Error: New time cannot be empty");
            return false;
        }

        String oldDate = this.appointmentDate;
        String oldTime = this.appointmentTime;

        this.appointmentDate = newDate;
        this.appointmentTime = newTime;

        System.out.println("✓ Appointment rescheduled successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Previous: " + oldDate + " at " + oldTime);
        System.out.println("  - New: " + newDate + " at " + newTime);

        return true;
    }

    // ============== HELPER METHODS ==============

    /**
     * Get service duration
     * Demonstrates: Business rule implementation
     *
     * @return duration as string
     */
    public String getServiceDuration() {
        if (serviceType.equalsIgnoreCase("Normal")) {
            return "1 hour";
        } else if (serviceType.equalsIgnoreCase("Major")) {
            return "3 hours";
        }
        return "Unknown";
    }

    /**
     * Validate status value
     */
    private boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("Pending") ||
                status.equalsIgnoreCase("Scheduled") ||
                status.equalsIgnoreCase("In-Progress") ||
                status.equalsIgnoreCase("Completed") ||
                status.equalsIgnoreCase("Cancelled");
    }

    /**
     * Validate status transitions
     * Demonstrates: State machine pattern
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // Define valid transitions
        if (currentStatus.equalsIgnoreCase("Pending")) {
            return newStatus.equalsIgnoreCase("Scheduled") || newStatus.equalsIgnoreCase("Cancelled");
        }
        if (currentStatus.equalsIgnoreCase("Scheduled")) {
            return newStatus.equalsIgnoreCase("In-Progress") || newStatus.equalsIgnoreCase("Cancelled");
        }
        if (currentStatus.equalsIgnoreCase("In-Progress")) {
            return newStatus.equalsIgnoreCase("Completed") || newStatus.equalsIgnoreCase("Cancelled");
        }
        // Completed and Cancelled are terminal states
        return false;
    }

    /**
     * Display appointment summary
     */
    public void displayAppointmentSummary() {
        System.out.println("\n════════════════════════════════════════════════");
        System.out.println("   APPOINTMENT SUMMARY");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Customer: " + customerName);
        System.out.println("Date & Time: " + appointmentDate + " at " + appointmentTime);
        System.out.println("Service: " + serviceType);
        System.out.println("Status: " + status);
        System.out.println("Technician: " + (technicianName != null ? technicianName : "Not assigned"));
        System.out.println("════════════════════════════════════════════════\n");
    }

    // ============== UTILITY METHODS ==============

    /**
     * Convert appointment object to string format for file storage
     * Format: appointmentId|customerId|vehicleNo|appointmentDate|appointmentTime|
     *         serviceType|status|technicianId|estimatedCost|actualCost
     */
    @Override
    public String toString() {
        return appointmentId + "|" + customerId + "|" + vehicleNo + "|" +
                appointmentDate + "|" + appointmentTime + "|" + serviceType + "|" +
                status + "|" + (technicianId != null ? technicianId : "Unassigned") + "|" +
                estimatedCost + "|" + actualCost;
    }

    /**
     * Detailed string representation
     */
    public String toDetailedString() {
        return String.format("Appointment{id='%s', customer='%s', date='%s', service='%s', status='%s', technician='%s'}",
                appointmentId, customerName, appointmentDate, serviceType, status,
                technicianName != null ? technicianName : "Unassigned");
    }
}


/**
 * 
 * my name is zul
 */