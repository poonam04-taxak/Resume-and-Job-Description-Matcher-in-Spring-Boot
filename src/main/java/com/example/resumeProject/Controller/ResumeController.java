package com.example.resumeProject.Controller;

import com.example.resumeProject.Entity.ResumeEntity;
import com.example.resumeProject.Repository.ResumeRepository;
import com.example.resumeProject.Service.ResumeService;
import com.example.resumeProject.Utility.SkillExtractor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.aot.AotServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    //Post-ADD resume
    @PostMapping
    public ResumeEntity addResume(@RequestBody ResumeEntity resumeEntity) {
        return resumeService.saveResume(resumeEntity);
    }

    //GET-get all resumes
    @GetMapping
    public List<ResumeEntity> getAllResumes() {
        return resumeService.getAllResumes();
    }

    //GET-get resume by ID
    @GetMapping("/{id}")
    public ResumeEntity getResumeById(@PathVariable Long id) {
        return resumeService.getResumeById(id).orElse(null);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ResumeEntity>> searchBySkill(
            @RequestParam String skill,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {


        Page<ResumeEntity> res = resumeService.searchBySkill(skill, PageRequest.of(page, size));
        return ResponseEntity.ok(res);
    }

    //Upload resume
    @PostMapping("/upload")
    public ResumeEntity uploadResume(@RequestParam("file") MultipartFile file) {
        ResumeEntity resume = new ResumeEntity();
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name is null or empty");
        }

        resume.setFilename(fileName);
        String content = "";

        // Convert filename to lowercase for safety
        String lowerFileName = fileName.toLowerCase();

        // Validate file type first (outside try)
        if (!lowerFileName.endsWith(".pdf") && !lowerFileName.endsWith(".docx")) {
            throw new IllegalArgumentException("Unsupported file format. Please upload PDF or DOCX.");
        }

        try {
            if (lowerFileName.endsWith(".pdf")) {
                   // PDF extraction
                   try (PDDocument document = Loader.loadPDF(file.getInputStream().readAllBytes())) {
                       PDFTextStripper pdfStripper = new PDFTextStripper();
                       content = pdfStripper.getText(document);
                   }
               }
           else if (lowerFileName.endsWith(".docx")) {
                // DOCX extraction
                try (XWPFDocument doc = new XWPFDocument(file.getInputStream());
                     XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
                    content = extractor.getText();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to extract content from file: " + e.getMessage());
        }

        // Set extracted content to resume
        resume.setContent(content);

        // Extract skills from content
        Set<String> skillSet = SkillExtractor.extractSkills(content);
        String skillsCsv = String.join(",", skillSet);
        resume.setSkillsCsv(skillsCsv);

        // Save resume to MySQL
        return resumeService.saveResume(resume);
    }
}
