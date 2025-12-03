package model;

import data.JobDataManager;
import data.RequestDataManager;
import data.VehicleDataManager;
import java.util.*;

public class Controller {
    private int vcID;
    private List<Job> jobs;
    private List<Checkpoint> checkpoint;
    private List<Vehicle> parkingLot;
    private List<Client> clients;
    private List<Owner> owners;
    

    private Map<Integer, Queue<Job>> vehicleJobQueues;
    private int nextVehicleIndex = 0;

    private JobDataManager jobDataManager;
    private VehicleDataManager vehicleDataManager;
    private RequestDataManager requestDataManager;


    private static class PendingVehicleRequest {
        Owner owner; 
        Vehicle vehicle;

        PendingVehicleRequest(Owner owner, Vehicle vehicle) {
            this.owner = owner;
            this.vehicle = vehicle;
        }
    }


    private Map<Integer, PendingVehicleRequest> pendingVehicleRequests;

    public Controller(int vcID, List<Job> jobs, List<Checkpoint> checkpoint, List<Vehicle> parkingLot, List<Client> clients, List<Owner> owners) {
        this.vcID = vcID;
        this.jobs = jobs; 
        this.checkpoint = checkpoint;
        this.parkingLot = parkingLot;
        this.clients = clients;
        this.owners = owners;

        this.jobDataManager = new JobDataManager();
        this.vehicleDataManager = new VehicleDataManager();
        this.requestDataManager = new RequestDataManager();

        this.jobs = new ArrayList<>();
        this.parkingLot = new ArrayList<>();
        this.vehicleJobQueues = new HashMap<>();


        this.pendingVehicleRequests = new HashMap<>();

        refreshAndProcessData();
    }
    
    public void refreshAndProcessData() {
        this.jobs = jobDataManager.getAllJobs();
        this.parkingLot = vehicleDataManager.getAllVehicles();
        
        this.vehicleJobQueues.clear();
        initializeVehicleQueues();
        
        this.nextVehicleIndex = 0;
        assignAllJobsToVehicles();
    }

    private void initializeVehicleQueues() {
        if (parkingLot == null || parkingLot.isEmpty()) {
            System.out.println("Warning: No vehicles available in parking lot");
            return;
        }
        
        for (Vehicle vehicle : parkingLot) {
            vehicleJobQueues.put(vehicle.getVehicleId(), new LinkedList<>());
        }
    }

    private void assignAllJobsToVehicles() {
        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs to assign");
            return;
        }
        
        if (parkingLot == null || parkingLot.isEmpty()) {
            System.out.println("Warning: Cannot assign jobs - no vehicles available");
            return;
        }

        jobs.sort(Comparator.comparing(Job::getSubmissionTimestamp));

        for (Job job : jobs) {
            assignJobToNextAvailableVehicle(job);
        }

        calculateCompletionTimesPerVehicle();
    }

    private void assignJobToNextAvailableVehicle(Job job) {
        if (parkingLot == null || parkingLot.isEmpty()) {
            return;
        }
        
        Vehicle selectedVehicle = parkingLot.get(nextVehicleIndex);
        int vehicleId = selectedVehicle.getVehicleId();
        
        if (vehicleJobQueues.get(vehicleId) == null) {
             System.err.println("Error: No queue found for vehicle ID: " + vehicleId);
             return;
        }

        vehicleJobQueues.get(vehicleId).offer(job);

        nextVehicleIndex = (nextVehicleIndex + 1) % parkingLot.size();
    }

    private void calculateCompletionTimesPerVehicle() {
        for (Map.Entry<Integer, Queue<Job>> entry : vehicleJobQueues.entrySet()) {
            Queue<Job> jobQueue = entry.getValue();
            int cumulativeTime = 0;
            
            for (Job job : jobQueue) {

                cumulativeTime += job.getDuration();
                

                job.setCompletionTime(cumulativeTime);
                

                jobDataManager.updateJobCompletionTime(job.getJobId(), cumulativeTime);
            }
        }
        System.out.println("Completion times recalculated and saved to database.");
    }

    public List<Job> getJobsForVehicle(int vehicleId) {
        Queue<Job> queue = vehicleJobQueues.get(vehicleId);
        if (queue == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(queue);
    }

    public List<Integer> getAllVehicleIDs() {
        return new ArrayList<>(vehicleJobQueues.keySet());
    }
    

    public Vehicle getVehicleById(int vehicleId) {
        if (parkingLot == null) {
            return null;
        }
        
        for (Vehicle vehicle : parkingLot) {
            if (vehicle.getVehicleId() == vehicleId) {
                return vehicle;
            }
        }
        return null;
    }


    public Map<Integer, Queue<Job>> getVehicleJobQueues() {
        return vehicleJobQueues;
    }


    public List<Job> calculateAllCompletionTimes() {
        List<Job> allJobs = new ArrayList<>();
        
        for (Queue<Job> jobQueue : vehicleJobQueues.values()) {
            allJobs.addAll(jobQueue);
        }
        

        allJobs.sort(Comparator.comparing(Job::getSubmissionTimestamp));
        
        this.jobs = allJobs;
        return allJobs;
    }


    public void addNewJob(Job newJob) {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }
        
        jobs.add(newJob);
        assignJobToNextAvailableVehicle(newJob);
        

        calculateCompletionTimesPerVehicle();
    }


    private void recalculateCompletionTimesForVehicle(int vehicleId) {
        Queue<Job> jobQueue = vehicleJobQueues.get(vehicleId);
        if (jobQueue == null) {
            return;
        }
        
        int cumulativeTime = 0;
        for (Job job : jobQueue) {
            cumulativeTime += job.getDuration();
            job.setCompletionTime(cumulativeTime);
            jobDataManager.updateJobCompletionTime(job.getJobId(), cumulativeTime);
        }
    }

    public void assignJobToVehicle(String vehicleID, int jobID, int redundancyLevel){

    }

    public List<Vehicle> getAvailableVehiclesFromParkingLot(){
        return parkingLot;
    }

    public void recruitVehicle(int jobID, String vehicleID){

    }

    public List<Vehicle> viewVehicles(){
        return parkingLot;
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
        

        for (Queue<Job> jobQueue : vehicleJobQueues.values()) {
            for (Job job : jobQueue) {
                if (job.getJobId() == jobID) {
                    return job.getCompletionTime();
                }
            }
        }
        
        return -1;
    }


    public void receiveVehicleSubmissionRequest(Owner owner, Vehicle vehicle) {
        pendingVehicleRequests.put(vehicle.getVehicleId(), new PendingVehicleRequest(owner, vehicle));

        owner.receiveNotification("SERVER: Received submission request for vehicle "
                + vehicle.getLicensePlate() + ". Awaiting VC Controller approval.");

        System.out.println("CONTROLLER: New pending vehicle for approval from "
                + owner.getUsername() + ". Vehicle ID: " + vehicle.getVehicleId());
    }

    public List<Vehicle> getPendingVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (PendingVehicleRequest req : pendingVehicleRequests.values()) {
            vehicles.add(req.vehicle);
        }
        return vehicles;
    }

    public void acceptVehicleSubmission(int vehicleId) {
        PendingVehicleRequest request = pendingVehicleRequests.remove(vehicleId);
        if (request == null) {
            System.err.println("CONTROLLER: No pending request found for vehicle ID: " + vehicleId);
            return;
        }

        boolean saved = vehicleDataManager.addVehicle(request.vehicle);

        if (saved) {
            refreshAndProcessData();

            request.owner.receiveNotification("SERVER: Your vehicle submission ("
                    + request.vehicle.getLicensePlate() + ") has been ACCEPTED.");
            System.out.println("CONTROLLER: Vehicle " + vehicleId + " accepted and saved.");
        } else {
            pendingVehicleRequests.put(vehicleId, request);

            request.owner.receiveNotification("SERVER: Your vehicle submission ("
                    + request.vehicle.getLicensePlate() + ") was REJECTED (Database Save Error).");
            System.err.println("CONTROLLER: Vehicle " + vehicleId + " approval failed (save error).");
        }
    }

    public void rejectVehicleSubmission(int vehicleId) {
        PendingVehicleRequest request = pendingVehicleRequests.remove(vehicleId);
        if (request == null) {
            System.err.println("CONTROLLER: No pending request found for vehicle ID: " + vehicleId);
            return;
        }

        request.owner.receiveNotification("SERVER: Your vehicle submission ("
                + request.vehicle.getLicensePlate() + ") has been REJECTED by the VC Controller.");
        System.out.println("CONTROLLER: Vehicle " + vehicleId + " rejected.");
    }
}