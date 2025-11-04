package model;

import java.util.List;

public class Client extends User{
    private List<Job> jobs;

    public Client(int id, String firstName, String lastName, String email, String username, String phoneNumber, String hashedPassword, String accountType, String creationTimestamp, boolean hasAgreedToTerms, List<Job> jobs) {
        super(id, firstName, lastName, email, username, phoneNumber, hashedPassword, accountType, creationTimestamp, hasAgreedToTerms);
        this.jobs = jobs;
    }

    //Implementation to Add Jobs
    public void addJobs(Job jobID)
    {
        if (jobID != null && jobs != null)
        {
            jobs.add(jobID);
        }
    }

    //Implementation to Delete Jobs
    public void deleteJobs(Job jobID)
    {
        if (jobID != null && jobs != null)
        {
            jobs.remove(jobID);
        }
    }

    //Implementation to submit jobs
    public void submitJobs()
    {
        if (jobs != null && !jobs.isEmpty())
        {
            for (Job job : jobs)
            {
               
                System.out.println("Job " + job.getJobId() + " submitted successfully.");
                
            }
        }
        else
        {
            System.out.println("No jobs to submit");
        }
    }

    //New Method to return list of Jobs
    public List<Job> getJobs()
    {
        return jobs;
    }

}



