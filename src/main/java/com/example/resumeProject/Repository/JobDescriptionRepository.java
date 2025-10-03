package com.example.resumeProject.Repository;

import com.example.resumeProject.Entity.JobDescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescriptionEntity, Long> {
}
