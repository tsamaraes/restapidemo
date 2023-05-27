package com.rest.demo.model;

import com.rest.demo.entity.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerResponseTest {
    @Test
    public void testGetterSetter() {
        CustomerResponse<Customer> response = new CustomerResponse<>();
        Customer customer = new Customer();
        response.setStatus("success");
        response.setData(customer);
        assertEquals("success", response.getStatus());
        assertEquals(customer, response.getData());
    }
}

