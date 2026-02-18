package org.example.dial.errors;

import org.example.dial.utils.Error;

public class ContactAlreadyExistsError implements Error {
    private final String contactName;
    private final String ownerName;

    public ContactAlreadyExistsError(String ownerName, String contactName) {
        this.contactName = contactName;
        this.ownerName = ownerName;
    }
    
    @Override
    public String getMessage() {
        return "In user " + this.ownerName + " contact " + this.contactName + " already exists";
    }
    
}
