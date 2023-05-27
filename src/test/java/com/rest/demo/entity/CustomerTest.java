package com.rest.demo.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTest {
    @Test
    public void testAllArgsConstructor() {
        Long id = 1L;
        String name = "John";
        String address = "Jakarta";
        LocalDate birthdate = LocalDate.now();
        Customer customer = new Customer(id, name, address, birthdate);

        assertEquals(id, customer.getId());
        assertEquals(name, customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals(birthdate, customer.getBirthdate());
    }
}
