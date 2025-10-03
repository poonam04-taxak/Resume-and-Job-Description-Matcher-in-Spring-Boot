package com.example.resumeProject.Service;

import com.example.resumeProject.Entity.ResumeEntity;
import com.example.resumeProject.Repository.ResumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    //Save resume
    public ResumeEntity saveResume(ResumeEntity resumeEntity){
        return resumeRepository.save(resumeEntity);
    }

    //Get all Resumes
    public List<ResumeEntity> getAllResumes(){
        return resumeRepository.findAll();
    }

    //Get resume by ID
    public Optional<ResumeEntity> getResumeById(Long id){
        return resumeRepository.findById(id);
    }

    public Page<ResumeEntity> searchBySkill(String skill, Pageable pageable) {
        return resumeRepository.findBySkillsCsvContainingIgnoreCase(skill, pageable);
    }
}
