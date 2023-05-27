package com.rest.demo.validator;

import com.rest.demo.model.CustomerModel;
import com.rest.demo.validator.CustomerValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(SpringExtension.class)
public class CustomerValidatorTest {

    private final CustomerValidator customerValidator = new CustomerValidator();

    @Test
    public void validateRequest_givenCompleteRequest_thenDoNothing() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setName("John");
        customerModel.setAddress("Jakarta");
        customerModel.setBirthdate("2002-01-01");

        ValidationException validationException = Mockito.mock(ValidationException.class);
        verifyNoInteractions(validationException);
        customerValidator.validateRequest(customerModel);
        verifyNoInteractions(validationException);
    }

    @Test
    public void validateRequest_givenEmptyName_thenThrowValidationException() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setName("");
        customerModel.setAddress("Jakarta");
        customerModel.setBirthdate("2002-01-01");

        assertThrows(ValidationException.class, () -> {
            customerValidator.validateRequest(customerModel);
        });
    }

    @Test
    public void validateRequest_givenEmptyAddress_thenThrowValidationException() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setName("John");
        customerModel.setAddress("");
        customerModel.setBirthdate("2002-01-01");

        assertThrows(ValidationException.class, () -> {
            customerValidator.validateRequest(customerModel);
        });
    }

    @Test
    public void validateRequest_givenEmptyBirthdate_thenThrowValidationException() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setName("John");
        customerModel.setAddress("Jakarta");
        customerModel.setBirthdate("");

        assertThrows(ValidationException.class, () -> {
            customerValidator.validateRequest(customerModel);
        });
    }

    @Test
    public void testValidateId_WithNonNullId_NoExceptionThrown() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setId(1L);
        ValidationException validationException = Mockito.mock(ValidationException.class);
        verifyNoInteractions(validationException);
        customerValidator.validateId(customerModel);
        verifyNoInteractions(validationException);
    }

    @Test
    public void validateId_givenNullId_thenThrowValidationException() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setId(null);

        assertThrows(ValidationException.class, () -> {
            customerValidator.validateId(customerModel);
        });
    }
}

