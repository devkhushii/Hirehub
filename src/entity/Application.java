package entity;

public class Application {
    public enum Status {
        PENDING,
        REVIEWED
    }

    private static int idCounter = 1;
    private final int id;
    private Student student;
    private Job job;
    private Status status;
    private int matchScore;

    public Application(Student student, Job job) {
        this.id = idCounter++;
        this.student = student;
        this.job = job;
        this.status = Status.PENDING;
        this.matchScore = 0;
    }

    public int getId() { return id; }
    public Student getStudent() { return student; }
    public Job getJob() { return job; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public int getMatchScore() { return matchScore; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
    public void markAsReviewed() { this.status = Status.REVIEWED; }

    @Override
    public String toString() {
        return String.format("Application[ID: %d, Student: %s, Job: %s, Status: %s, Match: %d%%]",
                id, student.getName(), job.getTitle(), status, matchScore);
    }
}