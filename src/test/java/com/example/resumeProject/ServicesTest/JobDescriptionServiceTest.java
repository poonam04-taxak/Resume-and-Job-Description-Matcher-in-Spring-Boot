package com.example.resumeProject.ServicesTest;

import com.example.resumeProject.Entity.JobDescriptionEntity;
import com.example.resumeProject.Repository.JobDescriptionRepository;
import com.example.resumeProject.Service.JobDescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JobDescriptionServiceTest {
    private JobDescriptionRepository jobDescriptionRepository;
    private JobDescriptionService jobDescriptionService;

    @BeforeEach
    void setUp(){
        jobDescriptionRepository = Mockito.mock(JobDescriptionRepository.class);
        jobDescriptionService = new JobDescriptionService(jobDescriptionRepository);
    }

    @Test
    void testSaveFromText(){
        String title = "Java Backend Developer";
        String content = "Requirements: Java,Spring boot";

        JobDescriptionEntity jd = new JobDescriptionEntity();
        jd.setTitle(title);
        jd.setContent(content);
        jd.setSkillsCsv("java,spring boot"); // ✅ ensure not null


        when(jobDescriptionRepository.save(any(JobDescriptionEntity.class))).thenReturn(jd);

        JobDescriptionEntity saved = jobDescriptionService.saveFromText(title, content);
        assertEquals(title,saved.getTitle());
        assertTrue(saved.getSkillsCsv().contains("java"));
        assertTrue(saved.getSkillsCsv().contains("spring boot"));
    }

    @Test
    void testGetById(){
        JobDescriptionEntity jd = new JobDescriptionEntity();
        jd.setId(1L);
        when(jobDescriptionRepository.findById(1L)).thenReturn(Optional.of(jd));
        Optional<JobDescriptionEntity> result= jobDescriptionService.getJDById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L,result.get().getId());
    }


}
