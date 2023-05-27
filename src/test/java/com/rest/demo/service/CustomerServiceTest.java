package com.rest.demo.service;

import com.rest.demo.converter.CustomerConverter;
import com.rest.demo.entity.Customer;
import com.rest.demo.repository.CustomerRepository;
import com.rest.demo.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    CustomerService customerService;
    Customer customer;
    List<Customer> customerList;

    @BeforeEach
    public void setUp(){
        customer = new Customer();
        customer.setName("John");
        customer.setBirthdate(LocalDate.now());
        customer.setAddress("Jakarta");
    }

    @Test
    public void addCustomer_givenValidCustomer_thenReturnCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer result = customerService.addCustomer(customer);

        assertNotNull(result);
        assertEquals(customer, result);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void updateCustomer_givenValidCustomer_thenReturnCustomer() {
        customer.setId(1L);
        Customer updatedCustomer = customer;
        updatedCustomer.setAddress("Bandung");
        when(customerRepository.findById(updatedCustomer.getId())).thenReturn(Optional.ofNullable(customer));
        when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);
        Customer result = customerService.updateCustomer(updatedCustomer);

        assertNotNull(result);
        assertEquals(customer, result);
        verify(customerRepository, times(1)).findById(updatedCustomer.getId());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void updateCustomer_givenNonExistingCustomer_thenThrowEntityNotFoundException() {
        Customer nonExistingCustomer = customer;
        nonExistingCustomer.setId(2L);
        nonExistingCustomer.setAddress("Bandung");
        when(customerRepository.findById(nonExistingCustomer.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomer(nonExistingCustomer));
        verify(customerRepository, times(1)).findById(nonExistingCustomer.getId());
        verify(customerRepository, never()).save(any());
    }

    @Test
    public void getCustomerById_givenValidId_thenReturnCustomer() {
        customer.setId(1L);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.ofNullable(customer));
        Customer result = customerService.findCustomerById(customer.getId());

        assertNotNull(result);
        assertEquals(customer, result);
        verify(customerRepository, times(1)).findById(customer.getId());
    }

    @Test
    public void getCustomerById_givenInvalidId_thenThrowEntityNotFoundException() {
        when(customerRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.findCustomerById(10L));
        verify(customerRepository, times(1)).findById(10L);
    }

    @Test
    public void findAllCustomers_givenCertainPageSize_thenReturnCustomers() {
        customer.setId(1L);
        Customer customer2 = customer;
        customer2.setId(2L);
        Customer customer3 = customer;
        customer3.setId(3L);
        Customer customer4 = customer;
        customer4.setId(4L);
        customerList = new ArrayList<>();
        customerList.add(customer);
        customerList.add(customer2);
        customerList.add(customer3);
        customerList.add(customer4);

        List<Customer> customerListPage0 = customerList.subList(0,2);
        Pageable page = PageRequest.of(0, 2);
        Page<Customer> customerPage = new PageImpl<>(customerListPage0);

        when(customerRepository.findAll(page)).thenReturn(customerPage);
        List<Customer> result = customerService.findAllCustomers(page);

        assertNotNull(result);
        assertEquals(customerListPage0.size(), result.size());
        assertTrue(result.contains(customer));
        assertTrue(result.contains(customer2));
        verify(customerRepository, times(1)).findAll(page);
    }

    @Test
    void deleteCustomer_givenValidId_thenDeleteCustomerAndReturnCustomer() {
        customer.setId(1L);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        Customer result = customerService.deleteCustomer(customer.getId());

        assertNotNull(result);
        assertEquals(customer, result);
        verify(customerRepository, times(1)).findById(customer.getId());
        verify(customerRepository, times(1)).deleteById(customer.getId());
    }

    @Test
    void deleteCustomer_givenInvalidId_thenThrowEntityNotFoundException() {
        when(customerRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(10L));
        verify(customerRepository, times(1)).findById(10L);
        verify(customerRepository, never()).deleteById(10L);
    }
}
