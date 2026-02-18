package org.example.dial.errors;

import org.example.dial.utils.Error;

public class UserNotFoundError implements Error {
    private final String ident;
    public UserNotFoundError(String ident) {
        this.ident = ident;
    }
    @Override
    public String getMessage() {
        return "User " + this.ident + " not found";
    }
}
