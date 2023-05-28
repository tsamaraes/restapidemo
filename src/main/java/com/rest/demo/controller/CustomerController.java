package com.rest.demo.controller;

import com.rest.demo.enums.ResponseEnum;
import com.rest.demo.converter.CustomerConverter;
import com.rest.demo.entity.Customer;
import com.rest.demo.model.CustomerModel;
import com.rest.demo.model.ErrorResponse;
import com.rest.demo.service.CustomerService;
import com.rest.demo.validator.CustomerValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerConverter customerConverter;
    private final CustomerValidator customerValidator;

    public CustomerController(CustomerService customerService, CustomerConverter customerConverter, CustomerValidator customerValidator) {
        this.customerService = customerService;
        this.customerConverter = customerConverter;
        this.customerValidator = customerValidator;
    }

    @PostMapping("")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerModel customerModel) {
        try {
            customerValidator.validateRequest(customerModel);
            Customer customer = customerConverter.toEntity(customerModel);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(customerConverter
                            .toResponse(ResponseEnum.SUCCESS.getMessage(), customerService.addCustomer(customer)));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCustomers(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(customerConverter
                            .toResponse(ResponseEnum.SUCCESS.getMessage(), customerService.findAllCustomers(pageable)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") Long id) {
        try {
            Customer customer = customerService.findCustomerById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(customerConverter.toResponse(ResponseEnum.SUCCESS.getMessage(), customer));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerModel customerModel) {
        try {
            customerValidator.validateId(customerModel);
            customerValidator.validateRequest(customerModel);
            Customer customer = customerConverter.toEntity(customerModel);
            Customer updatedCustomer = customerService.updateCustomer(customer);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(customerConverter.toResponse(ResponseEnum.SUCCESS.getMessage(), updatedCustomer));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(customerConverter.toResponse(ResponseEnum.SUCCESS.getMessage(), customerService.deleteCustomer(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ResponseEnum.FAILED.getMessage(), e.getMessage()));
        }
    }
}
