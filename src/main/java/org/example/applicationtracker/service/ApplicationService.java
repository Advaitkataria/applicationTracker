package org.example.applicationtracker.service;

import org.example.applicationtracker.model.Application;
import org.example.applicationtracker.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository){
        this.applicationRepository=applicationRepository;
    }

    public Page<Application> getAllApplications(int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        return applicationRepository.findAll(pageable);
    }
    public Application addApplication(Application application){
        return applicationRepository.save(application);
    }
    public void deleteApplication(int id){
        applicationRepository.deleteById(id);
    }

    public List<Application> getByJobTitleContaining(String keyword){
        return applicationRepository.findByJobTitleContaining(keyword);
    }
    public List<Application> getByStatus(String status){
        return applicationRepository.findByStatus(status);
    }
    public List<Application> getByAppliedDate(LocalDate appliedDate){
        return applicationRepository.findByAppliedDate(appliedDate);
    }
    public List<Application> getByCompanyNameAndAppliedDate(String companyName,LocalDate appliedDate){
        return applicationRepository.findByCompanyNameAndAppliedDate(companyName,appliedDate);
    }

}
