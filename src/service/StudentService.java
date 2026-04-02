package service;

import entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService {
    private final List<Student> students;

    public StudentService() {
        this.students = new ArrayList<>();
    }

    public Student createStudent(String name, String email, String phone, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Student student = new Student(name, email, phone, password);
        students.add(student);
        return student;
    }

    public Student loginStudent(String email, String password) {
        return students.stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email) && s.checkPassword(password))
                .findFirst()
                .orElse(null);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getStudentById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Student getStudentByEmail(String email) {
        return students.stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public boolean updateStudent(int id, String name, String email, String phone) {
        Student student = getStudentById(id);
        if (student != null) {
            if (name != null && !name.trim().isEmpty()) {
                student.setName(name);
            }
            if (email != null && !email.trim().isEmpty()) {
                student.setEmail(email);
            }
            if (phone != null) {
                student.setPhone(phone);
            }
            return true;
        }
        return false;
    }

    public boolean uploadResume(int studentId, String resume) {
        Student student = getStudentById(studentId);
        if (student != null) {
            student.setResume(resume);
            return true;
        }
        return false;
    }

    public void displayStudents(List<Student> studentList) {
        if (studentList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.printf("%-5s | %-20s | %-25s | %s%n", "ID", "Name", "Email", "Resume");
        System.out.println("=".repeat(70));

        for (Student student : studentList) {
            System.out.printf("%-5d | %-20s | %-25s | %s%n",
                    student.getId(),
                    truncate(student.getName(), 20),
                    truncate(student.getEmail(), 25),
                    student.hasResume() ? "Uploaded" : "Not Uploaded");
        }
        System.out.println("=".repeat(70));
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    public int getStudentCount() {
        return students.size();
    }
}