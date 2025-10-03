package com.example.resumeProject.Controller;

import com.example.resumeProject.Entity.JobDescriptionEntity;
import com.example.resumeProject.Service.JobDescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobdescriptions")
public class JobDescriptionController {
    private final JobDescriptionService jobDescriptionService;

    public JobDescriptionController(JobDescriptionService jobDescriptionService) {
        this.jobDescriptionService = jobDescriptionService;
    }
    //POST - ADD JD
    @PostMapping
    public JobDescriptionEntity addJD(@RequestParam String title, @RequestBody String text){
        return jobDescriptionService.saveFromText(title,text);
    }

    //GET - ALL JDS
    @GetMapping
    public List<JobDescriptionEntity> getAllJDs(){
        return jobDescriptionService.getAllJDs();
    }

    //GET 0 JD by ID
    @GetMapping("/{id}")
    public JobDescriptionEntity getJDById(@PathVariable Long id){
        return jobDescriptionService.getJDById(id).orElse(null);
    }

    //upload JD
    @PostMapping("/uploadText")
    public ResponseEntity<JobDescriptionEntity> uploadTextJD(@RequestParam String title, @RequestBody String content){
        JobDescriptionEntity jd = jobDescriptionService.saveFromText(title, content);
        return ResponseEntity.ok(jd);
    }
}
