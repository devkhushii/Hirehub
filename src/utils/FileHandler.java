package utils;

import entity.*;
import service.*;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final String STUDENTS_FILE = DATA_DIR + "/students.txt";
    private static final String COMPANIES_FILE = DATA_DIR + "/companies.txt";
    private static final String JOBS_FILE = DATA_DIR + "/jobs.txt";
    private static final String APPLICATIONS_FILE = DATA_DIR + "/applications.txt";

    public static void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static void saveStudents(StudentService studentService) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student student : studentService.getAllStudents()) {
                writer.println("ID:" + student.getId());
                writer.println("Name:" + student.getName());
                writer.println("Email:" + student.getEmail());
                writer.println("Phone:" + (student.getPhone() != null ? student.getPhone() : ""));
                writer.println("Password:" + (student.getPassword() != null ? student.getPassword() : ""));
                String resume = student.getResume() != null ? student.getResume().replace("\n", "\\n") : "";
                writer.println("Resume:" + resume);
                writer.println("---");
            }
            System.out.println("Students saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    public static void loadStudents(StudentService studentService) {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            Student student = null;
            String name = "", email = "", phone = "", password = "", resume = "";

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("ID:")) {
                    student = new Student("", "", "", "");
                } else if (line.startsWith("Name:")) {
                    name = line.substring(5);
                } else if (line.startsWith("Email:")) {
                    email = line.substring(6);
                } else if (line.startsWith("Phone:")) {
                    phone = line.substring(6);
                } else if (line.startsWith("Password:")) {
                    password = line.substring(9);
                } else if (line.startsWith("Resume:")) {
                    resume = line.substring(7).replace("\\n", "\n");
                } else if (line.equals("---") && student != null) {
                    student.setName(name);
                    student.setEmail(email);
                    student.setPhone(phone.isEmpty() ? null : phone);
                    student.setPassword(password.isEmpty() ? null : password);
                    if (!resume.isEmpty()) {
                        student.setResume(resume);
                    }
                    studentService.getAllStudents().add(student);
                    student = null;
                }
            }
            System.out.println("Students loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    public static void saveCompanies(CompanyService companyService) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COMPANIES_FILE))) {
            for (Company company : companyService.getAllCompanies()) {
                writer.println("ID:" + company.getId());
                writer.println("Name:" + company.getName());
                writer.println("Email:" + company.getEmail());
                writer.println("Password:" + (company.getPassword() != null ? company.getPassword() : ""));
                writer.println("---");
            }
            System.out.println("Companies saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving companies: " + e.getMessage());
        }
    }

    public static void loadCompanies(CompanyService companyService) {
        File file = new File(COMPANIES_FILE);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            String name = "", email = "", password = "";

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Name:")) {
                    name = line.substring(5);
                } else if (line.startsWith("Email:")) {
                    email = line.substring(6);
                } else if (line.startsWith("Password:")) {
                    password = line.substring(9);
                } else if (line.equals("---")) {
                    Company company = new Company(name, email, password);
                    companyService.getAllCompanies().add(company);
                }
            }
            System.out.println("Companies loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading companies: " + e.getMessage());
        }
    }

    public static void saveJobs(JobService jobService, CompanyService companyService) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(JOBS_FILE))) {
            for (Job job : jobService.getAllJobs()) {
                writer.println("ID:" + job.getId());
                writer.println("Title:" + job.getTitle());
                writer.println("Description:" + job.getDescription().replace("\n", "\\n"));
                writer.println("CompanyID:" + job.getCompany().getId());
                writer.println("Skills:" + String.join(",", job.getRequiredSkills()));
                writer.println("---");
            }
            System.out.println("Jobs saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving jobs: " + e.getMessage());
        }
    }

    public static void loadJobs(JobService jobService, CompanyService companyService) {
        File file = new File(JOBS_FILE);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            String title = "", description = "", skillsStr = "";
            int companyId = -1;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Title:")) {
                    title = line.substring(6);
                } else if (line.startsWith("Description:")) {
                    description = line.substring(12).replace("\\n", "\n");
                } else if (line.startsWith("CompanyID:")) {
                    companyId = Integer.parseInt(line.substring(10));
                } else if (line.startsWith("Skills:")) {
                    skillsStr = line.substring(7);
                } else if (line.equals("---")) {
                    Company company = companyService.getCompanyById(companyId);
                    if (company != null) {
                        List<String> skills = skillsStr.isEmpty() ? 
                            new ArrayList<>() : Arrays.asList(skillsStr.split(","));
                        Job job = new Job(title, description, skills, company);
                        jobService.getAllJobs().add(job);
                    }
                }
            }
            System.out.println("Jobs loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading jobs: " + e.getMessage());
        }
    }

    public static void saveApplications(ApplicationService appService, StudentService studentService, JobService jobService) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPLICATIONS_FILE))) {
            for (Application app : getAllApplicationsInternal(appService)) {
                writer.println("ID:" + app.getId());
                writer.println("StudentID:" + app.getStudent().getId());
                writer.println("JobID:" + app.getJob().getId());
                writer.println("Status:" + app.getStatus());
                writer.println("MatchScore:" + app.getMatchScore());
                writer.println("---");
            }
            System.out.println("Applications saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving applications: " + e.getMessage());
        }
    }

    private static List<Application> getAllApplicationsInternal(ApplicationService appService) {
        try {
            java.lang.reflect.Field field = ApplicationService.class.getDeclaredField("applications");
            field.setAccessible(true);
            return (List<Application>) field.get(appService);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void loadApplications(ApplicationService appService, StudentService studentService, JobService jobService) {
        File file = new File(APPLICATIONS_FILE);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            int studentId = -1, jobId = -1;
            Application.Status status = Application.Status.PENDING;
            int matchScore = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("StudentID:")) {
                    studentId = Integer.parseInt(line.substring(10));
                } else if (line.startsWith("JobID:")) {
                    jobId = Integer.parseInt(line.substring(6));
                } else if (line.startsWith("Status:")) {
                    status = Application.Status.valueOf(line.substring(7));
                } else if (line.startsWith("MatchScore:")) {
                    matchScore = Integer.parseInt(line.substring(11));
                } else if (line.equals("---")) {
                    Student student = studentService.getStudentById(studentId);
                    Job job = jobService.getJobById(jobId);
                    if (student != null && job != null) {
                        Application app = new Application(student, job);
                        app.setStatus(status);
                        app.setMatchScore(matchScore);
                        try {
                            java.lang.reflect.Field field = ApplicationService.class.getDeclaredField("applications");
                            field.setAccessible(true);
                            @SuppressWarnings("unchecked")
                            List<Application> apps = (List<Application>) field.get(appService);
                            apps.add(app);
                        } catch (Exception e) {
                            // Skip
                        }
                    }
                }
            }
            System.out.println("Applications loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading applications: " + e.getMessage());
        }
    }

    public static void saveAll(StudentService studentService, CompanyService companyService, 
                               JobService jobService, ApplicationService appService) {
        ensureDataDirectory();
        saveStudents(studentService);
        saveCompanies(companyService);
        saveJobs(jobService, companyService);
        saveApplications(appService, studentService, jobService);
    }

    public static void loadAll(StudentService studentService, CompanyService companyService, 
                               JobService jobService, ApplicationService appService) {
        ensureDataDirectory();
        loadStudents(studentService);
        loadCompanies(companyService);
        loadJobs(jobService, companyService);
        loadApplications(appService, studentService, jobService);
    }
}