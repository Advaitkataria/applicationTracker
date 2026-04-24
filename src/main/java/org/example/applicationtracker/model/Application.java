package org.example.applicationtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Company name cannot be blank")
    private String companyName;
    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;
    @Pattern(regexp = "Applied|Offer|Rejected|Interviewing")
    @NotBlank(message = "Status cannot be blank")
    private String status;
    @NotNull(message = "Date cannot be blank")
    private LocalDate appliedDate;
    @NotBlank(message = "Notes cannot be blank")
    private String notes;
    @NotNull(message = "Salary expectation cannot be blank")
    @Positive(message = "Salary expectation cannot be negative")
    private int salaryExpectation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;
}
