package com.library.model;

import javax.persistence.*;

/**
 * Represents a Book in the library.
 * 
 * Books:
 * - May be kept 14 days
 * - Cost 5.00 EGP per overdue day, without limit
 * - Are renewable up to twice
 */
@Entity
@DiscriminatorValue("BOOK")
public class Book extends LibraryItem implements Renewable {
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private int pageCount;

    @Column(nullable = false)
    private int renewalsRemaining;

    private static final int LOAN_PERIOD_DAYS = 14;
    private static final double DAILY_FINE_RATE = 5.00;
    private static final int RENEWAL_LIMIT = 2;

    public Book() {
        super();
    }

    public Book(String catalogueId, String title, String author, int pageCount,
                String libraryName, double administrativeCharge, int totalItemsCatalogued) {
        super(catalogueId, title, libraryName, administrativeCharge, totalItemsCatalogued);
        this.author = author;
        this.pageCount = pageCount;
        this.renewalsRemaining = RENEWAL_LIMIT;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public double calculateOverdueFine(int daysOverdue) {
        return daysOverdue * DAILY_FINE_RATE;
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_PERIOD_DAYS;
    }

    @Override
    public String getCategory() {
        return "Book";
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
