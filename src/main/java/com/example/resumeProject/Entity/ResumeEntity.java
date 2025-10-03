package com.example.resumeProject.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
public class ResumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "skills_csv", columnDefinition = "LONGTEXT")
    private String skillsCsv;

    private LocalDateTime createdAt = LocalDateTime.now();

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
