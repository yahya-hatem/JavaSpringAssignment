package com.library.model;

import javax.persistence.*;

/**
 * Abstract base class for all library items.
 * 
 * The core design principle: items are similar enough to be handled together,
 * but different enough that each must be allowed to answer for itself.
 * 
 * Every item can state how long it may be kept and what it costs to return late,
 * but no two item types answer either question the same way.
 * 
 * Cannot be instantiated directly. Subclasses must provide answers to three questions:
 * 1. What does it cost to return this item a given number of days late?
 * 2. How many days may this type be kept?
 * 3. What is this item type called?
 */
@Entity
@Table(name = "library_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
public abstract class LibraryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String catalogueId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private String borrowerName;

    @Column(nullable = false)
    private int renewalCount;

    // Library-wide information
    @Column(nullable = false)
    private String libraryName;

    @Column(nullable = false)
    private double administrativeCharge;

    @Column(nullable = false)
    private int totalItemsCatalogued;

    public LibraryItem() {
        this.status = ItemStatus.AVAILABLE;
        this.renewalCount = 0;
    }

    public LibraryItem(String catalogueId, String title, String libraryName, 
                       double administrativeCharge, int totalItemsCatalogued) {
        this();
        this.catalogueId = catalogueId;
        this.title = title;
        this.libraryName = libraryName;
        this.administrativeCharge = administrativeCharge;
        this.totalItemsCatalogued = totalItemsCatalogued;
    }

    // Abstract methods that every subclass must answer
    /**
     * Calculates the fine for returning this item a given number of days late.
     *
     * @param daysOverdue the number of days the item is overdue
     * @return the fine amount in EGP
     */
    public abstract double calculateOverdueFine(int daysOverdue);

    /**
     * Returns how many days this type of item may be kept.
     *
     * @return the loan period in days
     */
    public abstract int getLoanPeriodDays();

    /**
     * Returns the category name for this item type (Book, Magazine, DVD).
     *
     * @return the category name
     */
    public abstract String getCategory();

    // Getters
    public Long getId() {
        return id;
    }

    public String getCatalogueId() {
        return catalogueId;
    }

    public String getTitle() {
        return title;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public double getAdministrativeCharge() {
        return administrativeCharge;
    }

    public int getTotalItemsCatalogued() {
        return totalItemsCatalogued;
    }

    /**
     * Marks this item as reserved.
     * The item must be available to be reserved.
     *
     * @return true if the reservation was successful, false if item is not available
     */
    public boolean markReserved() {
        if (this.status != ItemStatus.AVAILABLE) {
            return false;
        }
        this.status = ItemStatus.RESERVED;
        return true;
    }

    /**
     * Marks this item as lost.
     *
     * @return true if the item was successfully marked lost
     */
    public boolean markLost() {
        this.status = ItemStatus.LOST;
        return true;
    }

    /**
     * Brings this item back to available status.
     * Used when a reserved or lost item is being restored.
     */
    public void bringBack() {
        this.status = ItemStatus.AVAILABLE;
        this.borrowerName = null;
        this.renewalCount = 0;
    }

    /**
     * Lends this item to a member.
     * Only possible if the item is currently available.
     *
     * @param memberName the name of the member borrowing this item
     * @return true if the loan was successful, false if item is not available
     */
    public boolean lendTo(String memberName) {
        if (this.status != ItemStatus.AVAILABLE) {
            return false;
        }
        this.status = ItemStatus.ON_LOAN;
        this.borrowerName = memberName;
        this.renewalCount = 0;
        return true;
    }

    /**
     * Takes this item back from a member.
     * Resets the state, clears the borrower, and resets the renewal count.
     * This behaviour cannot be overridden by subclasses.
     * It is load-bearing: if an item could override this and forget to reset the state,
     * it would remain permanently on loan, quietly and forever.
     */
    public final void takeBack() {
        this.status = ItemStatus.AVAILABLE;
        this.borrowerName = null;
        this.renewalCount = 0;
    }

    /**
     * Records a renewal against the current loan.
     *
     * @return true if the renewal was recorded, false if not on loan
     */
    public boolean recordRenewal() {
        if (this.status != ItemStatus.ON_LOAN) {
            return false;
        }
        this.renewalCount++;
        return true;
    }

    /**
     * Returns a one-line display of this item's details.
     * All item types display identically via this shared method,
     * which calls the three demanded abstract methods.
     * At runtime, the object is never a bare LibraryItem; it is always some specific kind.
     *
     * @return a formatted single-line representation
     */
    @Override
    public String toString() {
        double oneDayFine = calculateOverdueFine(1);
        return String.format("ID: %-8s | Category: %-10s | Title: %-30s | Status: %-10s | " +
                        "Borrower: %-15s | Loan: %d days | 1-day fine: %.2f EGP",
                catalogueId, getCategory(), title, status, 
                borrowerName != null ? borrowerName : "-", getLoanPeriodDays(), oneDayFine);
    }
}
