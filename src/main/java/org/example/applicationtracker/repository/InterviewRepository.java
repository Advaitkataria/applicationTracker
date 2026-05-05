package org.example.applicationtracker.repository;

import org.example.applicationtracker.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview,Integer> {

    List<Interview> findByApplicationId(Integer applicationId);

    List<Interview> findByApplicationIdAndOutcome(Integer applicationId,String outcome);
}
