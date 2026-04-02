package gui;

import entity.*;
import service.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginUI extends JPanel {
    private HireHubGUI mainFrame;
    private StudentService studentService;
    private CompanyService companyService;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public LoginUI(HireHubGUI mainFrame, StudentService studentService, CompanyService companyService) {
        this.mainFrame = mainFrame;
        this.studentService = studentService;
        this.companyService = companyService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        
        JLabel titleLabel = new JLabel("Hire Hub - Job Portal System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel roleLabel = new JLabel("Login As:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleCombo = new JComboBox<>(new String[]{"Student", "Company", "Admin"});
        roleCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        roleCombo.setPreferredSize(new Dimension(200, 30));
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(roleCombo, gbc);
        
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 30));
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 30));
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        JButton loginBtn = createStyledButton("Login", new Color(0, 123, 255));
        loginBtn.setPreferredSize(new Dimension(120, 35));
        loginBtn.addActionListener(e -> handleLogin());
        
        JButton registerBtn = createStyledButton("Register", null);
        registerBtn.setPreferredSize(new Dimension(120, 35));
        registerBtn.addActionListener(e -> showRegisterDialog());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);
        
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        
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

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (role.equals("Student")) {
            Student student = studentService.loginStudent(email, password);
            if (student != null) {
                mainFrame.showStudentDashboard(student);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (role.equals("Company")) {
            Company company = companyService.loginCompany(email, password);
            if (company != null) {
                mainFrame.showCompanyDashboard(company);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (role.equals("Admin")) {
            if (password.equals("admin123")) {
                mainFrame.showAdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRegisterDialog() {
        String role = (String) roleCombo.getSelectedItem();
        
        if (role.equals("Admin")) {
            JOptionPane.showMessageDialog(this, "Admin registration is not available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Register " + role, true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(200, 30));
        JTextField emailFieldReg = new JTextField(20);
        emailFieldReg.setPreferredSize(new Dimension(200, 30));
        JTextField phoneField = new JTextField(20);
        phoneField.setPreferredSize(new Dimension(200, 30));
        JPasswordField passwordFieldReg = new JPasswordField(20);
        passwordFieldReg.setPreferredSize(new Dimension(200, 30));
        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailFieldReg, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordFieldReg, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        
        JButton registerBtn = createStyledButton("Register", new Color(0, 123, 255));
        registerBtn.setPreferredSize(new Dimension(120, 35));
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailFieldReg.getText().trim();
            String phone = phoneField.getText().trim();
            String password = new String(passwordFieldReg.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name, email, and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                if (role.equals("Student")) {
                    Student student = studentService.createStudent(name, email, phone, password);
                    JOptionPane.showMessageDialog(dialog, 
                        "Registration successful! Your ID: " + student.getId());
                    dialog.dispose();
                    mainFrame.showStudentDashboard(student);
                } else {
                    Company company = companyService.createCompany(name, email, password);
                    JOptionPane.showMessageDialog(dialog, 
                        "Registration successful! Your Company ID: " + company.getId());
                    dialog.dispose();
                    mainFrame.showCompanyDashboard(company);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelBtn = createStyledButton("Cancel", null);
        cancelBtn.setPreferredSize(new Dimension(120, 35));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(registerBtn);
        btnPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}