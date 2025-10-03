package com.example.resumeProject.Repository;

import com.example.resumeProject.Entity.ResumeEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
    Page<ResumeEntity> findBySkillsCsvContainingIgnoreCase(String skill, Pageable pageable);
}
