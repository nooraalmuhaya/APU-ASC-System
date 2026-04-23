package com.asu.model;

/**
 * Feedback Class
 * Demonstrates: Encapsulation, Data Validation, Single Responsibility Principle
 *
 * The Feedback class handles:
 * - Customer and technician feedback recording
 * - Rating management
 * - Comment and feedback aggregation
 * - Feedback retrieval and analysis
 *
 * Composition: Feedback is created for specific appointments
 */
public class feedback {

    // Private attributes - Demonstrates Encapsulation
    private String feedbackId;
    private String appointmentId;
    private String givenBy;              // Customer or Technician
    private String givenById;
    private String comment;
    private int rating;                  // 1-5 stars
    private String feedbackDate;
    private String feedbackType;         // CustomerFeedback or TechnicianFeedback
    private String category;             // Service Quality, Staff Behavior, Cleanliness, etc.
    private boolean isHelpful;
    private boolean isVisible;

    /**
     * Constructor - Initialize Feedback
     * @param feedbackId Unique feedback identifier
     * @param appointmentId Associated appointment ID
     * @param givenBy Name of person giving feedback
     * @param givenById ID of person giving feedback
     * @param comment Feedback comment
     * @param rating Rating (1-5)
     * @param feedbackDate Date feedback was given
     * @param feedbackType Type of feedback (CustomerFeedback or TechnicianFeedback)
     */
    public feedback(String feedbackId, String appointmentId, String givenBy,
                    String givenById, String comment, int rating,
                    String feedbackDate, String feedbackType) {
        this.feedbackId = feedbackId;
        this.appointmentId = appointmentId;
        this.givenBy = givenBy;
        this.givenById = givenById;
        this.comment = comment;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
        this.feedbackType = feedbackType;
        this.isHelpful = false;
        this.isVisible = true;
        this.category = "General";
    }

    // ============== GETTER METHODS ==============
    public String getFeedbackId() {
        return feedbackId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public String getGivenById() {
        return givenById;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public String getCategory() {
        return category;
    }

    public boolean isHelpful() {
        return isHelpful;
    }

    public boolean isVisible() {
        return isVisible;
    }

    // ============== SETTER METHODS ==============
    public void setComment(String comment) {
        if (comment != null && !comment.isEmpty() && comment.length() >= 10) {
            this.comment = comment;
        } else {
            System.out.println("Error: Comment must be at least 10 characters");
        }
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            System.out.println("Error: Rating must be between 1 and 5");
        }
    }

    public void setCategory(String category) {
        if (category != null && !category.isEmpty()) {
            if (isValidCategory(category)) {
                this.category = category;
            } else {
                System.out.println("Error: Invalid category");
            }
        }
    }

    public void setHelpful(boolean isHelpful) {
        this.isHelpful = isHelpful;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    // ============== FEEDBACK MANAGEMENT METHODS ==============

    /**
     * Add feedback with validation
     * Demonstrates: Complex validation logic
     *
     * @param comment Feedback comment
     * @param rating Rating (1-5)
     * @param category Feedback category
     * @return true if feedback added successfully
     */
    public boolean addFeedback(String comment, int rating, String category) {
        // Validate comment
        if (comment == null || comment.isEmpty()) {
            System.out.println("Error: Comment cannot be empty");
            return false;
        }

        if (comment.length() < 10) {
            System.out.println("Error: Comment must be at least 10 characters");
            System.out.println("Current length: " + comment.length() + " characters");
            return false;
        }

        if (comment.length() > 500) {
            System.out.println("Error: Comment cannot exceed 500 characters");
            return false;
        }

        // Validate rating
        if (rating < 1 || rating > 5) {
            System.out.println("Error: Rating must be between 1 and 5");
            return false;
        }

        // Validate category
        if (category == null || category.isEmpty() || !isValidCategory(category)) {
            System.out.println("Error: Invalid feedback category");
            return false;
        }

        // Set feedback values
        this.comment = comment;
        this.rating = rating;
        this.category = category;
        this.isVisible = true;

        System.out.println("✓ Feedback added successfully:");
        System.out.println("  - Feedback ID: " + feedbackId);
        System.out.println("  - Given by: " + givenBy);
        System.out.println("  - Rating: " + rating + "/5");
        System.out.println("  - Category: " + category);
        System.out.println("  - Date: " + feedbackDate);

        return true;
    }

    /**
     * Edit existing feedback
     * Demonstrates: Data modification
     *
     * @param newComment Updated comment
     * @param newRating Updated rating
     * @return true if feedback updated successfully
     */
    public boolean editFeedback(String newComment, int newRating) {
        // Validate inputs
        if (newComment == null || newComment.isEmpty()) {
            System.out.println("Error: Comment cannot be empty");
            return false;
        }

        if (newComment.length() < 10 || newComment.length() > 500) {
            System.out.println("Error: Comment must be between 10 and 500 characters");
            return false;
        }

        if (newRating < 1 || newRating > 5) {
            System.out.println("Error: Rating must be between 1 and 5");
            return false;
        }

        String oldComment = this.comment;
        int oldRating = this.rating;

        this.comment = newComment;
        this.rating = newRating;

        System.out.println("✓ Feedback updated successfully:");
        System.out.println("  - Feedback ID: " + feedbackId);
        System.out.println("  - Previous Rating: " + oldRating + "/5");
        System.out.println("  - New Rating: " + newRating + "/5");
        System.out.println("  - Updated: [System Date & Time]");

        return true;
    }

    /**
     * Delete feedback
     * Demonstrates: Data deletion with validation
     *
     * @param deletionReason Reason for deletion
     * @return true if feedback deleted successfully
     */
    public boolean deleteFeedback(String deletionReason) {
        if (deletionReason == null || deletionReason.isEmpty()) {
            System.out.println("Error: Deletion reason cannot be empty");
            return false;
        }

        this.isVisible = false;

        System.out.println("✓ Feedback deleted successfully:");
        System.out.println("  - Feedback ID: " + feedbackId);
        System.out.println("  - Reason: " + deletionReason);
        System.out.println("  - Status: DELETED (Hidden)");

        return true;
    }

    /**
     * Mark feedback as helpful
     * Demonstrates: User interaction tracking
     */
    public void markAsHelpful() {
        this.isHelpful = true;
        System.out.println("✓ Feedback marked as helpful (ID: " + feedbackId + ")");
    }

    /**
     * Mark feedback as unhelpful
     */
    public void markAsUnhelpful() {
        this.isHelpful = false;
        System.out.println("✓ Feedback marked as unhelpful (ID: " + feedbackId + ")");
    }

    /**
     * Get feedback sentiment
     * Demonstrates: Data analysis
     *
     * @return sentiment based on rating
     */
    public String getFeedbackSentiment() {
        switch (rating) {
            case 5:
                return "Excellent";
            case 4:
                return "Good";
            case 3:
                return "Neutral";
            case 2:
                return "Poor";
            case 1:
                return "Very Poor";
            default:
                return "Unknown";
        }
    }

    /**
     * Display feedback
     * Demonstrates: Formatted display
     */
    public void displayFeedback() {
        System.out.println("\n════════════════════════════════════════════════");
        System.out.println("   FEEDBACK DETAILS");
        System.out.println("════════════════════════════════════════════════");
        System.out.println("Feedback ID: " + feedbackId);
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Given by: " + givenBy + " (" + feedbackType + ")");
        System.out.println("Date: " + feedbackDate);
        System.out.println("Category: " + category);
        System.out.println("Rating: " + rating + "/5 (" + getFeedbackSentiment() + ")");
        System.out.println("────────────────────────────────────────────────");
        System.out.println("Comment: " + comment);
        System.out.println("────────────────────────────────────────────────");
        System.out.println("Visibility: " + (isVisible ? "PUBLIC" : "HIDDEN"));
        System.out.println("Marked Helpful: " + (isHelpful ? "YES" : "NO"));
        System.out.println("════════════════════════════════════════════════\n");
    }

    /**
     * Get feedback summary
     */
    public String getFeedbackSummary() {
        return "Feedback ID: " + feedbackId +
                " | Rating: " + rating + "/5 (" + getFeedbackSentiment() + ")" +
                " | Category: " + category +
                " | By: " + givenBy;
    }

    /**
     * Get feedback preview (first 50 characters)
     */
    public String getFeedbackPreview() {
        int previewLength = Math.min(50, comment.length());
        return comment.substring(0, previewLength) + (comment.length() > 50 ? "..." : "");
    }

    // ============== VALIDATION HELPER METHODS ==============

    /**
     * Validate feedback category
     */
    private boolean isValidCategory(String category) {
        return category.equalsIgnoreCase("Service Quality") ||
                category.equalsIgnoreCase("Staff Behavior") ||
                category.equalsIgnoreCase("Cleanliness") ||
                category.equalsIgnoreCase("Pricing") ||
                category.equalsIgnoreCase("Facilities") ||
                category.equalsIgnoreCase("General");
    }

    /**
     * Validate feedback data
     */
    public boolean isValidFeedback() {
        return feedbackId != null && !feedbackId.isEmpty() &&
                appointmentId != null && !appointmentId.isEmpty() &&
                comment != null && comment.length() >= 10 &&
                rating >= 1 && rating <= 5 &&
                isValidCategory(category);
    }

    // ============== UTILITY METHODS ==============

    /**
     * Convert feedback object to string format for file storage
     * Format: feedbackId|appointmentId|givenBy|rating|feedbackDate|feedbackType|category
     */
    @Override
    public String toString() {
        return feedbackId + "|" + appointmentId + "|" + givenBy + "|" + rating + "|" +
                feedbackDate + "|" + feedbackType + "|" + category;
    }

    /**
     * Detailed string representation
     */
    public String toDetailedString() {
        return String.format("Feedback{id='%s', appointment='%s', rating=%d, sentiment='%s', category='%s', visible=%s}",
                feedbackId, appointmentId, rating, getFeedbackSentiment(), category, isVisible);
    }

    /**
     * Generate feedback report
     */
    public String generateFeedbackReport() {
        StringBuilder report = new StringBuilder();

        report.append("\n════════════════════════════════════════════════\n");
        report.append("   FEEDBACK REPORT\n");
        report.append("════════════════════════════════════════════════\n");
        report.append("Feedback ID: ").append(feedbackId).append("\n");
        report.append("Appointment: ").append(appointmentId).append("\n");
        report.append("Given by: ").append(givenBy).append("\n");
        report.append("Rating: ").append(rating).append("/5 (").append(getFeedbackSentiment()).append(")\n");
        report.append("Category: ").append(category).append("\n");
        report.append("Date: ").append(feedbackDate).append("\n");
        report.append("────────────────────────────────────────────────\n");
        report.append("Comment Summary:\n");
        report.append(comment).append("\n");
        report.append("────────────────────────────────────────────────\n");
        report.append("Helpful Votes: ").append(isHelpful ? "1" : "0").append("\n");
        report.append("Public Status: ").append(isVisible ? "Visible" : "Hidden").append("\n");
        report.append("════════════════════════════════════════════════\n");

        return report.toString();
    }
}
