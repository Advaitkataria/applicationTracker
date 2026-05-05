package org.example.applicationtracker.controller;

import jakarta.validation.Valid;
import org.example.applicationtracker.model.Interview;
import org.example.applicationtracker.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications/{applicationId}/interviews")
public class InterviewController {
    private final InterviewService interviewService;

    @Autowired
    public InterviewController(InterviewService interviewService){
        this.interviewService=interviewService;
    }

    @GetMapping
    public ResponseEntity<List<Interview>> getInterviews(@PathVariable Integer applicationId){
        return new ResponseEntity<>(interviewService.getInterviewsByApplicationId(applicationId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Interview> addInterview(@PathVariable Integer applicationId,@Valid @RequestBody Interview interview){
        return new ResponseEntity<>(interviewService.addInterview(applicationId,interview),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Interview> updateInterview(@PathVariable Integer applicationId,@Valid @RequestBody Interview interview){
        return new ResponseEntity<>(interviewService.updateInterview(applicationId,interview),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Integer id){
        interviewService.deleteInterview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
