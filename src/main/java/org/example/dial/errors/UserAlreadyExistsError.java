package org.example.dial.errors;

import org.example.dial.utils.Error;

public class UserAlreadyExistsError implements Error {
    private final String name;
    
    public UserAlreadyExistsError(String name) {
        this.name = name;
    }
    
	@Override
	public String getMessage() {
	    return "User " + this.name + " already exists";
	}
}