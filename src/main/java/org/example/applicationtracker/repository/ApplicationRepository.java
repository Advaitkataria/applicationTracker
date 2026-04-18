package org.example.applicationtracker.repository;

import org.example.applicationtracker.model.Application;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Integer> {

    Page<Application> findAll(Pageable pageable);

    List<Application> findByJobTitleContaining(String keyword);

    List<Application> findByStatus(String status);

    List<Application> findByAppliedDate(LocalDate appliedDate);

    @Query("Select a FROM Application WHERE a.companyName = ?1 AND a.appliedDate = ?2")
    List<Application> findByCompanyNameAndAppliedDate(String companyName, LocalDate AppliedDate);
}
