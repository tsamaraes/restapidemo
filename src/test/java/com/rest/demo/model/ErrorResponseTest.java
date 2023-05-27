package com.rest.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {
    @Test
    public void testGetterSetter() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus("failed");
        response.setMessage("something went wrong");
        assertEquals("failed", response.getStatus());
        assertEquals("something went wrong", response.getMessage());
    }
}
