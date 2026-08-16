package com.library.model;

import javax.persistence.*;

/**
 * Represents a DVD in the library.
 * 
 * DVDs:
 * - May be kept 3 days
 * - Cost 15.00 EGP per overdue day, without limit
 * - Are NOT renewable
 * 
 * DVDs demonstrate a key design principle: they are perfectly ordinary catalogue items
 * in every other respect while remaining impossible to renew, without duplicating
 * shared behaviour. This is achieved by NOT implementing the Renewable interface.
 */
@Entity
@DiscriminatorValue("DVD")
public class DVD extends LibraryItem {
    @Column(nullable = false)
    private int runtime;

    private static final int LOAN_PERIOD_DAYS = 3;
    private static final double DAILY_FINE_RATE = 15.00;

    public DVD() {
        super();
    }

    public DVD(String catalogueId, String title, int runtime,
               String libraryName, double administrativeCharge, int totalItemsCatalogued) {
        super(catalogueId, title, libraryName, administrativeCharge, totalItemsCatalogued);
        this.runtime = runtime;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
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
        return "DVD";
    }
}
