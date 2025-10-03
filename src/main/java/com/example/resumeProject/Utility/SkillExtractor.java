package com.example.resumeProject.Utility;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SkillExtractor {
    //Define common skills
    private static final Set<String> SKILLS = new HashSet<>(Arrays.asList(
            "java","spring boot","spring","hibernate","jpa","sql","mysql","postgresql",
            "mongodb","rest api","microservices","aws","docker","kubernetes",
            "git","maven","gradle","linux","jenkins","ci/cd","html","css","javascript",
            "react","angular","nodejs","python","c++","data structures","algorithms",
            "nosql","redis","rabbitmq","kafka","elasticsearch","spring mvc","spring cloud",
            "unit testing","junit","integration testing"
    ));

    // alias -> canonical
    private static final Map<String,String> ALIASES;
    static {
        ALIASES = Map.of("restapi", "rest api", "rest-api", "rest api", "rest", "rest api", "js", "javascript", "node", "nodejs", "postgres", "postgresql", "postgresql", "postgresql", "db", "sql", "sqlserver", "sql");
    }

    // precompiled patterns cache for performance
    private static final Map<String,Pattern> PATTERN_CACHE = new HashMap<>();

    private static Pattern patternFor(String skill) {
        return PATTERN_CACHE.computeIfAbsent(skill,
                k -> Pattern.compile("(?<![a-zA-Z0-9])" + Pattern.quote(k) + "(?![a-zA-Z0-9])", Pattern.CASE_INSENSITIVE));
    }

    /**
     * Extract canonical skills found in text.
     * Returns sorted set of canonical skills.
     */
    public static Set<String> extractSkills(String text) {
        if (text == null || text.isBlank()) return Collections.emptySet();
        String lower = text.toLowerCase();

        Set<String> found = new LinkedHashSet<>();

        // check canonical list
        for (String s : SKILLS) {
            if (patternFor(s).matcher(lower).find()) found.add(s);
        }

        // check aliases
        for (Map.Entry<String,String> e : ALIASES.entrySet()) {
            if (patternFor(e.getKey()).matcher(lower).find()) {
                found.add(e.getValue());
            }
        }

        // also detect multi-word tokens with slight normalization (dashes/slashes)
        // no further processing here

        return found.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /** Utility: convert Set to CSV (lowercase canonical order) */
    public static String toCsv(Collection<String> skills) {
        return skills == null ? "" : skills.stream().map(String::trim).collect(Collectors.joining(","));
    }
}