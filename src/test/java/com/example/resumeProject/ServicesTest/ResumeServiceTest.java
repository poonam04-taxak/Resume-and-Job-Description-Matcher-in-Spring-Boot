package com.example.resumeProject.ServicesTest;

import com.example.resumeProject.Entity.ResumeEntity;
import com.example.resumeProject.Repository.ResumeRepository;
import com.example.resumeProject.Service.ResumeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ResumeServiceTest {
    private ResumeRepository resumeRepository;
    private ResumeService resumeService;

    @BeforeEach
    void setUp(){
        resumeRepository = Mockito.mock(ResumeRepository.class);
        resumeService = new ResumeService(resumeRepository);
    }

    @Test
    void testSaveResume(){
        ResumeEntity resume = new ResumeEntity();
        resume.setFilename("poonam_taxak.pdf");
        resume.setContent("Java");

        when(resumeRepository.save(resume)).thenReturn(resume);

        ResumeEntity saved = resumeService.saveResume(resume);
        assertEquals("poonam_taxak.pdf", saved.getFilename());
        assertEquals("Java", saved.getContent());
        verify(resumeRepository, times(1)).save(resume);
    }

    @Test
    void testGetAllResumes(){
        List<ResumeEntity> list = Arrays.asList(new ResumeEntity(),new ResumeEntity() );
        when(resumeRepository.findAll()).thenReturn(list);

        List<ResumeEntity> result= resumeService.getAllResumes();
        assertEquals(2,result.size());
    }

    @Test
    void testSearchBySkills(){
        ResumeEntity r=new ResumeEntity();
        r.setSkillsCsv("java");
        Page<ResumeEntity> page = new PageImpl<ResumeEntity>(List.of(r));

        when(resumeRepository.findBySkillsCsvContainingIgnoreCase(eq("java"), any(Pageable.class))).thenReturn(page);

        Page<ResumeEntity> result = resumeService.searchBySkill("java", PageRequest.of(0,10));
        assertEquals(1,result.getContent().size());
        assertTrue(result.getContent().get(0).getSkillsCsv().contains("java"));

    }


}
