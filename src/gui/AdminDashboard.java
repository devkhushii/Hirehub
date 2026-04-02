package gui;

import entity.*;
import service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JPanel {
    private HireHubGUI mainFrame;
    private StudentService studentService;
    private CompanyService companyService;
    private JobService jobService;
    private ApplicationService applicationService;
    
    private JTable studentsTable, companiesTable, jobsTable, applicationsTable;
    private DefaultTableModel studentsModel, companiesModel, jobsModel, applicationsModel;

    public AdminDashboard(HireHubGUI mainFrame, StudentService studentService, 
                         CompanyService companyService, JobService jobService,
                         ApplicationService applicationService) {
        this.mainFrame = mainFrame;
        this.studentService = studentService;
        this.companyService = companyService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        initComponents();
        refreshAll();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header panel with welcome and logout
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel headerLabel = new JLabel("Admin Dashboard - System Overview");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 102, 204));
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(220, 220, 220));
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setPreferredSize(new Dimension(100, 30));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.showLoginPanel();
            }
        });
        
        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setPreferredSize(new Dimension(800, 500));
        
        tabbedPane.addTab("Students", createStudentsPanel());
        tabbedPane.addTab("Companies", createCompaniesPanel());
        tabbedPane.addTab("Jobs", createJobsPanel());
        tabbedPane.addTab("Applications", createApplicationsPanel());
        
        // Button panel with BoxLayout
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton addSampleBtn = createStyledButton("Add Sample Data", new Color(0, 123, 255));
        addSampleBtn.addActionListener(e -> addSampleData());
        
        JButton refreshBtn = createStyledButton("Refresh All", null);
        refreshBtn.addActionListener(e -> refreshAll());
        
        btnPanel.add(addSampleBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(refreshBtn);
        btnPanel.add(Box.createHorizontalGlue());
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(140, 35));
        
        if (bgColor != null) {
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
        } else {
            button.setForeground(Color.BLACK);
            button.setBackground(new Color(230, 230, 230));
        }
        
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        return button;
    }
    
    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Name", "Email", "Phone", "Resume"};
        studentsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentsTable = new JTable(studentsModel);
        studentsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentsTable.setRowHeight(22);
        
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createCompaniesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Name", "Email", "Jobs Posted"};
        companiesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        companiesTable = new JTable(companiesModel);
        companiesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        companiesTable.setRowHeight(22);
        
        JScrollPane scrollPane = new JScrollPane(companiesTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Title", "Company", "Required Skills", "Applicants"};
        jobsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobsTable = new JTable(jobsModel);
        jobsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        jobsTable.setRowHeight(22);
        
        JScrollPane scrollPane = new JScrollPane(jobsTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Student", "Job", "Company", "Status", "Match %"};
        applicationsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicationsTable = new JTable(applicationsModel);
        applicationsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(new JScrollPane(applicationsTable), BorderLayout.CENTER);
        
        return panel;
    }

    private void refreshAll() {
        refreshStudents();
        refreshCompanies();
        refreshJobs();
        refreshApplications();
    }

    private void refreshStudents() {
        studentsModel.setRowCount(0);
        for (Student student : studentService.getAllStudents()) {
            studentsModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhone() != null ? student.getPhone() : "N/A",
                student.hasResume() ? "Uploaded" : "Not Uploaded"
            });
        }
    }

    private void refreshCompanies() {
        companiesModel.setRowCount(0);
        for (Company company : companyService.getAllCompanies()) {
            int jobCount = jobService.getJobsByCompany(company).size();
            companiesModel.addRow(new Object[]{
                company.getId(),
                company.getName(),
                company.getEmail(),
                jobCount
            });
        }
    }

    private void refreshJobs() {
        jobsModel.setRowCount(0);
        for (Job job : jobService.getAllJobs()) {
            int appCount = applicationService.getApplicationsByJob(job).size();
            jobsModel.addRow(new Object[]{
                job.getId(),
                job.getTitle(),
                job.getCompany().getName(),
                job.getSkillsAsString(),
                appCount
            });
        }
    }

    private void refreshApplications() {
        applicationsModel.setRowCount(0);
        for (Student student : studentService.getAllStudents()) {
            for (Application app : applicationService.getApplicationsByStudent(student)) {
                applicationsModel.addRow(new Object[]{
                    app.getId(),
                    app.getStudent().getName(),
                    app.getJob().getTitle(),
                    app.getJob().getCompany().getName(),
                    app.getStatus(),
                    app.getMatchScore() + "%"
                });
            }
        }
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

        JOptionPane.showMessageDialog(this, 
            "Sample data added successfully!\n\n" +
            "Students: 5\nCompanies: 4\nJobs: 11\nApplications: 5\n\n" +
            "Test Login:\nStudent: john@email.com / password123\nCompany: tech@company.com / password123\nAdmin: (any) / admin123");
        refreshAll();
    }
}