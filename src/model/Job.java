package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Job {
    private int jobId;
    private int accountId;
    private int requestId; // NEW: Link to the request
    private String jobType;
    private int duration;
    private String deadline;
    private String description;
    private String submissionTimestamp;
    private double progress = 0.0;
    private Vehicle activeVehicle;
    private List<Vehicle> redundantVehicles = new ArrayList<>();
    private int redundancyLevel = 0;
    
    private int completionTime;

    public Job(int accountId, String jobType, int duration, String deadline, String description) {
        this.accountId = accountId;
        this.jobType = jobType;
        this.duration = duration;
        this.deadline = deadline;
        this.description = description;
        this.submissionTimestamp = LocalDateTime.now().toString();
    }
    
    public Job(int jobId, int accountId, String submissionTimestamp, String jobType, int duration, String deadline, String description) {
        this.jobId = jobId;
        this.accountId = accountId;
        this.submissionTimestamp = submissionTimestamp;
        this.jobType = jobType;
        this.duration = duration;
        this.deadline = deadline;
        this.description = description;
    }

    public int getJobId() { 
        return jobId;
    }

    public int getAccountId() { 
        return accountId;
    }

    // NEW Getter
    public int getRequestId() {
        return requestId;
    }

    // NEW Setter
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getJobType() { 
        return jobType;
    }

    public int getDuration() { 
        return duration;
    }

    public String getDeadline() { 
        return deadline;
    }

    public String getDescription() { 
        return description;
    }

    public String getSubmissionTimestamp() { 
        return submissionTimestamp;
    }

    public void setJobId(int jobId) { 
        this.jobId = jobId;
    }

    public Vehicle getActiveVehicle(){
        return activeVehicle;
    }

    public List<Vehicle> getRedundantVehicle(){
        return redundantVehicles;
    }

    public int getRedundancyLevel(){
        return redundancyLevel;
    }

    public double getProgress(int jobID){
        return progress;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
}