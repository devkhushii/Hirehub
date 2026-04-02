package entity;

public class Student {
    private static int idCounter = 1;
    private final int id;
    private String name;
    private String email;
    private String phone;
    private String resume;
    private String password;

    public Student(String name, String email, String phone, String password) {
        this.id = idCounter++;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.resume = "";
        this.password = password;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }
    public boolean hasResume() { return resume != null && !resume.trim().isEmpty(); }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean checkPassword(String pwd) { return password != null && password.equals(pwd); }

    @Override
    public String toString() {
        return String.format("Student[ID: %d, Name: %s, Email: %s, Phone: %s, Resume: %s]",
                id, name, email, phone, hasResume() ? "Uploaded" : "Not Uploaded");
    }
}