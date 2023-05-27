package com.rest.demo.controller;

import com.rest.demo.controller.CustomerController;
import com.rest.demo.converter.CustomerConverter;
import com.rest.demo.entity.Customer;
import com.rest.demo.enums.ResponseEnum;
import com.rest.demo.model.CustomerModel;
import com.rest.demo.model.CustomerResponse;
import com.rest.demo.model.ErrorResponse;
import com.rest.demo.service.CustomerService;
import com.rest.demo.validator.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerConverter customerConverter;

    @Mock
    private CustomerValidator customerValidator;

    @InjectMocks
    private CustomerController customerController;
    CustomerModel customerModel;
    Customer customer;
    CustomerResponse<CustomerModel> customerResponse;
    CustomerResponse<List<CustomerModel>> listCustomerResponse;
    ErrorResponse errorResponse;
    List<Customer> customerList;

    @BeforeEach
    public void setup() {
        customerModel = new CustomerModel();
        customerModel.setName("John");
        customerModel.setBirthdate("2001-01-01");
        customerModel.setAddress("Jakarta");

        customer = new Customer();
        customer.setName("John");
        customer.setBirthdate(LocalDate.now());
        customer.setAddress("Jakarta");

        customerResponse = new CustomerResponse<>();
        customerResponse.setStatus("Success");
        listCustomerResponse = new CustomerResponse<>();
        listCustomerResponse.setStatus("Success");
        errorResponse = new ErrorResponse();
        errorResponse.setStatus("Failed");
    }

    @Test
    public void createCustomer_givenValidRequest_thenReturnCreatedStatus() {
        customer.setId(1L);
        customerResponse.setStatus("Success");
        customerResponse.setData(customerModel);
        doNothing().when(customerValidator).validateRequest(customerModel);
        when(customerConverter.toEntity(customerModel)).thenReturn(customer);
        when(customerConverter.toResponse(ResponseEnum.SUCCESS.getMessage(), customer)).thenReturn(customerResponse);
        when(customerService.addCustomer(customer)).thenReturn(customer);

        ResponseEntity<?> response = customerController.createCustomer(customerModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(customerValidator, times(1)).validateRequest(customerModel);
        verify(customerConverter, times(1)).toEntity(customerModel);
        verify(customerConverter, times(1)).toResponse(ResponseEnum.SUCCESS.getMessage(), customer);
        verify(customerService, times(1)).addCustomer(customer);
    }

    @Test
    public void createCustomer_givenInvalidRequest_thenReturnBadRequestStatus() {
        customer.setId(1L);
        customerModel.setName("");

        String errorMessage = "name must not be empty";
        ValidationException validationException = new ValidationException(errorMessage);
        errorResponse.setMessage(errorMessage);

        doThrow(validationException).when(customerValidator).validateRequest(customerModel);

        ResponseEntity<?> response = customerController.createCustomer(customerModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void createCustomer_givenInvalidRequest_thenReturnInternalServerErrorStatus() {
        customer.setId(1L);
        customerModel.setName("");

        String errorMessage = "Something went wrong";
        RuntimeException exception = new RuntimeException(errorMessage);
        errorResponse.setMessage(errorMessage);

        doThrow(exception).when(customerValidator).validateRequest(customerModel);

        ResponseEntity<?> response = customerController.createCustomer(customerModel);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void getAllCustomers_givenPage_thenReturnCustomersBasedOnPage() {
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

        when(customerService.findAllCustomers(page)).thenReturn(customerListPage0);

        ResponseEntity<?> response = customerController.getAllCustomers(1,2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerConverter, times(1)).toResponse(ResponseEnum.SUCCESS.getMessage(), customerListPage0);
        verify(customerService, times(1)).findAllCustomers(page);
    }

    @Test
    public void getAllCustomers_givenException_thenReturnInternalServerError() {
        Pageable page = PageRequest.of(0, 2);
        String errorMessage = "Something went wrong";
        RuntimeException exception = new RuntimeException(errorMessage);
        errorResponse.setMessage(errorMessage);

        when(customerService.findAllCustomers(page)).thenThrow(exception);
        ResponseEntity<?> response = customerController.getAllCustomers(1,2);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void getCustomerById_givenValidId_thenReturnCustomer() {
        customer.setId(1L);

        customerResponse.setStatus("Success");
        customerResponse.setData(customerModel);

        when(customerConverter.toResponse(ResponseEnum.SUCCESS.getMessage(), customer)).thenReturn(customerResponse);
        when(customerService.findCustomerById(1L)).thenReturn(customer);

        ResponseEntity<?> response = customerController.getCustomerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerConverter, times(1)).toResponse(ResponseEnum.SUCCESS.getMessage(), customer);
        verify(customerService, times(1)).findCustomerById(1L);
    }

    @Test
    public void createCustomer_givenInvalidId_thenReturnNotFoundStatus() {
        String errorMessage = "Customer with id 2 is not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);
        errorResponse.setMessage(errorMessage);

        when(customerService.findCustomerById(2L)).thenThrow(exception);

        ResponseEntity<?> response = customerController.getCustomerById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void createCustomer_givenException_thenReturnInternalServerErrorStatus() {
        String errorMessage = "Something went wrong";
        RuntimeException exception = new RuntimeException(errorMessage);
        errorResponse.setMessage(errorMessage);

        when(customerService.findCustomerById(2L)).thenThrow(exception);

        ResponseEntity<?> response = customerController.getCustomerById(2L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void updateCustomer_givenValidRequest_thenReturnCreatedStatus() {
        customer.setId(1L);
        customerModel.setId(1L);
        customerResponse.setStatus("Success");
        customerResponse.setData(customerModel);
        doNothing().when(customerValidator).validateRequest(customerModel);
        doNothing().when(customerValidator).validateId(customerModel);
        when(customerConverter.toEntity(customerModel)).thenReturn(customer);
        when(customerConverter.toResponse(ResponseEnum.SUCCESS.getMessage(), customer)).thenReturn(customerResponse);
        when(customerService.updateCustomer(customer)).thenReturn(customer);

        ResponseEntity<?> response = customerController.updateCustomer(customerModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerValidator, times(1)).validateRequest(customerModel);
        verify(customerConverter, times(1)).toEntity(customerModel);
        verify(customerConverter, times(1)).toResponse(ResponseEnum.SUCCESS.getMessage(), customer);
        verify(customerService, times(1)).updateCustomer(customer);
    }

    @Test
    public void updateCustomer_givenEmptyId_thenReturnBadRequestStatus() {
        String errorMessage = "id must not be empty";
        ValidationException validationException = new ValidationException(errorMessage);
        errorResponse.setMessage(errorMessage);

        doThrow(validationException).when(customerValidator).validateId(customerModel);

        ResponseEntity<?> response = customerController.updateCustomer(customerModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void updateCustomer_givenInvalidRequest_thenReturnInternalServerErrorStatus() {
        customer.setId(1L);
        customerModel.setName("");

        String errorMessage = "Something went wrong";
        RuntimeException exception = new RuntimeException(errorMessage);
        errorResponse.setMessage(errorMessage);

        doThrow(exception).when(customerValidator).validateId(customerModel);

        ResponseEntity<?> response = customerController.updateCustomer(customerModel);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void updateCustomer_givenInvalidId_thenReturnNotFoundStatus() {
        customerModel.setId(2L);
        customer.setId(2L);

        doNothing().when(customerValidator).validateId(customerModel);
        doNothing().when(customerValidator).validateRequest(customerModel);
        when(customerConverter.toEntity(customerModel)).thenReturn(customer);
        String errorMessage = "Customer with id: 2 is not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);
        errorResponse.setMessage(errorMessage);

        when(customerService.updateCustomer(customer)).thenThrow(exception);

        ResponseEntity<?> response = customerController.updateCustomer(customerModel);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void deleteCustomer_givenValidId_thenReturnCustomer() {
        customer.setId(1L);
        customerResponse.setStatus("Success");
        customerResponse.setData(customerModel);

        when(customerConverter.toResponse(ResponseEnum.SUCCESS.getMessage(), customer)).thenReturn(customerResponse);
        when(customerService.deleteCustomer(customer.getId())).thenReturn(customer);

        ResponseEntity<?> response = customerController.deleteCustomer(customer.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerConverter, times(1)).toResponse(ResponseEnum.SUCCESS.getMessage(), customer);
        verify(customerService, times(1)).deleteCustomer(customer.getId());
    }

    @Test
    public void deleteCustomer_givenInvalidId_thenReturnNotFoundStatus() {
        String errorMessage = "Customer with id: 2 is not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);
        errorResponse.setMessage(errorMessage);

        when(customerService.deleteCustomer(2L)).thenThrow(exception);

        ResponseEntity<?> response = customerController.deleteCustomer(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }

    @Test
    public void deleteCustomer_givenException_thenReturnInternalServerErrorStatus() {
        String errorMessage = "Customer with id: 2 is not found";
        RuntimeException exception = new RuntimeException(errorMessage);
        errorResponse.setMessage(errorMessage);

        when(customerService.deleteCustomer(2L)).thenThrow(exception);

        ResponseEntity<?> response = customerController.deleteCustomer(2L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorResponse.getMessage(), errorMessage);
    }
}
