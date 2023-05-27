package com.rest.demo.enums;

public enum ResponseEnum {
    SUCCESS("Success"),
    FAILED("Failed");

    private final String message;

    private ResponseEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
