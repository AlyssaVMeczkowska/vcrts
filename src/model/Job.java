package model;

import java.time.LocalDateTime;

public class Job {
    private String jobType;
    private int duration;
    private String deadline;
    private String description;
    private String submissionTimestamp;

    public Job(String jobType, int duration, String deadline, String description) {
        this.jobType = jobType;
        this.duration = duration;
        this.deadline = deadline;
        this.description = description;
        this.submissionTimestamp = LocalDateTime.now().toString();
    }

    public String getJobType() { return jobType; }
    public int getDuration() { return duration; }
    public String getDeadline() { return deadline; }
    public String getDescription() { return description; }
    public String getSubmissionTimestamp() { return submissionTimestamp; }
}