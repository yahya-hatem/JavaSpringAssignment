/**
 * Enum representing the possible states of a restaurant order.
 * Using an enum ensures type safety and prevents invalid status values.
 */
public enum OrderStatus {
    PENDING("Pending"),
    IN_KITCHEN("In Kitchen"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
