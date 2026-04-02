package ui;

import entity.*;
import service.*;

import java.util.*;

public class ConsoleMenu {
    private final StudentService studentService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final Scanner scanner;

    public ConsoleMenu(StudentService studentService, CompanyService companyService,
                       JobService jobService, ApplicationService applicationService) {
        this.studentService = studentService;
        this.companyService = companyService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     WELCOME TO HIRE HUB - JOB PORTAL SYSTEM");
        System.out.println("=".repeat(60));

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    handleStudentLogin();
                    break;
                case 2:
                    handleCompanyLogin();
                    break;
                case 3:
                    handleAdminLogin();
                    break;
                case 0:
                    System.out.println("Thank you for using Hire Hub. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n----- MAIN MENU -----");
        System.out.println("1. Student Login");
        System.out.println("2. Company Login");
        System.out.println("3. Admin Login");
        System.out.println("0. Exit");
    }

    private void handleStudentLogin() {
        System.out.println("\n----- STUDENT LOGIN -----");
        System.out.println("1. Register as New Student");
        System.out.println("2. Login with Email");
        System.out.println("0. Back");
        
        int choice = readInt("Enter choice: ");
        
        switch (choice) {
            case 1:
                registerStudent();
                break;
            case 2:
                loginAsStudent();
                break;
            default:
                return;
        }
    }

    private void registerStudent() {
        System.out.println("\n----- STUDENT REGISTRATION -----");
        String name = readLine("Enter Name: ");
        String email = readLine("Enter Email: ");
        String phone = readLine("Enter Phone: ");
        
        try {
            Student student = studentService.createStudent(name, email, phone);
            System.out.println("Registration successful! Your ID: " + student.getId());
            loginStudent(student);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void loginAsStudent() {
        String email = readLine("Enter Email: ");
        Student student = studentService.getStudentByEmail(email);
        
        if (student != null) {
            System.out.println("Login successful!");
            loginStudent(student);
        } else {
            System.out.println("Student not found. Please register first.");
        }
    }

    private void loginStudent(Student student) {
        boolean running = true;
        while (running) {
            showStudentMenu();
            int choice = readInt("Enter choice: ");
            
            switch (choice) {
                case 1:
                    viewStudentProfile(student);
                    break;
                case 2:
                    updateStudentProfile(student);
                    break;
                case 3:
                    uploadResume(student);
                    break;
                case 4:
                    browseJobs();
                    break;
                case 5:
                    applyToJob(student);
                    break;
                case 6:
                    viewMyApplications(student);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showStudentMenu() {
        System.out.println("\n----- STUDENT MENU -----");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. Upload Resume");
        System.out.println("4. Browse Jobs");
        System.out.println("5. Apply to Job");
        System.out.println("6. View My Applications");
        System.out.println("0. Logout");
    }

    private void viewStudentProfile(Student student) {
        System.out.println("\n----- YOUR PROFILE -----");
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Phone: " + student.getPhone());
        System.out.println("Resume: " + (student.hasResume() ? "Uploaded" : "Not Uploaded"));
    }

    private void updateStudentProfile(Student student) {
        System.out.println("\n----- UPDATE PROFILE -----");
        System.out.println("Leave blank to keep current value.");
        
        String name = readLine("New Name (" + student.getName() + "): ");
        String email = readLine("New Email (" + student.getEmail() + "): ");
        String phone = readLine("New Phone (" + student.getPhone() + "): ");
        
        studentService.updateStudent(student.getId(), 
                name.isEmpty() ? null : name,
                email.isEmpty() ? null : email,
                phone.isEmpty() ? null : phone);
        
        System.out.println("Profile updated successfully!");
    }

    private void uploadResume(Student student) {
        System.out.println("\n----- UPLOAD RESUME -----");
        System.out.println("Enter your resume text (skills, experience, etc.):");
        System.out.println("Press Enter twice to finish or type 'cancel' to abort:");
        
        StringBuilder resume = new StringBuilder();
        String line;
        int emptyLineCount = 0;
        
        while ((line = scanner.nextLine()) != null) {
            if (line.equalsIgnoreCase("cancel")) {
                System.out.println("Upload cancelled.");
                return;
            }
            if (line.trim().isEmpty()) {
                emptyLineCount++;
                if (emptyLineCount >= 2) break;
            } else {
                emptyLineCount = 0;
                resume.append(line).append("\n");
            }
        }
        
        if (resume.length() > 0) {
            studentService.uploadResume(student.getId(), resume.toString().trim());
            System.out.println("Resume uploaded successfully!");
        } else {
            System.out.println("No resume content entered.");
        }
    }

    private void browseJobs() {
        System.out.println("\n----- BROWSE JOBS -----");
        List<Job> allJobs = jobService.getAllJobs();
        
        if (allJobs.isEmpty()) {
            System.out.println("No jobs available.");
            return;
        }
        
        System.out.println("1. View All Jobs");
        System.out.println("2. Search by Keyword");
        System.out.println("3. Filter by Skills");
        
        int choice = readInt("Enter choice: ");
        
        switch (choice) {
            case 1:
                jobService.displayJobs(allJobs);
                break;
            case 2:
                String keyword = readLine("Enter search keyword: ");
                jobService.displayJobs(jobService.searchJobs(keyword));
                break;
            case 3:
                List<String> skills = parseSkills(readLine("Enter skills (comma separated): "));
                jobService.displayJobs(jobService.getJobsBySkills(skills));
                break;
            default:
                jobService.displayJobs(allJobs);
        }
    }

    private void applyToJob(Student student) {
        System.out.println("\n----- APPLY TO JOB -----");
        
        if (!student.hasResume()) {
            System.out.println("Please upload your resume first before applying.");
            return;
        }
        
        List<Job> allJobs = jobService.getAllJobs();
        if (allJobs.isEmpty()) {
            System.out.println("No jobs available.");
            return;
        }
        
        jobService.displayJobs(allJobs);
        int jobId = readInt("Enter Job ID to apply: ");
        
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            System.out.println("Invalid job ID.");
            return;
        }

        ResumeAnalyzer.ResumeAnalysisResult analysis = 
            ResumeAnalyzer.analyzeWithFeedback(student.getResume(), job);
        ResumeAnalyzer.displayAnalysis(analysis);

        String confirm = readLine("\nDo you still want to apply? (Y/N): ");
        if (confirm == null || !confirm.trim().equalsIgnoreCase("Y")) {
            System.out.println("Application cancelled.");
            return;
        }
        
        try {
            Application app = applicationService.applyToJob(student, job);
            System.out.println("\nApplication submitted successfully!");
            System.out.println("Application ID: " + app.getId());
            System.out.println("Match Score: " + app.getMatchScore() + "%");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewMyApplications(Student student) {
        System.out.println("\n----- MY APPLICATIONS -----");
        List<Application> apps = applicationService.getApplicationsByStudent(student);
        
        if (apps.isEmpty()) {
            System.out.println("You haven't applied to any jobs.");
            return;
        }
        
        applicationService.displayApplications(apps, false);
    }

    private void handleCompanyLogin() {
        System.out.println("\n----- COMPANY LOGIN -----");
        System.out.println("1. Register as New Company");
        System.out.println("2. Login with Email");
        System.out.println("0. Back");
        
        int choice = readInt("Enter choice: ");
        
        switch (choice) {
            case 1:
                registerCompany();
                break;
            case 2:
                loginAsCompany();
                break;
            default:
                return;
        }
    }

    private void registerCompany() {
        System.out.println("\n----- COMPANY REGISTRATION -----");
        String name = readLine("Enter Company Name: ");
        String email = readLine("Enter Company Email: ");
        
        try {
            Company company = companyService.createCompany(name, email);
            System.out.println("Registration successful! Your Company ID: " + company.getId());
            loginCompany(company);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void loginAsCompany() {
        String email = readLine("Enter Company Email: ");
        Company company = companyService.getCompanyByEmail(email);
        
        if (company != null) {
            System.out.println("Login successful!");
            loginCompany(company);
        } else {
            System.out.println("Company not found. Please register first.");
        }
    }

    private void loginCompany(Company company) {
        boolean running = true;
        while (running) {
            showCompanyMenu();
            int choice = readInt("Enter choice: ");
            
            switch (choice) {
                case 1:
                    viewCompanyProfile(company);
                    break;
                case 2:
                    postJob(company);
                    break;
                case 3:
                    viewPostedJobs(company);
                    break;
                case 4:
                    viewApplicants(company);
                    break;
                case 5:
                    filterApplicants(company);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showCompanyMenu() {
        System.out.println("\n----- COMPANY MENU -----");
        System.out.println("1. View Company Profile");
        System.out.println("2. Post New Job/Internship");
        System.out.println("3. View Posted Jobs");
        System.out.println("4. View Applicants for a Job");
        System.out.println("5. Filter Applicants by Score");
        System.out.println("0. Logout");
    }

    private void viewCompanyProfile(Company company) {
        System.out.println("\n----- COMPANY PROFILE -----");
        System.out.println("ID: " + company.getId());
        System.out.println("Name: " + company.getName());
        System.out.println("Email: " + company.getEmail());
        
        List<Job> companyJobs = jobService.getJobsByCompany(company);
        System.out.println("Total Jobs Posted: " + companyJobs.size());
    }

    private void postJob(Company company) {
        System.out.println("\n----- POST NEW JOB -----");
        String title = readLine("Enter Job Title: ");
        String description = readLine("Enter Job Description: ");
        String skillsInput = readLine("Enter Required Skills (comma separated): ");
        List<String> skills = parseSkills(skillsInput);
        
        try {
            Job job = jobService.createJob(title, description, skills, company);
            System.out.println("Job posted successfully! Job ID: " + job.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewPostedJobs(Company company) {
        System.out.println("\n----- YOUR POSTED JOBS -----");
        List<Job> jobs = jobService.getJobsByCompany(company);
        
        if (jobs.isEmpty()) {
            System.out.println("You haven't posted any jobs yet.");
            return;
        }
        
        jobService.displayJobs(jobs);
    }

    private void viewApplicants(Company company) {
        System.out.println("\n----- VIEW APPLICANTS -----");
        List<Job> jobs = jobService.getJobsByCompany(company);
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs posted yet.");
            return;
        }
        
        jobService.displayJobs(jobs);
        int jobId = readInt("Enter Job ID to view applicants: ");
        
        Job job = jobService.getJobById(jobId);
        if (job == null || job.getCompany().getId() != company.getId()) {
            System.out.println("Invalid job ID.");
            return;
        }
        
        List<Application> apps = applicationService.getApplicationsByJob(job);
        
        if (apps.isEmpty()) {
            System.out.println("No applicants for this job.");
            return;
        }
        
        System.out.println("\nApplicants for: " + job.getTitle());
        applicationService.displayApplications(apps, true);
    }

    private void filterApplicants(Company company) {
        System.out.println("\n----- FILTER APPLICANTS -----");
        List<Job> jobs = jobService.getJobsByCompany(company);
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs posted yet.");
            return;
        }
        
        jobService.displayJobs(jobs);
        int jobId = readInt("Enter Job ID: ");
        
        Job job = jobService.getJobById(jobId);
        if (job == null || job.getCompany().getId() != company.getId()) {
            System.out.println("Invalid job ID.");
            return;
        }
        
        int minScore = readInt("Enter minimum match score (0-100): ");
        
        List<Application> apps = applicationService.getApplicationsByJob(job);
        List<Application> filtered = apps.stream()
                .filter(app -> app.getMatchScore() >= minScore)
                .sorted(Comparator.comparing(Application::getMatchScore).reversed())
                .toList();
        
        if (filtered.isEmpty()) {
            System.out.println("No applicants match the criteria.");
            return;
        }
        
        System.out.println("\nFiltered Applicants (Score >= " + minScore + "%):");
        applicationService.displayApplications(filtered, true);
    }

    private void handleAdminLogin() {
        System.out.println("\n----- ADMIN LOGIN -----");
        String password = readLine("Enter Admin Password: ");
        
        if (!password.equals("admin123")) {
            System.out.println("Invalid password.");
            return;
        }
        
        System.out.println("Admin login successful!");
        adminMenu();
    }

    private void adminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n----- ADMIN MENU -----");
            System.out.println("1. View All Students");
            System.out.println("2. View All Companies");
            System.out.println("3. View All Jobs");
            System.out.println("4. View All Applications");
            System.out.println("5. Add Sample Data");
            System.out.println("0. Logout");
            
            int choice = readInt("Enter choice: ");
            
            switch (choice) {
                case 1:
                    studentService.displayStudents(studentService.getAllStudents());
                    break;
                case 2:
                    companyService.displayCompanies(companyService.getAllCompanies());
                    break;
                case 3:
                    jobService.displayJobs(jobService.getAllJobs());
                    break;
                case 4:
                    List<Application> allApps = new ArrayList<>();
                    for (Student s : studentService.getAllStudents()) {
                        allApps.addAll(applicationService.getApplicationsByStudent(s));
                    }
                    applicationService.displayApplications(allApps, false);
                    break;
                case 5:
                    addSampleData();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addSampleData() {
        System.out.println("Adding sample data...");
        
        Student s1 = studentService.createStudent("John Doe", "john@email.com", "1234567890");
        studentService.uploadResume(s1.getId(), "Java Python SQL Git React HTML CSS Node.js experience in web development");
        
        Student s2 = studentService.createStudent("Jane Smith", "jane@email.com", "9876543210");
        studentService.uploadResume(s2.getId(), "Python Machine Learning Data Science TensorFlow SQL statistics");
        
        Student s3 = studentService.createStudent("Bob Wilson", "bob@email.com", "5551234567");
        studentService.uploadResume(s3.getId(), "Java Spring Boot Microservices Docker Kubernetes AWS cloud");
        
        Company c1 = companyService.createCompany("Tech Corp", "tech@company.com");
        Company c2 = companyService.createCompany("Data Solutions", "data@company.com");
        
        jobService.createJob("Software Engineer", "Full stack developer needed", 
                Arrays.asList("Java", "React", "SQL", "Git"), c1);
        jobService.createJob("Data Analyst", "Data analyst for business intelligence",
                Arrays.asList("Python", "SQL", "Excel", "Tableau"), c2);
        jobService.createJob("Backend Developer", "Java Spring Boot developer",
                Arrays.asList("Java", "Spring", "Docker", "AWS"), c1);
        
        System.out.println("Sample data added successfully!");
    }

    private List<String> parseSkills(String input) {
        List<String> skills = new ArrayList<>();
        if (input != null && !input.trim().isEmpty()) {
            String[] parts = input.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    skills.add(trimmed);
                }
            }
        }
        return skills;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        try {
            System.out.print(prompt);
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}