package com.asu.model;

/**
 * Service Class
 * Demonstrates: Encapsulation, Data Abstraction, Business Object Pattern
 *
 * The Service class represents an automotive service offering with:
 * - Service details (ID, name, type)
 * - Pricing information
 * - Duration specifications
 * - Price update functionality
 */
public class Service {

    // Private attributes - Demonstrates Encapsulation
    private String serviceId;
    private String serviceName;
    private String serviceType;      // Normal or Major
    private double price;
    private int duration;             // Duration in hours
    private String description;
    private boolean isActive;
    private String lastUpdated;

    /**
     * Constructor - Initialize Service with required details
     * @param serviceId Unique service identifier
     * @param serviceName Name of the service
     * @param serviceType Type of service (Normal or Major)
     * @param price Price of the service
     * @param duration Duration in hours
     */
    public Service(String serviceId, String serviceName, String serviceType, double price, int duration) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.price = price;
        this.duration = duration;
        this.isActive = true;
        this.lastUpdated = "[System Date]";
        this.description = "";
    }

    // ============== GETTER METHODS ==============
    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    // ============== SETTER METHODS ==============
    public void setServiceName(String serviceName) {
        if (serviceName != null && !serviceName.isEmpty()) {
            this.serviceName = serviceName;
            updateTimestamp();
        }
    }

    public void setServiceType(String serviceType) {
        if (serviceType != null && !serviceType.isEmpty()) {
            if (serviceType.equalsIgnoreCase("Normal") || serviceType.equalsIgnoreCase("Major")) {
                this.serviceType = serviceType;
                updateTimestamp();
            } else {
                System.out.println("Error: Service type must be 'Normal' or 'Major'");
            }
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
            updateTimestamp();
        }
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        updateTimestamp();
    }

    // ============== PRICE MANAGEMENT METHODS ==============

    /**
     * Update the price of the service
     * Demonstrates: Business logic with validation
     *
     * @param newPrice New price for the service
     * @return true if price updated successfully
     */
    public boolean updatePrice(double newPrice) {
        if (newPrice < 0) {
            System.out.println("Error: Price cannot be negative");
            return false;
        }

        double oldPrice = this.price;
        this.price = newPrice;
        updateTimestamp();

        System.out.println("✓ Service price updated successfully:");
        System.out.println("  - Service ID: " + this.serviceId);
        System.out.println("  - Service Name: " + this.serviceName);
        System.out.println("  - Old Price: RM " + String.format("%.2f", oldPrice));
        System.out.println("  - New Price: RM " + String.format("%.2f", newPrice));
        System.out.println("  - Change: RM " + String.format("%.2f", (newPrice - oldPrice)));
        System.out.println("  - Updated: " + this.lastUpdated);

        return true;
    }

    /**
     * Apply a percentage discount to the service price
     * Demonstrates: Calculation logic
     *
     * @param discountPercent Discount percentage (0-100)
     * @return discounted price
     */
    public double applyDiscount(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            System.out.println("Error: Discount percentage must be between 0 and 100");
            return this.price;
        }

        double discountAmount = (this.price * discountPercent) / 100;
        double discountedPrice = this.price - discountAmount;

        System.out.println("Discount Applied:");
        System.out.println("  - Original Price: RM " + String.format("%.2f", this.price));
        System.out.println("  - Discount: " + discountPercent + "%");
        System.out.println("  - Discount Amount: RM " + String.format("%.2f", discountAmount));
        System.out.println("  - Final Price: RM " + String.format("%.2f", discountedPrice));

        return discountedPrice;
    }

    /**
     * Get service pricing details
     * Demonstrates: Information encapsulation
     *
     * @return formatted pricing information
     */
    public String getPricingDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Service: " + this.serviceName + "\n");
        details.append("Type: " + this.serviceType + "\n");
        details.append("Price: RM " + String.format("%.2f", this.price) + "\n");
        details.append("Duration: " + this.duration + " hour(s)\n");

        return details.toString();
    }

    // ============== SERVICE INFORMATION METHODS ==============

    /**
     * Display complete service information
     * Demonstrates: Information presentation
     */
    public void displayServiceInfo() {
        System.out.println("\n════════════════════════════════════════════════\n");
        System.out.println("   SERVICE INFORMATION\n");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("Service ID: " + this.serviceId);
        System.out.println("Service Name: " + this.serviceName);
        System.out.println("Service Type: " + this.serviceType);
        System.out.println("Price: RM " + String.format("%.2f", this.price));
        System.out.println("Duration: " + this.duration + " hour(s)");
        System.out.println("Description: " + (this.description.isEmpty() ? "No description" : this.description));
        System.out.println("Status: " + (this.isActive ? "ACTIVE" : "INACTIVE"));
        System.out.println("Last Updated: " + this.lastUpdated);
        System.out.println("════════════════════════════════════════════════\n");
    }

    /**
     * Check if service is available for booking
     * Demonstrates: Status checking
     *
     * @return true if service is active and available
     */
    public boolean isAvailable() {
        return this.isActive;
    }

    /**
     * Get service duration in minutes
     * Demonstrates: Unit conversion
     *
     * @return duration in minutes
     */
    public int getDurationInMinutes() {
        return this.duration * 60;
    }

    /**
     * Compare service prices
     * Demonstrates: Comparison logic
     *
     * @param otherPrice Price to compare with
     * @return difference in price
     */
    public double comparePriceWith(double otherPrice) {
        return this.price - otherPrice;
    }

    // ============== UTILITY METHODS ==============

    /**
     * Update the last modified timestamp
     * Demonstrates: Audit trail
     */
    private void updateTimestamp() {
        this.lastUpdated = "[System Date & Time]";
    }

    /**
     * Convert service object to string format for file storage
     * Format: serviceId|serviceName|serviceType|price|duration
     */
    @Override
    public String toString() {
        return serviceId + "|" + serviceName + "|" + serviceType + "|" + price + "|" + duration;
    }

    /**
     * Detailed string representation for display
     */
    public String toDetailedString() {
        return String.format("Service{id='%s', name='%s', type='%s', price=%.2f, duration=%d, active=%s}",
                serviceId, serviceName, serviceType, price, duration, isActive);
    }
}
