package service;

import entity.Application;
import entity.Job;
import entity.Student;
import service.ResumeAnalyzer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationService {
    private final List<Application> applications;

    public ApplicationService() {
        this.applications = new ArrayList<>();
    }

    public Application applyToJob(Student student, Job job) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (job == null) {
            throw new IllegalArgumentException("Job cannot be null");
        }

        boolean alreadyApplied = hasAlreadyApplied(student.getId(), job.getId());
        if (alreadyApplied) {
            throw new IllegalStateException("You have already applied to this job");
        }

        Application application = new Application(student, job);

        if (student.hasResume()) {
            int matchScore = ResumeAnalyzer.calculateMatchPercentage(
                    student.getResume(), job.getRequiredSkills());
            application.setMatchScore(matchScore);
        }

        applications.add(application);
        return application;
    }

    public boolean hasAlreadyApplied(int studentId, int jobId) {
        return applications.stream()
                .anyMatch(app -> app.getStudent().getId() == studentId && 
                                 app.getJob().getId() == jobId);
    }

    public List<Application> getApplicationsByStudent(Student student) {
        return applications.stream()
                .filter(app -> app.getStudent().getId() == student.getId())
                .collect(Collectors.toList());
    }

    public List<Application> getApplicationsByJob(Job job) {
        return applications.stream()
                .filter(app -> app.getJob().getId() == job.getId())
                .collect(Collectors.toList());
    }

    public List<Application> getApplicationsByCompany(int companyId) {
        return applications.stream()
                .filter(app -> app.getJob().getCompany().getId() == companyId)
                .collect(Collectors.toList());
    }

    public List<Application> filterByMatchScore(int minScore) {
        return applications.stream()
                .filter(app -> app.getMatchScore() >= minScore)
                .collect(Collectors.toList());
    }

    public List<Application> getTopCandidates(Job job, int limit) {
        return getApplicationsByJob(job).stream()
                .sorted(Comparator.comparing(Application::getMatchScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void sortByMatchScore(List<Application> apps) {
        apps.sort(Comparator.comparing(Application::getMatchScore).reversed());
    }

    public Application getApplicationById(int applicationId) {
        return applications.stream()
                .filter(app -> app.getId() == applicationId)
                .findFirst()
                .orElse(null);
    }

    public void updateApplicationStatus(int applicationId, Application.Status status) {
        Application app = getApplicationById(applicationId);
        if (app != null) {
            app.setStatus(status);
            if (status == Application.Status.REVIEWED) {
                app.markAsReviewed();
            }
        }
    }

    public void displayApplications(List<Application> apps, boolean showDetails) {
        if (apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        if (showDetails) {
            System.out.println("\n" + "=".repeat(80));
            for (Application app : apps) {
                System.out.println("Application ID: " + app.getId());
                System.out.println("  Student: " + app.getStudent().getName());
                System.out.println("  Email: " + app.getStudent().getEmail());
                System.out.println("  Job: " + app.getJob().getTitle());
                System.out.println("  Company: " + app.getJob().getCompany().getName());
                System.out.println("  Status: " + app.getStatus());
                System.out.println("  Match Score: " + app.getMatchScore() + "%");

                if (app.getStudent().hasResume()) {
                    String analysis = ResumeAnalyzer.getMatchSummary(
                            app.getStudent().getResume(),
                            app.getJob().getRequiredSkills());
                    System.out.println("  Resume Analysis:");
                    for (String line : analysis.split("\n")) {
                        System.out.println("    " + line);
                    }
                }
                System.out.println("-".repeat(80));
            }
            System.out.println("=".repeat(80));
        } else {
            System.out.println("\n" + "=".repeat(70));
            System.out.printf("%-5s | %-15s | %-20s | %-10s | %s%n", 
                    "ID", "Student", "Job", "Status", "Match %");
            System.out.println("=".repeat(70));

            for (Application app : apps) {
                System.out.printf("%-5d | %-15s | %-20s | %-10s | %d%%%n",
                        app.getId(),
                        truncate(app.getStudent().getName(), 15),
                        truncate(app.getJob().getTitle(), 20),
                        app.getStatus(),
                        app.getMatchScore());
            }
            System.out.println("=".repeat(70));
        }
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    public int getApplicationCount() {
        return applications.size();
    }
}