package com.example.resumeProject.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_description")
public class JobDescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "skills_csv", columnDefinition = "LONGTEXT")
    private String skillsCsv;

    @Column(name = "required_skills_csv" , columnDefinition = "LONGTEXT")
    private String requiredSkillsCsv;

    public String getOptionalSkillsCsv() {
        return optionalSkillsCsv;
    }

    public void setOptionalSkillsCsv(String optionalSkillsCsv) {
        this.optionalSkillsCsv = optionalSkillsCsv;
    }

    public String getRequiredSkillsCsv() {
        return requiredSkillsCsv;
    }

    public void setRequiredSkillsCsv(String requiredSkillsCsv) {
        this.requiredSkillsCsv = requiredSkillsCsv;
    }

    @Column(name = "optional_skills_csv", columnDefinition = "LONGTEXT")
    private String optionalSkillsCsv;

    private LocalDateTime createdAt = LocalDateTime.now();

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSkillsCsv() {
        return skillsCsv;
    }

    public void setSkillsCsv(String skillsCsv) {
        this.skillsCsv = skillsCsv;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
