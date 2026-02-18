package org.example.dial.errors;

import org.example.dial.utils.Error;

public class CannotSaveEntityError implements Error {
    private final String tableName;
    
    public CannotSaveEntityError(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getMessage() {
        return "Cannot save entity in " + this.tableName + " table.";
    }

}
