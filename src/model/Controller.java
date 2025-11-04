package model;

import data.JobDataManager;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Controller {
    private int vcID;
    private List<Job> jobs;
    private List<Checkpoint> checkpoint;
    private List<Vehicle> parkingLot;
    private List<Client> clients;
    private List<Owner> owners;

    private JobDataManager jobDataManager;

    public Controller(int vcID, List<Job> jobs, List<Checkpoint> checkpoint, List<Vehicle> parkingLot, List<Client> clients, List<Owner> owners) {
        this.vcID = vcID;
        this.jobs = jobs; 
        this.checkpoint = checkpoint;
        this.parkingLot = parkingLot;
        this.clients = clients;
        this.owners = owners;


        this.jobDataManager = new JobDataManager();
        this.jobs = jobDataManager.getAllJobs(); 
    }


    public List<Job> calculateAllCompletionTimes() {

        List<Job> allJobs = jobDataManager.getAllJobs();

        allJobs.sort(Comparator.comparing(Job::getSubmissionTimestamp));

        int cumulativeTime = 0;
        for (Job job : allJobs) {
            cumulativeTime += job.getDuration();
            job.setCompletionTime(cumulativeTime);
        }
        
        this.jobs = allJobs;
        return allJobs;
    }

    public void assignJobToVehicle(String vehicleID, int jobID, int redundancyLevel){

    }

    public List<Vehicle> getAvailableVehiclesFromParkingLot(){
        return null;
    }

    public void recruitVehicle(int jobID, String vehicleID){

    }

    public List<Vehicle> viewVehicles(){
        return null;
    }

    public Job checkJobStatus(int jobID){
        return null;
    }

    public float jobPercentCompletion(){
        return 0.0f;
    }

    public Checkpoint createCheckpoint(int jobID){
        return null;
    }

    public void restoreVehicleToCheckpoint(int checkpointID, String loadImage, String vehicleID){

    }

    public void restartComputationOnVehicleCheckpoint(String vehicleID, String checkpointID, String jobID){

    }

    public int checkpointDelete(int checkpointID){
        return 0;
    }

    public void deleteVehicleOnDeparture(String vehicleID){

    }

    public void transferJobToServer(int jobID){

    }

    public void updateVehicleStatus(String vehicleID, String status){

    }
    
    public List<Job> viewJobs(){
        return jobs;
    }

    public List<Job> addJobs(){
        return null;
    }

    public Job updateJobStatus(){
        return null;
    }

    public void registerClient(Client client){
        clients.add(client);
    }

    public void registerOwner(Owner owner){
        owners.add(owner);
    }

    public List<Client> viewClients(){
        return clients;
    }

    public List<Owner> viewOwners(){
        return owners;
    }

    public int calculateCompletionTime(int jobID) { 
        if (jobs == null || jobs.isEmpty()) {
            return -1;
        }
        
        Queue<Job> jobQueue = new LinkedList<>();
        jobs.stream()
            .sorted(Comparator.comparing(Job::getSubmissionTimestamp))
            .forEach(jobQueue::offer);
        int cumulativeTime = 0;
        
        while (!jobQueue.isEmpty()) {
            Job currentJob = jobQueue.poll();
            cumulativeTime += currentJob.getDuration();
            
            if (currentJob.getJobId() == jobID) {
                return cumulativeTime;
            }
        }
        
        return -1;
    }
}