package com.app.repo;

import com.app.model.Application;
import com.app.model.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    List<Application> findAllByStatus(ApplicationStatus status);
    List<Application> findAllByCategory_Id(Long categoryId);
}
