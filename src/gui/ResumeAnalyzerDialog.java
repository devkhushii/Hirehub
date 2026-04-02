package gui;

import entity.*;
import service.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResumeAnalyzerDialog extends JDialog {
    private Student student;
    private Job job;
    private ResumeAnalyzer.ResumeAnalysisResult analysis;
    private ApplicationService applicationService;
    private boolean applicationSubmitted = false;

    public ResumeAnalyzerDialog(Frame parent, Student student, Job job, 
                                ResumeAnalyzer.ResumeAnalysisResult analysis,
                                ApplicationService applicationService) {
        super(parent, "Resume Analysis - Apply to Job", true);
        this.student = student;
        this.job = job;
        this.analysis = analysis;
        this.applicationService = applicationService;
        initComponents();
    }

    private void initComponents() {
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Applying to: " + job.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel matchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel matchLabel = new JLabel("Match Percentage: " + analysis.getMatchPercentage() + "%");
        matchLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        Color matchColor;
        if (analysis.getMatchPercentage() >= 70) {
            matchColor = new Color(0, 150, 0);
        } else if (analysis.getMatchPercentage() >= 40) {
            matchColor = new Color(200, 150, 0);
        } else {
            matchColor = new Color(200, 0, 0);
        }
        matchLabel.setForeground(matchColor);
        matchPanel.add(matchLabel);
        
        contentPanel.add(matchPanel);
        
        if (!analysis.getMatchedSkills().isEmpty()) {
            JPanel matchedPanel = new JPanel();
            matchedPanel.setLayout(new BoxLayout(matchedPanel, BoxLayout.Y_AXIS));
            matchedPanel.setBorder(BorderFactory.createTitledBorder("Matched Skills"));
            
            JLabel matchedLabel = new JLabel("<html>" + String.join(", ", analysis.getMatchedSkills()) + "</html>");
            matchedLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            matchedPanel.add(matchedLabel);
            
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(matchedPanel);
        }
        
        if (!analysis.getMissingSkills().isEmpty()) {
            JPanel missingPanel = new JPanel();
            missingPanel.setLayout(new BoxLayout(missingPanel, BoxLayout.Y_AXIS));
            missingPanel.setBorder(BorderFactory.createTitledBorder("Missing Keywords (Suggestions)"));
            
            for (String skill : analysis.getMissingSkills()) {
                JLabel skillLabel = new JLabel("• Add '" + skill + "'");
                skillLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                missingPanel.add(skillLabel);
            }
            
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(missingPanel);
        }
        
        JPanel suggestionPanel = new JPanel();
        suggestionPanel.setLayout(new BoxLayout(suggestionPanel, BoxLayout.Y_AXIS));
        suggestionPanel.setBorder(BorderFactory.createTitledBorder("Tips to Improve"));
        suggestionPanel.add(new JLabel("<html><ul>", SwingConstants.LEFT));
        
        for (String skill : analysis.getTopMissingSkills()) {
            suggestionPanel.add(new JLabel("<li>Add '" + skill + "' to your resume</li>"));
        }
        
        if (analysis.getMissingSkills().size() > 5) {
            suggestionPanel.add(new JLabel("<li>... and " + (analysis.getMissingSkills().size() - 5) + " more skills</li>"));
        }
        
        suggestionPanel.add(new JLabel("</ul></html>"));
        
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(suggestionPanel);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        JButton yesBtn = new JButton("Yes, Apply Anyway");
        yesBtn.setBackground(new Color(0, 123, 255));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setFont(new Font("Arial", Font.BOLD, 14));
        yesBtn.addActionListener(e -> submitApplication());
        
        JButton noBtn = new JButton("No, Cancel");
        noBtn.setFont(new Font("Arial", Font.BOLD, 14));
        noBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Application cancelled. Consider improving your resume first!");
            dispose();
        });
        
        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);
        
        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void submitApplication() {
        try {
            Application app = applicationService.applyToJob(student, job);
            JOptionPane.showMessageDialog(this, 
                "Application submitted successfully!\n\n" +
                "Application ID: " + app.getId() + "\n" +
                "Match Score: " + app.getMatchScore() + "%");
            applicationSubmitted = true;
            dispose();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isApplicationSubmitted() {
        return applicationSubmitted;
    }
}