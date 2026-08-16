package com.library.model;

import javax.persistence.*;

/**
 * Represents a Magazine in the library.
 * 
 * Magazines:
 * - May be kept 7 days
 * - Cost 3.00 EGP per overdue day, but the total stops at 30.00 EGP however long it is kept
 * - Are renewable once
 * 
 * The magazine's fine cap is the single most instructive aspect of the design.
 * Where the book and the DVD differ only by a number, the magazine differs by shape.
 * Its rule contains a maximum and therefore cannot be expressed as "the daily rate times the days".
 * A centralized fine calculation would need a special case here.
 * This design does not notice, because it never assumed the rules were related in the first place.
 */
@Entity
@DiscriminatorValue("MAGAZINE")
public class Magazine extends LibraryItem implements Renewable {
    @Column(nullable = false)
    private int issueNumber;

    @Column(nullable = false)
    private int renewalsRemaining;

    private static final int LOAN_PERIOD_DAYS = 7;
    private static final double DAILY_FINE_RATE = 3.00;
    private static final double MAXIMUM_FINE = 30.00;
    private static final int RENEWAL_LIMIT = 1;

    public Magazine() {
        super();
    }

    public Magazine(String catalogueId, String title, int issueNumber,
                    String libraryName, double administrativeCharge, int totalItemsCatalogued) {
        super(catalogueId, title, libraryName, administrativeCharge, totalItemsCatalogued);
        this.issueNumber = issueNumber;
        this.renewalsRemaining = RENEWAL_LIMIT;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    @Override
    public double calculateOverdueFine(int daysOverdue) {
        double fine = daysOverdue * DAILY_FINE_RATE;
        return Math.min(fine, MAXIMUM_FINE);
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_PERIOD_DAYS;
    }

    @Override
    public String getCategory() {
        return "Magazine";
    }

    // Renewable implementation
    @Override
    public boolean renewLoan() {
        if (this.getStatus() != ItemStatus.ON_LOAN) {
            return false;
        }
        if (this.renewalsRemaining <= 0) {
            return false;
        }
        this.renewalsRemaining--;
        return true;
    }

    @Override
    public int getRenewalLimit() {
        return RENEWAL_LIMIT;
    }

    @Override
    public int getRenewalsRemaining() {
        if (this.getStatus() != ItemStatus.ON_LOAN) {
            return -1;
        }
        return this.renewalsRemaining;
    }

    /**
     * Override to reset renewalsRemaining when item is taken back.
     */
    @Override
    public void bringBack() {
        super.bringBack();
        this.renewalsRemaining = RENEWAL_LIMIT;
    }
}
