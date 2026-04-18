package org.example.applicationtracker.controller;

import org.example.applicationtracker.model.Application;
import org.example.applicationtracker.service.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    public ApplicationController(ApplicationService applicationService){
        this.applicationService=applicationService;
    }

    @GetMapping
    public ResponseEntity<Page<Application>> getAllApplications(@RequestParam int page, @RequestParam int size){
        return new ResponseEntity<>(applicationService.getAllApplications(page, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Application> addApplication(@RequestBody Application application){
        return new ResponseEntity<>(applicationService.addApplication(application),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
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

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Application>> getByDate(@PathVariable LocalDate appliedDate){
        return new ResponseEntity<>(applicationService.getByAppliedDate(appliedDate),HttpStatus.OK);
    }

    @GetMapping("/filter/{companyName}/{appliedDate}")
    public ResponseEntity<List<Application>> getByCompanyNameAndAppliedDate(@PathVariable String companyName, @PathVariable LocalDate appliedDate){
        return new ResponseEntity<>(applicationService.getByCompanyNameAndAppliedDate(companyName,appliedDate),HttpStatus.OK);
    }

}
