package com.library.model;

/**
 * Interface representing the capability to renew a loan.
 * This is a contract about what an object can do, not about what it is.
 * Books and Magazines implement this; DVDs do not.
 * 
 * This allows DVDs to be perfectly ordinary catalogue items in every other respect
 * while remaining impossible to renew, without duplicating shared behaviour.
 */
public interface Renewable {
    /**
     * Attempts to renew the current loan.
     *
     * @return true if renewal succeeded, false if the renewal limit has been reached
     *         or the item is not currently on loan
     */
    boolean renewLoan();

    /**
     * Reports how many times this item type may be renewed in total.
     *
     * @return the renewal limit for this item type
     */
    int getRenewalLimit();

    /**
     * Reports how many renewals remain for the current loan.
     *
     * @return the number of renewals left, or -1 if not currently on loan
     */
    int getRenewalsRemaining();
}
