package org.example.applicationtracker.service;

import org.example.applicationtracker.exception.ApplicationNotFoundException;
import org.example.applicationtracker.model.Application;
import org.example.applicationtracker.model.Interview;
import org.example.applicationtracker.model.User;
import org.example.applicationtracker.repository.ApplicationRepository;
import org.example.applicationtracker.repository.InterviewRepository;
import org.example.applicationtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {
    private InterviewRepository interviewRepository;
    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;

    @Autowired
    public InterviewService(InterviewRepository interviewRepository,ApplicationRepository applicationRepository,UserRepository userRepository){
        this.interviewRepository=interviewRepository;
        this.applicationRepository=applicationRepository;
        this.userRepository=userRepository;
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    }

    private Application getApplicationAndVerifyOwner(Integer applicationId){
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->new ApplicationNotFoundException(applicationId));
        if(!application.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can access only your applications");
        }
        return application;
    }

    public List<Interview> getInterviewsByApplicationId(Integer applicationId){
        getApplicationAndVerifyOwner(applicationId);
        return interviewRepository.findByApplicationId(applicationId);
    }

    public Interview addInterview(Integer applicationId,Interview interview){
        Application application = getApplicationAndVerifyOwner(applicationId);
        interview.setApplication(application);
        return interviewRepository.save(interview);
    }

    public void deleteInterview(Integer id){
        Interview interview = interviewRepository.findById(id).orElseThrow(()->new RuntimeException("Interview not found"));
        if(!interview.getApplication().getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can delete only your interviews");
        }
        interviewRepository.deleteById(id);
    }

    public Interview updateInterview(Integer id, Interview updatedInterview){
        Interview interview = interviewRepository.findById(id).orElseThrow(()-> new RuntimeException("Interview not found"));
        if(!interview.getApplication().getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new RuntimeException("You can update only your interviews");
        }
        interview.setInterviewDate(updatedInterview.getInterviewDate());
        interview.setType(updatedInterview.getType());
        interview.setOutcome(updatedInterview.getOutcome());
        interview.setNotes(updatedInterview.getNotes());

        return interviewRepository.save(interview);
    }
}
