package com.example.resumeProject.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long resumeId;
    private Long jdId;
    private Double skillsMatchPercentage;
    private Double textSimilarityPercentage;
    private Double overallMatchPercentage;
    private String matchedSkills;
    private String missingSkills;
    private String extraSkills;

    private LocalDateTime createdAt = LocalDateTime.now();

    //Getters and Setters

    public String getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(String missingSkills) {
        this.missingSkills = missingSkills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJdId() {
        return jdId;
    }

    public void setJdId(Long jdId) {
        this.jdId = jdId;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public Double getSkillsMatchPercentage() {
        return skillsMatchPercentage;
    }

    public void setSkillsMatchPercentage(Double skillsMatchPercentage) {
        this.skillsMatchPercentage = skillsMatchPercentage;
    }

    public Double getTextSimilarityPercentage() {
        return textSimilarityPercentage;
    }

    public void setTextSimilarityPercentage(Double textSimilarityPercentage) {
        this.textSimilarityPercentage = textSimilarityPercentage;
    }

    public Double getOverallMatchPercentage() {
        return overallMatchPercentage;
    }

    public void setOverallMatchPercentage(Double overallMatchPercentage) {
        this.overallMatchPercentage = overallMatchPercentage;
    }

    public String getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(String matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public String getExtraSkills() {
        return extraSkills;
    }

    public void setExtraSkills(String extraSkills) {
        this.extraSkills = extraSkills;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
