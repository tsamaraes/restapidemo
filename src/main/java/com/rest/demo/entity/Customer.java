package com.rest.demo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    private Long id;
    private String name;
    private String address;
    private LocalDate birthdate;

    public void setId(Long id) {
        this.id = id;
    }

    @javax.persistence.Id
    @GeneratedValue
    public Long getId() {
        return id;
    }
}
