package org.example.applicationtracker.service;

import org.example.applicationtracker.exception.ApplicationNotFoundException;
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
        if(applicationRepository.count()==0){
            return null;
        }
        else {
            return applicationRepository.findAll(pageable);}
    }
    public Application addApplication(Application application){
        return applicationRepository.save(application);
    }
    public void deleteApplication(int id){
        if(!applicationRepository.existsById(id)){
            throw new ApplicationNotFoundException(id);
        }
        applicationRepository.deleteById(id);
    }
    public Application updateApplication(int id, Application updatedApplication){
        Application a = applicationRepository.findById(id).orElseThrow(()-> new ApplicationNotFoundException(id));
        a.setJobTitle(updatedApplication.getJobTitle());
        a.setCompanyName(updatedApplication.getCompanyName());
        a.setJobTitle(updatedApplication.getJobTitle());
        a.setStatus(updatedApplication.getStatus());
        a.setAppliedDate(updatedApplication.getAppliedDate());
        a.setNotes(updatedApplication.getNotes());
        a.setSalaryExpectation(updatedApplication.getSalaryExpectation());

        return applicationRepository.save(a);
    }

    public List<Application> getByJobTitleContaining(String keyword){
        return applicationRepository.findByJobTitleContaining(keyword);
    }
    public List<Application> getByStatus(String status) {
        List<Application> applications = applicationRepository.findByStatus(status);
        if (applications.isEmpty()) {
            throw new ApplicationNotFoundException();
        }
        return applications;
    }
    public List<Application> getByAppliedDate(LocalDate appliedDate){
        if(applicationRepository.findByAppliedDate(appliedDate).isEmpty()){
            throw new ApplicationNotFoundException();
        }
        return applicationRepository.findByAppliedDate(appliedDate);
    }
    public List<Application> getByCompanyNameAndAppliedDate(String companyName,LocalDate appliedDate){
        if(applicationRepository.findByCompanyNameAndAppliedDate(companyName, appliedDate).isEmpty()){
            throw new ApplicationNotFoundException();
        }
        return applicationRepository.findByCompanyNameAndAppliedDate(companyName,appliedDate);
    }


}
