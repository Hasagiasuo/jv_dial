package org.example.dial.utils;

public class Result<T> {
    private T value;
    private Error error;
    private final boolean isSuccess;

    public Result(T value) {
        this.isSuccess = true;
        this.value = value;
    }

    public Result(Error error) {
        this.isSuccess = false;
        this.error = error;
    }

    public T getValue() { return this.value; }
    public Error getError() { return this.error; }
    public boolean isSuccess() { return this.isSuccess; }
}
