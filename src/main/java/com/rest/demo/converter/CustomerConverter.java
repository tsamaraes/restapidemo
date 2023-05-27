package com.rest.demo.converter;

import com.rest.demo.entity.Customer;
import com.rest.demo.model.CustomerModel;
import com.rest.demo.model.CustomerResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerConverter {
    private final String pattern = "yyyy-MM-dd";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

    public Customer toEntity(CustomerModel model) {
        Customer customer = new Customer();
        customer.setId(model.getId() != null? model.getId() : null);
        customer.setName(model.getName());
        customer.setAddress(model.getAddress());
        LocalDate date = LocalDate.parse(model.getBirthdate(), dateFormatter);
        customer.setBirthdate(date);

        return customer;
    }

    public CustomerModel toModel(Customer customer) {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setId(customer.getId());
        customerModel.setName(customer.getName());
        customerModel.setAddress(customer.getAddress());
        String birthdate = customer.getBirthdate().format(dateFormatter);
        customerModel.setBirthdate(birthdate);

        return customerModel;
    }

    public CustomerResponse<CustomerModel> toResponse(String status, Customer customer) {
        CustomerResponse<CustomerModel> customerResponse = new CustomerResponse<CustomerModel>();
        customerResponse.setStatus(status);
        customerResponse.setData(toModel(customer));
        return customerResponse;
    }

    public CustomerResponse<List<CustomerModel>> toResponse(String status, List<Customer> customer) {
        CustomerResponse<List<CustomerModel>> customerResponse = new CustomerResponse<List<CustomerModel>>();
        customerResponse.setStatus(status);
        customerResponse.setData(customer.stream().map(this::toModel).collect(Collectors.toList()));
        return customerResponse;
    }
}
