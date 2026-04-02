package gui;

import entity.*;
import service.*;
import utils.FileHandler;
import javax.swing.*;
import java.awt.*;

public class HireHubGUI extends JFrame {
    private StudentService studentService;
    private CompanyService companyService;
    private JobService jobService;
    private ApplicationService applicationService;
    
    private JPanel currentPanel;
    private Student currentStudent;
    private Company currentCompany;
    private boolean isAdminLoggedIn;

    public HireHubGUI() {
        studentService = new StudentService();
        companyService = new CompanyService();
        jobService = new JobService();
        applicationService = new ApplicationService();
        
        FileHandler.loadAll(studentService, companyService, jobService, applicationService);
        
        initFrame();
        showLoginPanel();
    }

    private void initFrame() {
        setTitle("Hire Hub - Job Portal & Internship Management System");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    public void showLoginPanel() {
        setJMenuBar(null);
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new LoginUI(this, studentService, companyService);
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showStudentDashboard(Student student) {
        this.currentStudent = student;
        this.currentCompany = null;
        this.isAdminLoggedIn = false;
        setJMenuBar(createMenuBar("student"));
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new StudentDashboard(this, student, jobService, applicationService, studentService);
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showCompanyDashboard(Company company) {
        this.currentCompany = company;
        this.currentStudent = null;
        this.isAdminLoggedIn = false;
        setJMenuBar(createMenuBar("company"));
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new CompanyDashboard(this, company, jobService, applicationService);
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showAdminDashboard() {
        this.isAdminLoggedIn = true;
        this.currentStudent = null;
        this.currentCompany = null;
        setJMenuBar(createMenuBar("admin"));
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new AdminDashboard(this, studentService, companyService, jobService, applicationService);
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JMenuBar createMenuBar(String role) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                showLoginPanel();
            }
        });
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            FileHandler.saveAll(studentService, companyService, jobService, applicationService);
            System.exit(0);
        });
        
        fileMenu.add(logoutItem);
        fileMenu.add(exitMenu());
        menuBar.add(fileMenu);
        
        return menuBar;
    }

    private JMenu exitMenu() {
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem addSampleItem = new JMenuItem("Add Sample Data");
        addSampleItem.addActionListener(e -> addSampleData());
        optionsMenu.add(addSampleItem);
        return optionsMenu;
    }

    private void addSampleData() {
        // Create Students
        Student s1 = studentService.createStudent("John Doe", "john@email.com", "1234567890", "password123");
        studentService.uploadResume(s1.getId(), "Java Python SQL Git React HTML CSS Node.js experience in web development");

        Student s2 = studentService.createStudent("Jane Smith", "jane@email.com", "9876543210", "password123");
        studentService.uploadResume(s2.getId(), "Python Machine Learning Data Science TensorFlow SQL statistics");

        Student s3 = studentService.createStudent("Bob Wilson", "bob@email.com", "5551234567", "password123");
        studentService.uploadResume(s3.getId(), "Java Spring Boot Microservices Docker Kubernetes AWS cloud");

        Student s4 = studentService.createStudent("Alice Johnson", "alice@email.com", "2223334444", "password123");
        studentService.uploadResume(s4.getId(), "C C++ Python Data Structures Algorithms Machine Learning");

        Student s5 = studentService.createStudent("Charlie Brown", "charlie@email.com", "3334445555", "password123");
        studentService.uploadResume(s5.getId(), "JavaScript HTML CSS React Node.js Express MongoDB Full Stack");

        // Create Companies
        Company c1 = companyService.createCompany("Tech Corp", "tech@company.com", "password123");
        Company c2 = companyService.createCompany("Data Solutions", "data@company.com", "password123");
        Company c3 = companyService.createCompany("WebTech Inc", "web@company.com", "password123");
        Company c4 = companyService.createCompany("Cloud Systems", "cloud@company.com", "password123");

        // Create Jobs for Tech Corp
        jobService.createJob("Software Engineer", "Full stack developer needed for our web applications. Must have experience with modern web technologies.", 
                java.util.Arrays.asList("Java", "React", "SQL", "Git"), c1);
        jobService.createJob("Backend Developer", "Java Spring Boot developer to build microservices architecture.",
                java.util.Arrays.asList("Java", "Spring", "Docker", "AWS"), c1);
        jobService.createJob("Junior Developer", "Entry level position for fresh graduates. Will train on job.",
                java.util.Arrays.asList("Java", "Python", "Git"), c1);

        // Create Jobs for Data Solutions
        jobService.createJob("Data Analyst", "Data analyst for business intelligence and reporting.",
                java.util.Arrays.asList("Python", "SQL", "Excel", "Tableau"), c2);
        jobService.createJob("Machine Learning Engineer", "Build and deploy ML models for predictive analytics.",
                java.util.Arrays.asList("Python", "Machine Learning", "TensorFlow", "SQL"), c2);
        jobService.createJob("Data Scientist", "Analyze complex datasets and derive insights.",
                java.util.Arrays.asList("Python", "Statistics", "R", "SQL"), c2);

        // Create Jobs for WebTech Inc
        jobService.createJob("Frontend Developer", "Create responsive web interfaces using React.",
                java.util.Arrays.asList("JavaScript", "React", "HTML", "CSS"), c3);
        jobService.createJob("Full Stack Developer", "Work on both frontend and backend of our products.",
                java.util.Arrays.asList("JavaScript", "Node.js", "React", "MongoDB"), c3);

        // Create Jobs for Cloud Systems
        jobService.createJob("DevOps Engineer", "Manage cloud infrastructure and CI/CD pipelines.",
                java.util.Arrays.asList("Docker", "Kubernetes", "AWS", "Jenkins"), c4);
        jobService.createJob("Cloud Architect", "Design cloud solutions for enterprise clients.",
                java.util.Arrays.asList("AWS", "Azure", "Docker", "Terraform"), c4);

        // Create some sample applications
        try {
            Job job1 = jobService.getJobById(1);
            Application app1 = applicationService.applyToJob(s1, job1);
            
            Job job2 = jobService.getJobById(2);
            Application app2 = applicationService.applyToJob(s3, job2);
            
            Job job4 = jobService.getJobById(4);
            Application app3 = applicationService.applyToJob(s2, job4);
            
            Job job5 = jobService.getJobById(5);
            Application app4 = applicationService.applyToJob(s4, job5);
            
            Job job8 = jobService.getJobById(8);
            Application app5 = applicationService.applyToJob(s5, job8);
        } catch (Exception e) {
            // Some applications may fail due to duplicate or other issues
        }

        FileHandler.saveAll(studentService, companyService, jobService, applicationService);
        
        JOptionPane.showMessageDialog(this, 
            "Sample data added successfully!\n\n" +
            "Students: 5\nCompanies: 4\nJobs: 11\nApplications: 5\n\n" +
            "Test Login:\nStudent: john@email.com / password123\nCompany: tech@company.com / password123\nAdmin: (any) / admin123");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HireHubGUI().setVisible(true);
        });
    }
}