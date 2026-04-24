package org.example.applicationtracker.service;

import org.example.applicationtracker.exception.ApplicationNotFoundException;
import org.example.applicationtracker.exception.UnauthorizedAccessException;
import org.example.applicationtracker.model.Application;
import org.example.applicationtracker.model.User;
import org.example.applicationtracker.repository.ApplicationRepository;
import org.example.applicationtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository){
        this.applicationRepository=applicationRepository;
        this.userRepository=userRepository;
    }
    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Page<Application> getAllApplications(int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        String email = getCurrentUser().getEmail();
        if(getCurrentUser().getRole().equals("ROLE_ADMIN")){
            return applicationRepository.findAll(pageable);
        }
        return applicationRepository.findByUserEmail(email,pageable);
    }
    public Application addApplication(Application application){
        application.setUser(getCurrentUser());
        return applicationRepository.save(application);
    }
    public void deleteApplication(int id){
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        applicationRepository.deleteById(id);
    }
    public Application updateApplication(int id, Application updatedApplication){
        Application a =applicationRepository.findById(id).orElseThrow(()->new ApplicationNotFoundException(id));
        if(!a.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new UnauthorizedAccessException("You can update only your applications");
        };
        a.setJobTitle(updatedApplication.getJobTitle());
        a.setCompanyName(updatedApplication.getCompanyName());
        a.setStatus(updatedApplication.getStatus());
        a.setAppliedDate(updatedApplication.getAppliedDate());
        a.setNotes(updatedApplication.getNotes());
        a.setSalaryExpectation(updatedApplication.getSalaryExpectation());

        return applicationRepository.save(a);
    }

    public List<Application> getByJobTitleContaining(String keyword){
        return applicationRepository.findByUserEmailAndJobTitleContaining(getCurrentUser().getEmail(), keyword);
    }
    public List<Application> getByStatus(String status) {
        List<Application> applications = applicationRepository.findByUserEmailAndStatus(getCurrentUser().getEmail(), status);
        if (applications.isEmpty()) {
            throw new ApplicationNotFoundException();
        }
        return applications;
    }
    public boolean isOwner(int id) {
        String email = getCurrentUser().getEmail();


        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));

        return application.getUser().getEmail().equals(email);
    }



}
