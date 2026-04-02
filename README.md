# Hire Hub - Job Portal & Internship Management System

A complete Java Swing-based GUI application for managing job postings, internships, and student applications with an integrated Resume Analyzer.

## Features

### User Roles
- **Student**: Create profile, upload resume, browse jobs, apply to jobs, view applications
- **Company**: Create company profile, post jobs/internships, view applicants, filter by match score
- **Admin**: View all students, companies, jobs, applications, add sample data

### Resume Analyzer
- Keyword-based matching between student resumes and job requirements
- Calculates match percentage for each applicant
- Identifies missing skills and provides improvement suggestions
- Shows match percentage before allowing job application

### Core Functionality
- Job management (create, list, search)
- Application tracking with status (Pending/Reviewed)
- Duplicate application prevention
- File-based data persistence

## Technology Stack
- **Language**: Java 21
- **GUI Framework**: Java Swing
- **Data Storage**: Text files (CSV-like format)
- **Architecture**: OOP with Service Layer pattern

## Project Structure

```
src/
├── entity/              # Data models
│   ├── Student.java
│   ├── Company.java
│   ├── Job.java
│   └── Application.java
├── service/            # Business logic
│   ├── StudentService.java
│   ├── CompanyService.java
│   ├── JobService.java
│   ├── ApplicationService.java
│   └── ResumeAnalyzer.java
├── gui/                # Java Swing UI
│   ├── HireHubGUI.java
│   ├── LoginUI.java
│   ├── StudentDashboard.java
│   ├── CompanyDashboard.java
│   ├── AdminDashboard.java
│   └── ResumeAnalyzerDialog.java
├── utils/              # Utilities
│   └── FileHandler.java
└── HireHubApp.java    # Console version entry
```

## Building & Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher

### Compile
```bash
javac -d out -sourcepath src src/*.java src/**/*.java
```

### Run GUI Application
```bash
java -cp out gui.HireHubGUI
```

### Run Console Application
```bash
java -cp out HireHubApp
```

## Sample Data

The application includes sample data with:

### Students (5)
| Email | Password |
|-------|----------|
| john@email.com | password123 |
| jane@email.com | password123 |
| bob@email.com | password123 |
| alice@email.com | password123 |
| charlie@email.com | password123 |

### Companies (4)
- Tech Corp (tech@company.com / password123)
- Data Solutions (data@company.com / password123)
- WebTech Inc (web@company.com / password123)
- Cloud Systems (cloud@company.com / password123)

### Admin
- Password: `admin123`

## How to Use

### For Students
1. Register or login with credentials
2. Upload your resume (include relevant skills)
3. Browse available jobs
4. Apply to jobs - see match percentage and suggestions before applying
5. View your application status

### For Companies
1. Register or login with company credentials
2. Post new jobs/internships with required skills
3. View all posted jobs
4. View applicants for each job
5. See resume analysis with match percentage

### For Admin
1. Login with any email and admin123 password
2. View all students, companies, jobs, applications
3. Add sample data for testing

## Key Features in Detail

### Resume Analyzer
- Compares student resume keywords against job requirements
- Calculates match percentage
- Identifies matched and missing skills
- Suggests skills to add for improvement
- Color-coded feedback (green/yellow/red based on match %)

### Application Workflow
1. Student selects a job to apply
2. Resume Analyzer runs automatically
3. Match percentage and suggestions displayed
4. Student confirms or cancels application
5. Application submitted if confirmed

### Data Persistence
- Data saved to `data/` folder on exit
- Data loaded automatically on startup
- Files: students.txt, companies.txt, jobs.txt, applications.txt

## Screenshots

### Login Screen
- Role selection (Student/Company/Admin)
- Email and password fields
- Registration option

### Student Dashboard
- Profile tab with edit option
- Resume upload with file chooser
- Job browser with apply functionality
- Application tracker

### Company Dashboard
- Company profile
- Job posting form
- Applicant list with match scores
- Resume analysis viewer

### Admin Dashboard
- Tabbed view of all data
- Add sample data button
- System overview

## License
This project is for educational purposes.

## Author
Hire Hub Team