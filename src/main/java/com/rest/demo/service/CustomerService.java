package com.rest.demo.service;

import com.rest.demo.converter.CustomerConverter;
import com.rest.demo.entity.Customer;
import com.rest.demo.model.CustomerModel;
import com.rest.demo.repository.CustomerRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {
        Customer currentCustomer = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + customer.getId() + " is not found"));
        currentCustomer.setName(customer.getName());
        currentCustomer.setAddress(customer.getAddress());
        currentCustomer.setBirthdate(customer.getBirthdate());
        return customerRepository.save(currentCustomer);
    }

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + id + " is not found"));
    }

    public List<Customer> findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).getContent();
    }

    public Customer deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id: " + id + " not found"));
        customerRepository.deleteById(id);
        return customer;
    }
}
