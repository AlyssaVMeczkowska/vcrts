package model;

import java.util.List;

public class Client extends User{
    private List<Job> jobs;

    public Client(int id, String firstName, String lastName, String email, String username, String phoneNumber, String hashedPassword, String accountType, String creationTimestamp, boolean hasAgreedToTerms, List<Job> jobs) {
        super(id, firstName, lastName, email, username, phoneNumber, hashedPassword, accountType, creationTimestamp, hasAgreedToTerms);
        this.jobs = jobs;
    }

    public void addJobs(Job jobID){

    }

    public void deleteJobs(Job jobID){

    }

    public void submitJobs(){
        
    }
}

