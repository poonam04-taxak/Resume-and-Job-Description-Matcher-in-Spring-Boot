package com.example.resumeProject.Service;

import com.example.resumeProject.Entity.JobDescriptionEntity;
import com.example.resumeProject.Entity.MatchEntity;
import com.example.resumeProject.Entity.ResumeEntity;
import com.example.resumeProject.Repository.JobDescriptionRepository;
import com.example.resumeProject.Repository.MatchRepository;
import com.example.resumeProject.Repository.ResumeRepository;
import com.example.resumeProject.Utility.SkillExtractor;
import org.aspectj.lang.annotation.Around;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.*;
@Service
public class MatchService {
    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final MatchRepository matchRepository;

    public MatchService(ResumeRepository resumeRepository, JobDescriptionRepository jobDescriptionRepository, MatchRepository matchRepository){
        this.resumeRepository = resumeRepository;
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.matchRepository=matchRepository;
    }

    public MatchEntity matchResumeAndJD(Long resumeId, Long jdId){
        ResumeEntity resume = resumeRepository.findById(resumeId).orElseThrow(() ->new RuntimeException("Resume Not Found!"));
        JobDescriptionEntity jd = jobDescriptionRepository.findById(jdId).orElseThrow(() -> new RuntimeException("Job Description Not Found!"));

        //extract skills sets
        Set<String> resumeSkills = SkillExtractor.extractSkills(resume.getContent());
        Set<String> required = SkillExtractor.extractSkills(jd.getRequiredSkillsCsv() == null ? jd.getContent() : jd.getRequiredSkillsCsv());
        Set<String> optional = SkillExtractor.extractSkills(jd.getOptionalSkillsCsv() == null ? "" : jd.getOptionalSkillsCsv());

        //required match
        int reqTotal = Math.max(required.size(),0);
        Set<String> matchedRequired = new TreeSet<>(resumeSkills);
        matchedRequired.retainAll(required);

        double reqPercent = reqTotal == 0?0.0 : (matchedRequired.size() * 100.0 /reqTotal);

        //optional match
        int optTotal = Math.max(optional.size(),0);
        Set<String> matchedOptional = new TreeSet<>(resumeSkills);
        matchedOptional.retainAll(optional);

        double optPercent = optTotal == 0 ? 0.0 : (matchedOptional.size() * 100.0 / optTotal);

        //combined skills percent: give required higher weight
        double skillScore;
        if(reqTotal > 0){
            skillScore = reqPercent * 0.8 + optPercent * 0.2; //80% weight to required skills
        }
        else{
            //no required info -> fallback to matched among union
            Set<String> union = new HashSet<>(required);
            union.addAll(optional);
            skillScore = union.isEmpty() ? 0.0
                    : (resumeSkills.stream().filter(union::contains).count() * 100.0 / union.size());
        }

        //text similarity (TF - IDF based)
        double textSimilarity = computeTextSimilarity(resume.getContent(),jd.getContent()) * 100.0;

        //final overall: skills heavier (ex-> 0.7 skills, 0.3 text)
        double overall = (skillScore * 0.7) + (textSimilarity * 0.3);

        //missing (from required and important optional)
        Set<String> missing = new TreeSet<>(required);
        missing.removeAll(resumeSkills);

        // prepare extra skills(present in resume but not in JD)
        Set<String> extra = new TreeSet<>(resumeSkills);
        Set<String> jdUnion = new HashSet<>(required);
        jdUnion.addAll(optional);
        extra.removeAll(jdUnion);

        //suggestions
        List<String> suggestions = generateSuggestions(missing);



        //Save Match Entity
        // Check if match already exists
        MatchEntity match = matchRepository.findByResumeIdAndJdId(resumeId, jdId)
                .orElse(new MatchEntity());
        match.setResumeId(resumeId);
        match.setJdId(jdId);
        match.setSkillsMatchPercentage(round(skillScore));
        match.setTextSimilarityPercentage(round(textSimilarity));
        match.setOverallMatchPercentage(round(overall));
        match.setMatchedSkills(SkillExtractor.toCsv(new ArrayList<>(matchedRequired)));
        match.setMissingSkills(SkillExtractor.toCsv(missing));
        match.setExtraSkills(SkillExtractor.toCsv(extra));

        MatchEntity saved = matchRepository.save(match);

        // optionally, store suggestions somewhere or return them in API response
        // (we can extend MatchEntity to include suggestion text)
         return saved;

            }

            private static double round(double v){
        return Math.round(v * 100.0) / 100.0;
            }

            //TF-IDF + cosine similarity implementation for two documents
    private double computeTextSimilarity(String a, String b){
        Map<String, Integer> tfA = termFrequency(a);
        Map<String, Integer> tfB = termFrequency(b);

        //document frequency across the two docs
        Set<String> all = new HashSet<>();
        all.addAll(tfA.keySet());
        all.addAll(tfB.keySet());

        Map<String, Double> idf = new HashMap<>();
        int N = 2;
        for(String term : all){
            int df=0;
            if(tfA.getOrDefault(term, 0)> 0) df++;
            if(tfB.getOrDefault(term,0)> 0) df++;
            //idf smoothing
            double value = Math.log((N+1.0) / (df+1.0))+ 1.0;
            idf.put(term,value);
        }

        //build TF-IDF vector
        Map<String, Double> vA = new HashMap<>();
        Map<String, Double> vB = new HashMap<>();
        for(String term:all){
            double tfidfA =tfA.getOrDefault(term,0)* idf.get(term);
            double tfidfB = tfB.getOrDefault(term,0) * idf.get(term);
            vA.put(term, tfidfA);
            vB.put(term, tfidfB);
        }
        //cosine
        double dot = 0.0, na=0.0, nb = 0.0;
        for(String term:all){
            double va=vA.get(term);
            double vb = vB.get(term);
            dot += va*vb;
            na += va*va;
            nb += vb*vb;
        }
        if(na ==0 || nb == 0) return 0.0;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }
    private Map<String,Integer> termFrequency(String text){
        if(text == null) return Collections.emptyMap();
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");
        String[] tokens = cleaned.split("\\s+");
        Set<String> stopWords = new HashSet<>(Arrays.asList("and","or","the","a","an","to","in","on","of","with","for","is","are","was","were","this","that"));
        Map<String,Integer> tf =new HashMap<>();
        for(String t : tokens){
            if(t==null || t.length()<=1) continue;
            if(stopWords.contains(t)) continue;
            tf.put(t, tf.getOrDefault(t,0)+1);
        }
        return tf;
    }
    private List<String> generateSuggestions(Set<String> missing){
        List<String> out = new ArrayList<>();
        for(String s: missing){
            out.add("Add skill '" + s + "' to your Skills and Projects (mention where you used it). Example line : \"Used " + s + "to ...\"");
        }
        return out;
    }

    public List<String> suggestForMatch(MatchEntity match) {
        if (match.getMissingSkills() == null || match.getMissingSkills().isEmpty()) {
            return Collections.emptyList();
        }
        // Convert CSV string to Set
        Set<String> missing = new HashSet<>(Arrays.asList(match.getMissingSkills().split(",")));
        return generateSuggestions(missing);
    }
    public List<MatchEntity> getTopMatches(Long jdId, int limit) {
        return matchRepository.findByJdIdOrderByOverallMatchPercentageDesc(jdId, PageRequest.of(0, limit));
    }
}
