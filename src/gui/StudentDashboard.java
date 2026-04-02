package gui;

import entity.*;
import service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JPanel {
    private HireHubGUI mainFrame;
    private Student student;
    private JobService jobService;
    private ApplicationService applicationService;
    private StudentService studentService;
    
    private JTable jobsTable;
    private DefaultTableModel jobsTableModel;
    private JTextArea resumeArea;
    private JTable applicationsTable;
    private DefaultTableModel applicationsTableModel;

    public StudentDashboard(HireHubGUI mainFrame, Student student, JobService jobService, 
                           ApplicationService applicationService, StudentService studentService) {
        this.mainFrame = mainFrame;
        this.student = student;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.studentService = studentService;
        initComponents();
        refreshJobs();
        refreshApplications();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header with welcome message and logout
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel headerLabel = new JLabel("Welcome, " + student.getName());
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
        
        // Tabbed pane for content
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setPreferredSize(new Dimension(800, 500));
        
        tabbedPane.addTab("My Profile", createProfilePanel());
        tabbedPane.addTab("Upload Resume", createResumePanel());
        tabbedPane.addTab("Browse Jobs", createJobsPanel());
        tabbedPane.addTab("My Applications", createApplicationsPanel());
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        
        JLabel idLabel = new JLabel("Student ID: " + student.getId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel nameLabel = new JLabel("Name: " + student.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel emailLabel = new JLabel("Email: " + student.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel phoneLabel = new JLabel("Phone: " + (student.getPhone() != null ? student.getPhone() : "Not set"));
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel resumeLabel = new JLabel("Resume: " + (student.hasResume() ? "Uploaded" : "Not Uploaded"));
        resumeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JButton editBtn = createStyledButton("Edit Profile", new Color(0, 123, 255));
        editBtn.addActionListener(e -> showEditProfileDialog());
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(idLabel, gbc);
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        gbc.gridy = 2;
        panel.add(emailLabel, gbc);
        gbc.gridy = 3;
        panel.add(phoneLabel, gbc);
        gbc.gridy = 4;
        panel.add(resumeLabel, gbc);
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(editBtn, gbc);
        
        return panel;
    }

    private void showEditProfileDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Profile", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(student.getName(), 20);
        JTextField emailField = new JTextField(student.getEmail(), 20);
        JTextField phoneField = new JTextField(student.getPhone() != null ? student.getPhone() : "", 20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        JButton saveBtn = createStyledButton("Save", new Color(0, 123, 255));
        saveBtn.addActionListener(e -> {
            studentService.updateStudent(student.getId(), nameField.getText(), 
                emailField.getText(), phoneField.getText());
            dialog.dispose();
            mainFrame.showStudentDashboard(studentService.getStudentById(student.getId()));
            JOptionPane.showMessageDialog(mainFrame, "Profile updated successfully!");
        });
        
        JButton cancelBtn = createStyledButton("Cancel", null);
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel createResumePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel infoLabel = new JLabel("<html><b>Upload your resume</b> to apply for jobs. Include your skills, experience, and keywords relevant to jobs you're interested in.</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        resumeArea = new JTextArea(12, 50);
        resumeArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resumeArea.setLineWrap(true);
        resumeArea.setWrapStyleWord(true);
        if (student.hasResume()) {
            resumeArea.setText(student.getResume());
        }
        
        JScrollPane scrollPane = new JScrollPane(resumeArea);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        // Button panel with BoxLayout for horizontal spacing
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton saveBtn = createStyledButton("Save Resume", new Color(0, 123, 255));
        saveBtn.addActionListener(e -> saveResume());
        
        JButton uploadFileBtn = createStyledButton("Upload from File", null);
        uploadFileBtn.addActionListener(e -> uploadFromFile());
        
        JButton clearBtn = createStyledButton("Clear", null);
        clearBtn.addActionListener(e -> resumeArea.setText(""));
        
        btnPanel.add(saveBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(uploadFileBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(clearBtn);
        btnPanel.add(Box.createHorizontalGlue());
        
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void saveResume() {
        String resume = resumeArea.getText();
        if (resume.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter resume content!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        studentService.uploadResume(student.getId(), resume);
        JOptionPane.showMessageDialog(this, "Resume saved successfully!");
    }

    private void uploadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                resumeArea.setText(content);
                JOptionPane.showMessageDialog(this, "File loaded! Click 'Save Resume' to save.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Title", "Company", "Required Skills"};
        jobsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobsTable = new JTable(jobsTableModel);
        jobsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        jobsTable.setRowHeight(22);
        jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(jobsTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        // Button panel with BoxLayout
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton applyBtn = createStyledButton("Apply to Selected Job", new Color(0, 123, 255));
        applyBtn.addActionListener(e -> applyToJob());
        
        JButton refreshBtn = createStyledButton("Refresh Jobs", null);
        refreshBtn.addActionListener(e -> refreshJobs());
        
        JButton viewDetailsBtn = createStyledButton("View Job Details", null);
        viewDetailsBtn.addActionListener(e -> viewJobDetails());
        
        btnPanel.add(applyBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(refreshBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(viewDetailsBtn);
        btnPanel.add(Box.createHorizontalGlue());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void refreshJobs() {
        jobsTableModel.setRowCount(0);
        List<Job> jobs = jobService.getAllJobs();
        for (Job job : jobs) {
            jobsTableModel.addRow(new Object[]{
                job.getId(),
                job.getTitle(),
                job.getCompany().getName(),
                job.getSkillsAsString()
            });
        }
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
            "<b>Company:</b> " + job.getCompany().getName() + "<br>" +
            "<b>Required Skills:</b> " + job.getSkillsAsString() + "<br><br>" +
            "<b>Description:</b><br>" + job.getDescription() + "</html>";
        
        JOptionPane.showMessageDialog(this, details, "Job Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyToJob() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to apply!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!student.hasResume()) {
            JOptionPane.showMessageDialog(this, "Please upload your resume first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int jobId = (int) jobsTableModel.getValueAt(selectedRow, 0);
        Job job = jobService.getJobById(jobId);
        
        if (applicationService.hasAlreadyApplied(student.getId(), jobId)) {
            JOptionPane.showMessageDialog(this, "You have already applied to this job!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ResumeAnalyzer.ResumeAnalysisResult analysis = ResumeAnalyzer.analyzeWithFeedback(student.getResume(), job);
        
        ResumeAnalyzerDialog dialog = new ResumeAnalyzerDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            student, job, analysis, applicationService);
        dialog.setVisible(true);
        
        refreshApplications();
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Job Title", "Company", "Status", "Match %"};
        applicationsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicationsTable = new JTable(applicationsTableModel);
        applicationsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        applicationsTable.setRowHeight(22);
        
        JScrollPane scrollPane = new JScrollPane(applicationsTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        // Button panel with BoxLayout
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton refreshBtn = createStyledButton("Refresh Applications", null);
        refreshBtn.addActionListener(e -> refreshApplications());
        
        JButton viewDetailsBtn = createStyledButton("View Application Details", null);
        viewDetailsBtn.addActionListener(e -> viewApplicationDetails());
        
        btnPanel.add(refreshBtn);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(viewDetailsBtn);
        btnPanel.add(Box.createHorizontalGlue());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void refreshApplications() {
        applicationsTableModel.setRowCount(0);
        List<Application> apps = applicationService.getApplicationsByStudent(student);
        for (Application app : apps) {
            applicationsTableModel.addRow(new Object[]{
                app.getId(),
                app.getJob().getTitle(),
                app.getJob().getCompany().getName(),
                app.getStatus(),
                app.getMatchScore() + "%"
            });
        }
    }

    private void viewApplicationDetails() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application to view details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int appId = (int) applicationsTableModel.getValueAt(selectedRow, 0);
        Application app = applicationService.getApplicationById(appId);
        
        if (app != null) {
            String details = "<html><b>Application Details</b><br><br>" +
                "<b>Application ID:</b> " + app.getId() + "<br>" +
                "<b>Job Title:</b> " + app.getJob().getTitle() + "<br>" +
                "<b>Company:</b> " + app.getJob().getCompany().getName() + "<br>" +
                "<b>Status:</b> " + app.getStatus() + "<br>" +
                "<b>Match Score:</b> " + app.getMatchScore() + "%</html>";
            
            JOptionPane.showMessageDialog(this, details, "Application Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(150, 35));
        
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
}