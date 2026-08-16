package com.library.exception;

public class MemberNotFoundException extends LibraryException {
    public MemberNotFoundException(String membershipId) {
        super("Member with ID '" + membershipId + "' not found");
    }
}
