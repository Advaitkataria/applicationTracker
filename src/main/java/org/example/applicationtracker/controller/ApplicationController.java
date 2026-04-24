package org.example.applicationtracker.controller;

import jakarta.validation.Valid;
import org.example.applicationtracker.model.Application;
import org.example.applicationtracker.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    @Autowired
    public ApplicationController(ApplicationService applicationService){
        this.applicationService=applicationService;
    }

    @GetMapping
    public ResponseEntity<Page<Application>> getAllApplications(@RequestParam int page, @RequestParam int size){
        return new ResponseEntity<>(applicationService.getAllApplications(page, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Application> addApplication(@Valid @RequestBody Application application){
        return new ResponseEntity<>(applicationService.addApplication(application),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @applicationService.isOwner(#id)")
    public ResponseEntity<Void> deleteApplication(@PathVariable int id){
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/jobTitle/{jobTitle}")
    public ResponseEntity<List<Application>> getByJobTitle(@PathVariable String jobTitle){
        return new ResponseEntity<>(applicationService.getByJobTitleContaining(jobTitle),HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Application>> getByStatus(@PathVariable String status){
        return new ResponseEntity<>(applicationService.getByStatus(status),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Application> updateStatus(@PathVariable int id,@RequestBody Application updatedApplication){
        return new ResponseEntity<>(applicationService.updateApplication(id,updatedApplication),HttpStatus.OK);
    }



}
