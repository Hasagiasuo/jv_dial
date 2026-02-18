package org.example.dial.utils;

public class Option {
    private Error error;
    private final boolean isSuccess;
    
    public Option(Error error) {
        this.error = error;
        this.isSuccess = false;
    }

    public Option() {
        this.error = null;
        this.isSuccess = true;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public Error getError() {
        return this.error;
    }
}
