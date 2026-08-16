package com.library.model;

/**
 * Membership categories, each carrying its own waiver rate.
 * Unlike ItemStatus, each value is an object that knows its own rate.
 * This eliminates the need for a separate table mapping category names to percentages.
 */
public enum MembershipType {
    STUDENT("Student", 0.25),      // 25% waived
    STAFF("Staff", 0.10),           // 10% waived
    PUBLIC("Public", 0.0);          // No waiver

    private final String displayName;
    private final double waiverRate;

    MembershipType(String displayName, double waiverRate) {
        this.displayName = displayName;
        this.waiverRate = waiverRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getWaiverRate() {
        return waiverRate;
    }

    /**
     * Applies the waiver to a fine amount.
     * The waiver only applies to the base fine, never to the administrative charge.
     *
     * @param baseFine the fine before waiver is applied
     * @return the amount to waive from the base fine
     */
    public double calculateWaiver(double baseFine) {
        return baseFine * waiverRate;
    }
}
