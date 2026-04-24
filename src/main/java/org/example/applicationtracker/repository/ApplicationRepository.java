package org.example.applicationtracker.repository;

import org.example.applicationtracker.model.Application;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Integer> {

    Page<Application> findAll(Pageable pageable);
 

    Page<Application> findByUserEmail(String email,Pageable pageable);
    List<Application> findByUserEmailAndJobTitleContaining(String email,String keyword);
    List<Application> findByUserEmailAndStatus(String email,String status);
}
