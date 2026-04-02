import entity.*;
import service.*;
import ui.*;
import utils.*;
import java.util.*;

public class HireHubApp {
    public static void main(String[] args) {
        System.out.println("Initializing Hire Hub System...\n");

        StudentService studentService = new StudentService();
        CompanyService companyService = new CompanyService();
        JobService jobService = new JobService();
        ApplicationService applicationService = new ApplicationService();

        FileHandler.loadAll(studentService, companyService, jobService, applicationService);

        ConsoleMenu menu = new ConsoleMenu(
                studentService, companyService, jobService, applicationService);

        menu.start();

        FileHandler.saveAll(studentService, companyService, jobService, applicationService);
    }
}