package com.rest.demo.controller;

import com.rest.demo.converter.CustomerConverter;
import com.rest.demo.entity.Customer;
import com.rest.demo.model.CustomerModel;
import com.rest.demo.service.CustomerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InterfaceController {
    private final CustomerService customerService;
    private final CustomerController customerController;
    private final CustomerConverter customerConverter;

    public InterfaceController(CustomerService customerService, CustomerController customerController, CustomerConverter customerConverter) {
        this.customerService = customerService;
        this.customerController = customerController;
        this.customerConverter = customerConverter;
    }

    @GetMapping("")
    public String showData(Model model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Customer> customers = customerService.findAllCustomers(pageable);
        model.addAttribute("customers", customers);
        return "index"; // Thymeleaf template name (without extension)
    }

    @GetMapping("/customer/add")
    public String showCustomerForm(Model model) {
        model.addAttribute("customer", new CustomerModel()); // Initialize a new Customer object
        return "add-customer"; // Thymeleaf template name for the form
    }

    @PostMapping("/customer/save-add")
    public String saveCustomer(@ModelAttribute CustomerModel customerModel) {
        Customer customer = customerConverter.toEntity(customerModel);
        customerService.addCustomer(customer);
        return "redirect:/"; // Redirect to the customer list page
    }

    @GetMapping("/customer/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Customer existingCustomer = customerService.findCustomerById(id);
        model.addAttribute("customer", existingCustomer);

        return "update-customer";
    }

    @PostMapping("/customer/save-update")
    public String updateCustomer(@ModelAttribute CustomerModel customerModel) {
        Customer customer = customerConverter.toEntity(customerModel);
        customerService.updateCustomer(customer);
        return "redirect:/";
    }

    @GetMapping("/customer/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/";
    }
}
