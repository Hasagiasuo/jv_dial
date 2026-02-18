package org.example.dial.errors;

import org.example.dial.utils.Error;

public class PasswordUncorrectError implements Error {
    @Override
    public String getMessage() {
        return "Password uncorrect";
    }
    
}
