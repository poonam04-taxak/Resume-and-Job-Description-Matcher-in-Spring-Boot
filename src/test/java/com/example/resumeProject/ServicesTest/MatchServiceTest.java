package com.example.resumeProject.ServicesTest;

import com.example.resumeProject.Entity.JobDescriptionEntity;
import com.example.resumeProject.Entity.MatchEntity;
import com.example.resumeProject.Entity.ResumeEntity;
import com.example.resumeProject.Repository.JobDescriptionRepository;
import com.example.resumeProject.Repository.MatchRepository;
import com.example.resumeProject.Repository.ResumeRepository;
import com.example.resumeProject.Service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatchServiceTest {
    private ResumeRepository resumeRepository;
    private JobDescriptionRepository jobDescriptionRepository;
    private MatchRepository matchRepository;
    private MatchService matchService;

    @BeforeEach
    void setUp(){
        resumeRepository = mock(ResumeRepository.class);
        jobDescriptionRepository = mock(JobDescriptionRepository.class);
        matchRepository =  mock(MatchRepository.class);

        matchService = new MatchService(resumeRepository,jobDescriptionRepository,matchRepository);

    }
    @Test
    void testMatchResumeAndJD(){
        ResumeEntity resume = new ResumeEntity();
        resume.setId(1L);
        resume.setContent("Java, Spring Boot");

        JobDescriptionEntity jd = new JobDescriptionEntity();
        jd.setId(1L);
        jd.setRequiredSkillsCsv("Java, SQL");

        when(resumeRepository.findById(1L)).thenReturn(Optional.of(resume));
        when(jobDescriptionRepository.findById(1L)).thenReturn(Optional.of(jd));
        when(matchRepository.findByResumeIdAndJdId(1L, 1L)).thenReturn(Optional.empty());
        when(matchRepository.save(any(MatchEntity.class))).thenAnswer(i -> i.getArgument(0));

        MatchEntity match =  matchService.matchResumeAndJD(1L,1L);

        assertNotNull(match);
        assertEquals(1L,match.getResumeId());
        assertEquals(1L, match.getJdId());
        assertTrue(match.getSkillsMatchPercentage()>0);
        assertTrue(match.getOverallMatchPercentage()>0);

    }


}
