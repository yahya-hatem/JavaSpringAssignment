package com.library.exception;

public class ItemNotFoundException extends LibraryException {
    public ItemNotFoundException(String catalogueId) {
        super("Item with catalogue ID '" + catalogueId + "' not found");
    }
}
