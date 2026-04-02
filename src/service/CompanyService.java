package service;

import entity.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyService {
    private final List<Company> companies;

    public CompanyService() {
        this.companies = new ArrayList<>();
    }

    public Company createCompany(String name, String email, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Company company = new Company(name, email, password);
        companies.add(company);
        return company;
    }

    public Company loginCompany(String email, String password) {
        return companies.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email) && c.checkPassword(password))
                .findFirst()
                .orElse(null);
    }

    public List<Company> getAllCompanies() {
        return new ArrayList<>(companies);
    }

    public Company getCompanyById(int id) {
        return companies.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Company getCompanyByEmail(String email) {
        return companies.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public boolean updateCompany(int id, String name, String email) {
        Company company = getCompanyById(id);
        if (company != null) {
            if (name != null && !name.trim().isEmpty()) {
                company.setName(name);
            }
            if (email != null && !email.trim().isEmpty()) {
                company.setEmail(email);
            }
            return true;
        }
        return false;
    }

    public void displayCompanies(List<Company> companyList) {
        if (companyList.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.printf("%-5s | %-25s | %s%n", "ID", "Name", "Email");
        System.out.println("=".repeat(60));

        for (Company company : companyList) {
            System.out.printf("%-5d | %-25s | %s%n",
                    company.getId(),
                    truncate(company.getName(), 25),
                    company.getEmail());
        }
        System.out.println("=".repeat(60));
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    public int getCompanyCount() {
        return companies.size();
    }
}