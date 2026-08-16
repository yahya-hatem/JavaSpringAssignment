package com.library.model;

/**
 * An enumeration of possible states for library items.
 * An item is always in exactly one of these states.
 * Using a fixed set of values prevents invalid states from being written down.
 */
public enum ItemStatus {
    AVAILABLE("Available for borrowing"),
    ON_LOAN("Currently borrowed"),
    RESERVED("Reserved by a member"),
    LOST("Marked as lost");

    private final String description;

    ItemStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
