package com.rest.demo.model;

import com.rest.demo.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerResponse<T> {
    private String status;
    private T data;
}
