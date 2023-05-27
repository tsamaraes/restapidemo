package com.rest.demo.validator;

import com.rest.demo.model.CustomerModel;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;

@Component
public class CustomerValidator {
    public void validateRequest(CustomerModel customerModel) throws ValidationException {
        if (customerModel.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (customerModel.getAddress().isEmpty()) {
            throw new ValidationException("address must not be empty");
        }
        if (customerModel.getBirthdate().isEmpty()) {
            throw new ValidationException("birthdate must not be empty");
        }
    }

    public void validateId(CustomerModel customerModel) {
        if (customerModel.getId() == null) {
            throw new ValidationException("id must not be empty");
        }
    }
}
