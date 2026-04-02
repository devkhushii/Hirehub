package entity;

import java.util.ArrayList;
import java.util.List;

public class Job {
    private static int idCounter = 1;
    private final int id;
    private String title;
    private String description;
    private List<String> requiredSkills;
    private Company company;

    public Job(String title, String description, List<String> requiredSkills, Company company) {
        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
        this.company = company;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getRequiredSkills() { return new ArrayList<>(requiredSkills); }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>(); }
    public Company getCompany() { return company; }
    public String getSkillsAsString() { return String.join(", ", requiredSkills); }

    @Override
    public String toString() {
        return String.format("Job[ID: %d, Title: %s, Company: %s, Skills: %s]",
                id, title, company.getName(), getSkillsAsString());
    }
}