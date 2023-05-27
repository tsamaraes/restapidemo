package com.rest.demo.converter;

import com.rest.demo.entity.Customer;
import com.rest.demo.model.CustomerModel;
import com.rest.demo.model.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class CustomerConverterTest {

    private final CustomerConverter converter = new CustomerConverter();

    @Test
    public void toEntity_givenCustomerModelWithId_thenConvertToCustomer() {
        Long id = 1L;
        String name = "John";
        String address = "Jakarta";
        String birthdate = "2001-01-01";
        CustomerModel model = new CustomerModel(id, name, address, birthdate);
        Customer customer = converter.toEntity(model);

        assertEquals(id, customer.getId());
        assertEquals(name, customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals(LocalDate.parse(birthdate), customer.getBirthdate());
    }

    @Test
    public void toEntity_givenCustomerModelWithoutId_thenConvertToCustomer() {
        String name = "John";
        String address = "Jakarta";
        String birthdate = "2001-01-01";
        CustomerModel model = new CustomerModel();
        model.setName(name);
        model.setBirthdate(birthdate);
        model.setAddress(address);
        Customer customer = converter.toEntity(model);

        assertEquals(name, customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals(LocalDate.parse(birthdate), customer.getBirthdate());
    }

    @Test
    public void testCustomerEntityToCustomerModel() {
        Long id = 1L;
        String name = "John";
        String address = "Jakarta";
        LocalDate birthdate = LocalDate.now();
        Customer customer = new Customer(id, name, address, birthdate);
        CustomerModel model = converter.toModel(customer);

        assertEquals(id, model.getId());
        assertEquals(name, model.getName());
        assertEquals(address, model.getAddress());
        assertEquals(birthdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), model.getBirthdate());
    }

    @Test
    public void testToResponseWithSingleCustomer() {
        Long id = 1L;
        String name = "John";
        String address = "Jakarta";
        LocalDate birthdate = LocalDate.now();
        Customer customer = new Customer(id, name, address, birthdate);


        String status = "success";
        CustomerResponse<CustomerModel> response = converter.toResponse(status, customer);

        // Verify the response status and data
        assertEquals(status, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(id, response.getData().getId());
        assertEquals(name, response.getData().getName());
        assertEquals(address, response.getData().getAddress());
        assertEquals(birthdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), response.getData().getBirthdate());
    }

    @Test
    public void testToResponseWithCustomerList() {
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "John", "Jakarta", LocalDate.now()),
                new Customer(2L, "Jane", "Jakarta", LocalDate.now())
        );

        String status = "Success";
        CustomerResponse<List<CustomerModel>> response = converter.toResponse(status, customers);

        assertEquals(status, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(customers.size(), response.getData().size());

        for (int i = 0; i < customers.size(); i++) {
            CustomerModel expectedModel = converter.toModel(customers.get(i));
            CustomerModel actualModel = response.getData().get(i);
            assertEquals(expectedModel.getId(), actualModel.getId());
            assertEquals(expectedModel.getName(), actualModel.getName());
            assertEquals(expectedModel.getAddress(), actualModel.getAddress());
            assertEquals(expectedModel.getBirthdate(), actualModel.getBirthdate());
        }
    }
}