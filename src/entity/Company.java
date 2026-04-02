package entity;

public class Company {
    private static int idCounter = 1;
    private final int id;
    private String name;
    private String email;
    private String password;

    public Company(String name, String email, String password) {
        this.id = idCounter++;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean checkPassword(String pwd) { return password != null && password.equals(pwd); }

    @Override
    public String toString() {
        return String.format("Company[ID: %d, Name: %s, Email: %s]", id, name, email);
    }
}