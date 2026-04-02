package service;

import entity.Job;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeAnalyzer {
    
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-zA-Z]+");

    public static int calculateMatchPercentage(String resume, List<String> requiredSkills) {
        if (resume == null || resume.trim().isEmpty()) {
            return 0;
        }
        
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 0;
        }

        String resumeLower = resume.toLowerCase();
        Set<String> resumeWords = extractWords(resumeLower);
        
        int matchedSkills = 0;
        int totalSkills = requiredSkills.size();

        for (String skill : requiredSkills) {
            String skillLower = skill.toLowerCase().trim();
            if (resumeWords.contains(skillLower) || containsSubstring(resumeLower, skillLower)) {
                matchedSkills++;
            }
        }

        if (totalSkills == 0) {
            return 0;
        }

        return (int) ((matchedSkills * 100.0) / totalSkills);
    }

    private static Set<String> extractWords(String text) {
        Set<String> words = new HashSet<>();
        Matcher matcher = WORD_PATTERN.matcher(text);
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }
        return words;
    }

    private static boolean containsSubstring(String text, String keyword) {
        return text.contains(keyword);
    }

    public static List<String> findMatchingSkills(String resume, List<String> requiredSkills) {
        List<String> matchingSkills = new ArrayList<>();
        
        if (resume == null || resume.trim().isEmpty() || requiredSkills == null) {
            return matchingSkills;
        }

        String resumeLower = resume.toLowerCase();

        for (String skill : requiredSkills) {
            String skillLower = skill.toLowerCase().trim();
            if (resumeLower.contains(skillLower)) {
                matchingSkills.add(skill);
            }
        }

        return matchingSkills;
    }

    public static String getMatchSummary(String resume, List<String> requiredSkills) {
        List<String> matched = findMatchingSkills(resume, requiredSkills);
        int percentage = calculateMatchPercentage(resume, requiredSkills);
        
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Match: %d%%\n", percentage));
        
        if (!matched.isEmpty()) {
            summary.append("Matched Skills: ").append(String.join(", ", matched)).append("\n");
        }
        
        List<String> missing = new ArrayList<>();
        for (String skill : requiredSkills) {
            if (!matched.contains(skill)) {
                missing.add(skill);
            }
        }
        
        if (!missing.isEmpty()) {
            summary.append("Missing Skills: ").append(String.join(", ", missing));
        }
        
        return summary.toString();
    }

    public static ResumeAnalysisResult analyzeWithFeedback(String resumeText, Job job) {
        List<String> requiredSkills = job.getRequiredSkills();
        
        if (resumeText == null || resumeText.trim().isEmpty()) {
            return new ResumeAnalysisResult(0, new ArrayList<>(), requiredSkills);
        }
        
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return new ResumeAnalysisResult(0, new ArrayList<>(), new ArrayList<>());
        }

        List<String> matchedSkills = findMatchingSkills(resumeText, requiredSkills);
        List<String> missingSkills = new ArrayList<>();
        
        for (String skill : requiredSkills) {
            boolean found = matchedSkills.stream()
                    .anyMatch(s -> s.equalsIgnoreCase(skill));
            if (!found) {
                missingSkills.add(skill);
            }
        }

        Collections.sort(missingSkills, String.CASE_INSENSITIVE_ORDER);
        
        int matchPercentage = calculateMatchPercentage(resumeText, requiredSkills);
        
        List<String> topMissing;
        if (missingSkills.size() > 5) {
            topMissing = missingSkills.subList(0, 5);
        } else {
            topMissing = missingSkills;
        }
        
        return new ResumeAnalysisResult(matchPercentage, matchedSkills, missingSkills, topMissing);
    }

    public static void displayAnalysis(ResumeAnalysisResult result) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         RESUME ANALYSIS RESULTS");
        System.out.println("=".repeat(50));
        
        System.out.printf("Match Percentage: %d%%\n", result.getMatchPercentage());
        
        if (!result.getMatchedSkills().isEmpty()) {
            System.out.println("\nMatched Skills:");
            System.out.println("  " + String.join(", ", result.getMatchedSkills()));
        }
        
        if (!result.getMissingSkills().isEmpty()) {
            System.out.println("\nMissing Keywords (" + result.getMissingSkills().size() + "):");
            for (String skill : result.getMissingSkills()) {
                System.out.println("  - " + skill);
            }
        }
        
        if (!result.getTopMissingSkills().isEmpty()) {
            System.out.println("\nSuggestions to improve your resume:");
            for (String skill : result.getTopMissingSkills()) {
                System.out.println("  Add '" + skill + "'");
            }
            if (result.getMissingSkills().size() > 5) {
                System.out.println("  ... and " + (result.getMissingSkills().size() - 5) + " more");
            }
        }
        
        System.out.println("=".repeat(50));
    }

    public static class ResumeAnalysisResult {
        private final int matchPercentage;
        private final List<String> matchedSkills;
        private final List<String> missingSkills;
        private final List<String> topMissingSkills;

        public ResumeAnalysisResult(int matchPercentage, List<String> matchedSkills, 
                                    List<String> missingSkills) {
            this(matchPercentage, matchedSkills, missingSkills, missingSkills);
        }

        public ResumeAnalysisResult(int matchPercentage, List<String> matchedSkills,
                                    List<String> missingSkills, List<String> topMissingSkills) {
            this.matchPercentage = matchPercentage;
            this.matchedSkills = matchedSkills;
            this.missingSkills = missingSkills;
            this.topMissingSkills = topMissingSkills;
        }

        public int getMatchPercentage() {
            return matchPercentage;
        }

        public List<String> getMatchedSkills() {
            return matchedSkills;
        }

        public List<String> getMissingSkills() {
            return missingSkills;
        }

        public List<String> getTopMissingSkills() {
            return topMissingSkills;
        }
    }
}