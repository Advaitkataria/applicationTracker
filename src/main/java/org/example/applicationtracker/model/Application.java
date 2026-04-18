package org.example.applicationtracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="applications")
public class Application {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Company name cannot be blank")
    private String companyName;
    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;
    @NotBlank(message = "Status cannot be blank")
    private String status;
    @NotBlank(message = "Date cannot be blank")
    private LocalDate appliedDate;
    @NotBlank(message = "Notes cannot be blank")
    private String notes;
    @NotBlank(message = "Salary expectation cannot be blank")
    @Positive(message = "Salary expectation cannot be negative")
    private int salaryExpectation;
}
