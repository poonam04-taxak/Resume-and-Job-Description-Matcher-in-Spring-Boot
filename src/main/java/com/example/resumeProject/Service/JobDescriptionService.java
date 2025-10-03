package com.example.resumeProject.Service;

import com.example.resumeProject.Entity.JobDescriptionEntity;
import com.example.resumeProject.Repository.JobDescriptionRepository;
import com.example.resumeProject.Utility.SkillExtractor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobDescriptionService {
    private final JobDescriptionRepository jdRepository;

    public JobDescriptionService(JobDescriptionRepository jdRepository) {
        this.jdRepository = jdRepository;
    }

    //Save JD
    public JobDescriptionEntity saveFromText(String title, String text) {
        JobDescriptionEntity jd = new JobDescriptionEntity();
        jd.setTitle(title);
        jd.setContent(text);

        //parse sections
        Map<String, String> sections = extractSections(text);

        //required skills from "requirements" section if found, else from  full text
        Set<String> required = SkillExtractor.extractSkills(sections.getOrDefault("required", text));
        Set<String> optional = SkillExtractor.extractSkills(sections.getOrDefault("preferred", ""));

      //combine required + optional (avoid duplicates using TreeSet)
        Set<String> allSkills = new TreeSet<>();
        allSkills.addAll(required);
        allSkills.addAll(optional);

        // always set non-null CSV (even if empty)
        String csv = SkillExtractor.toCsv(allSkills);
        jd.setSkillsCsv(csv != null ? csv : "");

        jd.setSkillsCsv(SkillExtractor.toCsv(allSkills)); //combined CSV


        return jdRepository.save(jd);

    }

    /**
     * Heuristic section extractor:
     * looks for headings like "Requirements", "Responsibilities", "Preferred", "Nice to have"
     */
    private Map<String, String> extractSections(String text) {
        Map<String, String> map = new HashMap<>();
        if (text == null) return map;
        String lower = text.toLowerCase();

        //find required block
        Pattern reqPattern = Pattern.compile("(requirements?|required qualifications|must have)([:\\-\\n].*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = reqPattern.matcher(lower);
        if (m.find()) {
            //take next ~4000 characters after heading(heuristic)
            String block = lower.substring(m.start(1), Math.min(lower.length(), m.start(1) + 4000));
            map.put("required", block);
        }

        //find preferred block
        Pattern prefPattern = Pattern.compile("(preferred qualifications|nice to have|prefered|optional|nice-to-have)([:\\-\\n].*)",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        m = prefPattern.matcher(lower);
        if(m.find()) {
            String block = lower.substring(m.start(1), Math.min(lower.length(), m.start(1) + 4000));
            map.put("preferred", block);
        }
        return map;

    }

    public List<JobDescriptionEntity> getAllJDs() {

 return jdRepository.findAll();
    }

    //get jd by ID
    public Optional<JobDescriptionEntity> getJDById(Long id){
        return jdRepository.findById(id);
    }
}

