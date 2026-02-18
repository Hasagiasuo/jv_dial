package org.example.dial.errors;

import org.example.dial.utils.Error;

public class ContactNotFoundError implements Error {
    private final String ownerName;
    private final String contactName;

    public ContactNotFoundError(String ownerName, String contactName) {
        this.ownerName = ownerName;
        this.contactName = contactName;
    }

    @Override
    public String getMessage() {
        return "User " + this.ownerName + " havent contact " + this.contactName + ".";
    }
}
