package com.library.model;

import javax.persistence.*;

/**
 * Represents a library member.
 * 
 * Holds: a name, a membership ID, a category, a balance owed, and a count of items currently held.
 * 
 * Refuses: to let anyone set the balance or the item count directly.
 * Both numbers move only through controlled paths to ensure integrity.
 */
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String membershipId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipType category;

    @Column(nullable = false)
    private double balanceOwed;

    @Column(nullable = false)
    private int itemsHeld;

    // Constants for borrowing eligibility
    private static final int MAX_ITEMS_HELD = 3;
    private static final double MAX_BALANCE_TO_BORROW = 100.0;

    public Member() {
        this.balanceOwed = 0.0;
        this.itemsHeld = 0;
    }

    public Member(String membershipId, String name, MembershipType category) {
        this();
        this.membershipId = membershipId;
        this.name = name;
        this.category = category;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public String getName() {
        return name;
    }

    public MembershipType getCategory() {
        return category;
    }

    public double getBalanceOwed() {
        return balanceOwed;
    }

    public int getItemsHeld() {
        return itemsHeld;
    }

    /**
     * Sets the member's name. The membership ID and category never change.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Charges a fine to this member.
     * Rejects any amount that is not positive.
     *
     * @param amount the fine amount to charge
     * @return true if the charge was successful, false if rejected
     */
    public boolean chargeFine(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balanceOwed += amount;
        return true;
    }

    /**
     * Accepts a payment from this member.
     * Rejects any amount that is not positive.
     * Rejects any amount larger than the balance owed.
     *
     * @param amount the payment amount
     * @return true if the payment was successful, false if rejected
     */
    public boolean acceptPayment(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > this.balanceOwed) {
            return false;
        }
        this.balanceOwed -= amount;
        return true;
    }

    /**
     * Checks whether this member is currently eligible to borrow.
     * A member may borrow only if they hold fewer than three items and owe no more than 100 EGP.
     *
     * @return true if the member can borrow, false otherwise
     */
    public boolean isEligibleToBorrow() {
        return this.itemsHeld < MAX_ITEMS_HELD && this.balanceOwed <= MAX_BALANCE_TO_BORROW;
    }

    /**
     * Records that this member is borrowing an item.
     * Increases the item count by one.
     */
    public void recordBorrowing() {
        this.itemsHeld++;
    }

    /**
     * Records that this member is returning an item.
     * Decreases the item count by one, refusing to go below zero.
     *
     * @return true if the return was recorded, false if the count would go negative
     */
    public boolean recordReturn() {
        if (this.itemsHeld <= 0) {
            return false;
        }
        this.itemsHeld--;
        return true;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Category: %s | Items: %d | Balance: %.2f EGP",
                membershipId, name, category.getDisplayName(), itemsHeld, balanceOwed);
    }
}
