package com.asu.model;

/**
 * CounterStaff Class - Extends User
 * Demonstrates: Inheritance, Polymorphism, Method Overriding
 *
 * The Counter Staff role is responsible for:
 * - Customer management (Create, Read, Update, Delete)
 * - Appointment creation and assignment
 * - Payment collection and receipt generation
 * - Customer interactions at the service counter
 */
public class CounterStaff extends user {

    // Additional attributes specific to CounterStaff
    private String workShift;
    private String counterNumber;
    private int appointmentsHandled;
    private double totalCollected;

    /**
     * Constructor - Initialize CounterStaff with details
     * @param userId Unique staff identifier
     * @param name Staff member's full name
     * @param phone Contact number
     * @param username Login username
     * @param password Login password
     * @param workShift Work shift (Morning, Afternoon, Evening)
     * @param counterNumber Counter assignment number
     */
    public CounterStaff(String userId, String name, String phone, String username,
                        String password, String workShift, String counterNumber) {
        super(userId, name, phone, username, password, "CounterStaff");
        this.workShift = workShift;
        this.counterNumber = counterNumber;
        this.appointmentsHandled = 0;
        this.totalCollected = 0.0;
    }

    // ============== GETTER METHODS ==============
    public String getWorkShift() {
        return workShift;
    }

    public String getCounterNumber() {
        return counterNumber;
    }

    public int getAppointmentsHandled() {
        return appointmentsHandled;
    }

    public double getTotalCollected() {
        return totalCollected;
    }

    // ============== SETTER METHODS ==============
    public void setWorkShift(String workShift) {
        if (workShift != null && !workShift.isEmpty()) {
            this.workShift = workShift;
        }
    }

    public void setCounterNumber(String counterNumber) {
        if (counterNumber != null && !counterNumber.isEmpty()) {
            this.counterNumber = counterNumber;
        }
    }

    // ============== CUSTOMER MANAGEMENT METHODS ==============

    /**
     * Create a new customer record
     * Demonstrates: Business logic for CRUD operations
     *
     * @param customerId Unique customer identifier
     * @param name Customer's full name
     * @param phone Customer's phone number
     * @param email Customer's email address
     * @return true if customer created successfully
     */
    public boolean addCustomer(String customerId, String name, String phone, String email) {
        // Input validation
        if (!validateCustomerInput(customerId, name, phone, email)) {
            System.out.println("Customer creation failed: Invalid input");
            return false;
        }

        System.out.println("✓ Customer added successfully:");
        System.out.println("  - Customer ID: " + customerId);
        System.out.println("  - Name: " + name);
        System.out.println("  - Phone: " + phone);
        System.out.println("  - Email: " + email);
        return true;
    }

    /**
     * Update existing customer information
     * Demonstrates: Data modification with validation
     *
     * @param customerId ID of customer to update
     * @param name New name
     * @param phone New phone number
     * @param email New email address
     * @return true if update successful
     */
    public boolean updateCustomer(String customerId, String name, String phone, String email) {
        if (customerId == null || customerId.isEmpty()) {
            System.out.println("Error: Customer ID cannot be empty");
            return false;
        }

        // Check if at least one field is being updated
        if ((name == null || name.isEmpty()) &&
                (phone == null || phone.isEmpty()) &&
                (email == null || email.isEmpty())) {
            System.out.println("Error: At least one field must be updated");
            return false;
        }

        System.out.println("✓ Customer updated successfully (ID: " + customerId + "):");
        if (name != null && !name.isEmpty()) {
            System.out.println("  - New Name: " + name);
        }
        if (phone != null && !phone.isEmpty()) {
            System.out.println("  - New Phone: " + phone);
        }
        if (email != null && !email.isEmpty()) {
            System.out.println("  - New Email: " + email);
        }
        return true;
    }

    /**
     * Delete customer record from system
     * Demonstrates: Data deletion with authorization
     *
     * @param customerId ID of customer to delete
     * @return true if deletion successful
     */
    public boolean deleteCustomer(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            System.out.println("Error: Customer ID cannot be empty");
            return false;
        }

        System.out.println("✓ Customer deleted successfully (ID: " + customerId + ")");
        return true;
    }

    /**
     * Retrieve customer information
     * Demonstrates: Data retrieval
     *
     * @param customerId ID of customer to retrieve
     * @return customer information
     */
    public String getCustomerInfo(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            System.out.println("Error: Customer ID cannot be empty");
            return "";
        }

        return "Customer ID: " + customerId + "\n[Customer details retrieved from database]";
    }

    // ============== APPOINTMENT MANAGEMENT METHODS ==============

    /**
     * Create new appointment with technician assignment
     * Demonstrates: Complex business logic with multiple validations
     *
     * @param appointmentId Unique appointment ID
     * @param customerId Customer ID
     * @param vehicleNo Vehicle number/registration
     * @param serviceType Type of service (Normal/Major)
     * @param issueDescription Description of the issue
     * @param appointmentDate Date of appointment
     * @param appointmentTime Time of appointment
     * @return true if appointment created successfully
     */
    public boolean createAppointment(String appointmentId, String customerId, String vehicleNo,
                                     String serviceType, String issueDescription,
                                     String appointmentDate, String appointmentTime) {
        // Validate appointment details
        if (!validateAppointmentInput(appointmentId, customerId, vehicleNo,
                serviceType, issueDescription, appointmentDate, appointmentTime)) {
            System.out.println("Appointment creation failed: Invalid input");
            return false;
        }

        // Validate service type
        if (!serviceType.equalsIgnoreCase("Normal") && !serviceType.equalsIgnoreCase("Major")) {
            System.out.println("Error: Service type must be 'Normal' or 'Major'");
            return false;
        }

        System.out.println("✓ Appointment created successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Customer ID: " + customerId);
        System.out.println("  - Vehicle: " + vehicleNo);
        System.out.println("  - Service Type: " + serviceType);
        System.out.println("  - Date: " + appointmentDate);
        System.out.println("  - Time: " + appointmentTime);
        System.out.println("  - Duration: " + (serviceType.equalsIgnoreCase("Normal") ? "1 hour" : "3 hours"));

        appointmentsHandled++;
        return true;
    }

    /**
     * Assign appointment to available technician
     * Demonstrates: Resource allocation and availability checking
     *
     * @param appointmentId ID of appointment to assign
     * @param technicianId ID of technician to assign
     * @return true if assignment successful
     */
    public boolean assignAppointment(String appointmentId, String technicianId) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return false;
        }

        if (technicianId == null || technicianId.isEmpty()) {
            System.out.println("Error: Technician ID cannot be empty");
            return false;
        }

        // In a real system, this would check technician availability
        System.out.println("✓ Appointment assigned successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Assigned to Technician: " + technicianId);
        System.out.println("  - Status: ASSIGNED");

        return true;
    }

    // ============== PAYMENT METHODS ==============

    /**
     * Collect payment for service
     * Demonstrates: Financial transaction processing
     *
     * @param appointmentId Associated appointment
     * @param amount Amount to collect
     * @param paymentMethod Method of payment (Cash, Card, Cheque)
     * @return true if payment collected successfully
     */
    public boolean collectPayment(String appointmentId, double amount, String paymentMethod) {
        // Validate payment details
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Error: Amount must be greater than 0");
            return false;
        }

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            System.out.println("Error: Payment method cannot be empty");
            return false;
        }

        // Validate payment method
        if (!isValidPaymentMethod(paymentMethod)) {
            System.out.println("Error: Invalid payment method. Accepted: Cash, Card, Cheque");
            return false;
        }

        System.out.println("✓ Payment collected successfully:");
        System.out.println("  - Appointment ID: " + appointmentId);
        System.out.println("  - Amount: RM " + String.format("%.2f", amount));
        System.out.println("  - Payment Method: " + paymentMethod);
        System.out.println("  - Payment Status: COMPLETED");

        totalCollected += amount;
        return true;
    }

    /**
     * Generate receipt for payment
     * Demonstrates: Document generation
     *
     * @param appointmentId Associated appointment
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @return receipt string
     */
    public String generateReceipt(String appointmentId, double amount, String paymentMethod) {
        if (appointmentId == null || appointmentId.isEmpty() || amount <= 0 ||
                paymentMethod == null || paymentMethod.isEmpty()) {
            System.out.println("Error: Cannot generate receipt with invalid data");
            return "";
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("\n╔════════════════════════════════════════╗\n");
        receipt.append("║     APU AUTOMOTIVE SERVICE CENTRE    ║\n");
        receipt.append("║            SERVICE RECEIPT            ║\n");
        receipt.append("╠════════════════════════════════════════╣\n");
        receipt.append("║ Receipt ID: RCP_" + appointmentId + "\n");
        receipt.append("║ Counter Staff: " + this.getName() + "\n");
        receipt.append("║ Counter Number: " + this.counterNumber + "\n");
        receipt.append("║ Appointment ID: " + appointmentId + "\n");
        receipt.append("║ Amount: RM " + String.format("%.2f", amount) + "\n");
        receipt.append("║ Payment Method: " + paymentMethod + "\n");
        receipt.append("║ Date: [System Date]\n");
        receipt.append("║ Time: [System Time]\n");
        receipt.append("║ Status: PAID\n");
        receipt.append("╚════════════════════════════════════════╝\n");

        System.out.println(receipt.toString());
        return receipt.toString();
    }

    // ============== OVERRIDE ABSTRACT METHODS ==============

    @Override
    public void displayDashboard() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   COUNTER STAFF DASHBOARD              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Welcome, " + this.getName());
        System.out.println("║ Counter: " + this.counterNumber);
        System.out.println("║ Shift: " + this.workShift);
        System.out.println("║ Status: " + (this.isLoggedIn() ? "ONLINE" : "OFFLINE"));
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Today's Statistics:                    ║");
        System.out.println("║ Appointments Handled: " + appointmentsHandled);
        System.out.println("║ Total Collected: RM " + String.format("%.2f", totalCollected));
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Available Functions:                   ║");
        System.out.println("║ 1. Manage Customers (CRUD)             ║");
        System.out.println("║ 2. Create Appointments                 ║");
        System.out.println("║ 3. Assign Appointments to Technicians  ║");
        System.out.println("║ 4. Collect Payments                    ║");
        System.out.println("║ 5. Generate Receipts                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    @Override
    public void editProfile() {
        System.out.println("\n========== EDIT COUNTER STAFF PROFILE ==========");
        System.out.println("Current Name: " + this.getName());
        System.out.println("Current Phone: " + this.getPhone());
        System.out.println("Current Work Shift: " + this.workShift);
        System.out.println("Current Counter: " + this.counterNumber);
        System.out.println("Profile editing in progress...");
        System.out.println("===============================================\n");
    }

    @Override
    public String getUserRole() {
        return "Counter Staff - Counter " + counterNumber + " (" + workShift + " Shift)";
    }

    // ============== VALIDATION HELPER METHODS ==============

    /**
     * Validate customer input
     */
    private boolean validateCustomerInput(String customerId, String name, String phone, String email) {
        if (customerId == null || customerId.isEmpty()) {
            System.out.println("Error: Customer ID cannot be empty");
            return false;
        }
        if (name == null || name.isEmpty()) {
            System.out.println("Error: Customer name cannot be empty");
            return false;
        }
        if (phone == null || phone.isEmpty()) {
            System.out.println("Error: Customer phone cannot be empty");
            return false;
        }
        if (email == null || email.isEmpty()) {
            System.out.println("Error: Customer email cannot be empty");
            return false;
        }
        if (!email.contains("@")) {
            System.out.println("Error: Invalid email format");
            return false;
        }
        return true;
    }

    /**
     * Validate appointment input
     */
    private boolean validateAppointmentInput(String appointmentId, String customerId,
                                             String vehicleNo, String serviceType,
                                             String issueDescription, String appointmentDate,
                                             String appointmentTime) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("Error: Appointment ID cannot be empty");
            return false;
        }
        if (customerId == null || customerId.isEmpty()) {
            System.out.println("Error: Customer ID cannot be empty");
            return false;
        }
        if (vehicleNo == null || vehicleNo.isEmpty()) {
            System.out.println("Error: Vehicle number cannot be empty");
            return false;
        }
        if (serviceType == null || serviceType.isEmpty()) {
            System.out.println("Error: Service type cannot be empty");
            return false;
        }
        if (issueDescription == null || issueDescription.isEmpty()) {
            System.out.println("Error: Issue description cannot be empty");
            return false;
        }
        if (appointmentDate == null || appointmentDate.isEmpty()) {
            System.out.println("Error: Appointment date cannot be empty");
            return false;
        }
        if (appointmentTime == null || appointmentTime.isEmpty()) {
            System.out.println("Error: Appointment time cannot be empty");
            return false;
        }
        return true;
    }

    /**
     * Validate payment method
     */
    private boolean isValidPaymentMethod(String method) {
        return method.equalsIgnoreCase("Cash") ||
                method.equalsIgnoreCase("Card") ||
                method.equalsIgnoreCase("Cheque");
    }

    /**
     * Override toString for file storage
     */
    @Override
    public String toString() {
        return super.toString() + "|" + workShift + "|" + counterNumber;
    }
}
