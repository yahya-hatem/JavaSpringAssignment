package org.route;

/** Thrown for every business-rule failure in NileMart. */
public class StoreException extends RuntimeException {
    public StoreException(String message) {
        super(message);
    }
}
