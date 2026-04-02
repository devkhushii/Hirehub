package gui;

import entity.*;
import service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CompanyDashboard extends JPanel {
    private HireHubGUI mainFrame;
    private Company company;
    private JobService jobService;
    private ApplicationService applicationService;
    
    private JTable jobsTable;
    private DefaultTableModel jobsTableModel;
    private JTable applicantsTable;
    private DefaultTableModel applicantsTableModel;

    public CompanyDashboard(HireHubGUI mainFrame, Company company, JobService jobService, 
                           ApplicationService applicationService) {
        this.mainFrame = mainFrame;
        this.company = company;
        this.jobService = jobService;
        this.applicationService = applicationService;
        initComponents();
        refreshJobs();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header panel with welcome and logout
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel headerLabel = new JLabel("Welcome, " + company.getName());
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
        
        tabbedPane.addTab("Company Profile", createProfilePanel());
        tabbedPane.addTab("Post Jobs", createPostJobsPanel());
        tabbedPane.addTab("Manage Jobs", createManageJobsPanel());
        tabbedPane.addTab("View Applicants", createApplicantsPanel());
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        List<Job> companyJobs = jobService.getJobsByCompany(company);
        
        JLabel idLabel = new JLabel("Company ID: " + company.getId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel nameLabel = new JLabel("Company Name: " + company.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel emailLabel = new JLabel("Email: " + company.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel jobsLabel = new JLabel("Total Jobs Posted: " + companyJobs.size());
        jobsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(idLabel, gbc);
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        gbc.gridy = 2;
        panel.add(emailLabel, gbc);
        gbc.gridy = 3;
        panel.add(jobsLabel, gbc);
        
        return panel;
    }

    private JPanel createPostJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Post New Job"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField titleField = new JTextField(25);
        JTextArea descArea = new JTextArea(5, 25);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JTextField skillsField = new JTextField(25);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Required Skills:"), gbc);
        gbc.gridx = 1;
        formPanel.add(skillsField, gbc);
        
        JLabel helpLabel = new JLabel("(Separate skills with commas, e.g., Java, Python, SQL)");
        helpLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(helpLabel, gbc);
        
        JButton postBtn = createStyledButton("Post Job", new Color(0, 123, 255));
        postBtn.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String desc = descArea.getText().trim();
                String skillsInput = skillsField.getText().trim();
                
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Job title is required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                java.util.List<String> skills = java.util.Arrays.asList(skillsInput.split(","));
                skills.replaceAll(String::trim);
                
                Job job = jobService.createJob(title, desc, skills, company);
                JOptionPane.showMessageDialog(panel, "Job posted successfully! Job ID: " + job.getId());
                
                titleField.setText("");
                descArea.setText("");
                skillsField.setText("");
                refreshJobs();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(postBtn, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
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

    private JPanel createManageJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        String[] columns = {"ID", "Title", "Description", "Required Skills", "Applicants"};
        jobsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobsTable = new JTable(jobsTableModel);
        jobsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        jobsTable.setRowHeight(22);
        
        JScrollPane scrollPane = new JScrollPane(jobsTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        // Button panel with BoxLayout
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton refreshBtn = createStyledButton("Refresh Jobs", null);
        refreshBtn.addActionListener(e -> refreshJobs());
        
        JButton viewDetailsBtn = createStyledButton("View Job Details", null);
        viewDetailsBtn.addActionListener(e -> viewJobDetails());
        
        btnPanel.add(refreshBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(viewDetailsBtn);
        btnPanel.add(Box.createHorizontalGlue());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void viewJobDetails() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to view details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int jobId = (int) jobsTableModel.getValueAt(selectedRow, 0);
        Job job = jobService.getJobById(jobId);
        
        String details = "<html><b>Job Details</b><br><br>" +
            "<b>Title:</b> " + job.getTitle() + "<br>" +
            "<b>Required Skills:</b> " + job.getSkillsAsString() + "<br><br>" +
            "<b>Description:</b><br>" + job.getDescription() + "</html>";
        
        JOptionPane.showMessageDialog(this, details, "Job Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshJobs() {
        jobsTableModel.setRowCount(0);
        List<Job> jobs = jobService.getJobsByCompany(company);
        for (Job job : jobs) {
            int applicantCount = applicationService.getApplicationsByJob(job).size();
            jobsTableModel.addRow(new Object[]{
                job.getId(),
                job.getTitle(),
                truncate(job.getDescription(), 50),
                job.getSkillsAsString(),
                applicantCount
            });
        }
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    private JPanel createApplicantsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JPanel jobSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jobSelectPanel.add(new JLabel("Select Job:"));
        
        JComboBox<Job> jobCombo = new JComboBox<>();
        for (Job job : jobService.getJobsByCompany(company)) {
            jobCombo.addItem(job);
        }
        
        jobSelectPanel.add(jobCombo);
        
        String[] columns = {"App ID", "Student Name", "Email", "Status", "Match %", "Matched Skills"};
        applicantsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicantsTable = new JTable(applicantsTableModel);
        applicantsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        applicantsTable.setRowHeight(22);
        
        JScrollPane scrollPane = new JScrollPane(applicantsTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        // Button panel with BoxLayout
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton viewBtn = createStyledButton("View Applicants", new Color(0, 123, 255));
        viewBtn.addActionListener(e -> {
            Job selectedJob = (Job) jobCombo.getSelectedItem();
            if (selectedJob == null) {
                JOptionPane.showMessageDialog(panel, "No job selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            refreshApplicants(selectedJob);
        });
        
        JButton viewDetailsBtn = createStyledButton("View Resume Analysis", null);
        viewDetailsBtn.addActionListener(e -> {
            int selectedRow = applicantsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select an applicant!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int appId = (int) applicantsTableModel.getValueAt(selectedRow, 0);
            Application app = applicationService.getApplicationById(appId);
            if (app != null) {
                showApplicationDetails(app);
            }
        });
        
        btnPanel.add(viewBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(viewDetailsBtn);
        btnPanel.add(Box.createHorizontalGlue());
        
        panel.add(jobSelectPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void refreshApplicants(Job job) {
        applicantsTableModel.setRowCount(0);
        List<Application> apps = applicationService.getApplicationsByJob(job);
        for (Application app : apps) {
            List<String> matchedSkills = ResumeAnalyzer.findMatchingSkills(
                app.getStudent().getResume(), job.getRequiredSkills());
            applicantsTableModel.addRow(new Object[]{
                app.getId(),
                app.getStudent().getName(),
                app.getStudent().getEmail(),
                app.getStatus(),
                app.getMatchScore() + "%",
                String.join(", ", matchedSkills)
            });
        }
    }

    private void showApplicationDetails(Application app) {
        String resume = app.getStudent().getResume();
        Job job = app.getJob();
        
        ResumeAnalyzer.ResumeAnalysisResult analysis = ResumeAnalyzer.analyzeWithFeedback(resume, job);
        
        StringBuilder details = new StringBuilder();
        details.append("=== Resume Analysis for ").append(app.getStudent().getName()).append(" ===\n\n");
        details.append("Match Percentage: ").append(analysis.getMatchPercentage()).append("%\n\n");
        
        details.append("Matched Skills:\n");
        for (String skill : analysis.getMatchedSkills()) {
            details.append("  + ").append(skill).append("\n");
        }
        
        details.append("\nMissing Skills (should add):\n");
        for (String skill : analysis.getMissingSkills()) {
            details.append("  - ").append(skill).append("\n");
        }
        
        details.append("\nStatus: ").append(app.getStatus());
        
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), 
            "Applicant Resume Analysis", JOptionPane.INFORMATION_MESSAGE);
    }
}