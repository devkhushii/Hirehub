package service;

import entity.Company;
import entity.Job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JobService {
    private final List<Job> jobs;
    private static int idCounter = 1;

    public JobService() {
        this.jobs = new ArrayList<>();
    }

    public Job createJob(String title, String description, List<String> requiredSkills, Company company) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Job title cannot be empty");
        }
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }

        Job job = new Job(title, description, requiredSkills, company);
        jobs.add(job);
        return job;
    }

    public List<Job> getAllJobs() {
        return new ArrayList<>(jobs);
    }

    public List<Job> getJobsByCompany(Company company) {
        return jobs.stream()
                .filter(job -> job.getCompany().getId() == company.getId())
                .collect(Collectors.toList());
    }

    public Job getJobById(int jobId) {
        return jobs.stream()
                .filter(job -> job.getId() == jobId)
                .findFirst()
                .orElse(null);
    }

    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllJobs();
        }

        String lowerKeyword = keyword.toLowerCase();
        return jobs.stream()
                .filter(job -> job.getTitle().toLowerCase().contains(lowerKeyword) ||
                        job.getDescription().toLowerCase().contains(lowerKeyword) ||
                        job.getSkillsAsString().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public List<Job> getJobsBySkills(List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return getAllJobs();
        }

        List<String> lowerSkills = skills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return jobs.stream()
                .filter(job -> job.getRequiredSkills().stream()
                        .anyMatch(skill -> lowerSkills.contains(skill.toLowerCase())))
                .collect(Collectors.toList());
    }

    public boolean deleteJob(int jobId, Company company) {
        Job job = getJobById(jobId);
        if (job != null && job.getCompany().getId() == company.getId()) {
            jobs.remove(job);
            return true;
        }
        return false;
    }

    public void displayJobs(List<Job> jobList) {
        if (jobList.isEmpty()) {
            System.out.println("No jobs found.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.printf("%-5s | %-25s | %-20s | %s%n", "ID", "Title", "Company", "Skills");
        System.out.println("=".repeat(70));

        for (Job job : jobList) {
            System.out.printf("%-5d | %-25s | %-20s | %s%n",
                    job.getId(),
                    truncate(job.getTitle(), 25),
                    truncate(job.getCompany().getName(), 20),
                    truncate(job.getSkillsAsString(), 30));
        }
        System.out.println("=".repeat(70));
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    public int getJobCount() {
        return jobs.size();
    }
}