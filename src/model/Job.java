package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Job {
    private int jobId;
    private int accountId;
    private String jobType;
    private int duration;
    private String deadline;
    private String description;
    private String submissionTimestamp;
    private double progress = 0.0;
    private Vehicle activeVehicle;
    private List<Vehicle> redundantVehicles = new ArrayList<>();
    private int redundancyLevel = 0;

    public Job(int accountId, String jobType, int duration, String deadline, String description) {
        this.accountId = accountId;
        this.jobType = jobType;
        this.duration = duration;
        this.deadline = deadline;
        this.description = description;
        this.submissionTimestamp = LocalDateTime.now().toString();
    }

    public int getJobId() { 
        return jobId; 
    }

    public int getAccountId() { 
        return accountId; 
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

}