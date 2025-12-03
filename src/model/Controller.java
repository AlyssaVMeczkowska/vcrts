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
    
    // Per-vehicle job queues
    private Map<Integer, Queue<Job>> vehicleJobQueues;
    private int nextVehicleIndex = 0;

    private JobDataManager jobDataManager;
    private VehicleDataManager vehicleDataManager;
    private RequestDataManager requestDataManager;

    //New Method For Server State
    private static class PendingVehicleRequest {
        Owner owner; 
        Vehicle vehicle;

        PendingVehicleRequest(Owner owner, Vehicle vehicle) {
            this.owner = owner;
            this.vehicle = vehicle;
        }
    }

    //New Attribute To Store Pending Vehicle Submissions
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

        //New Attribute To Initialize Map of Pending Requests
        this.pendingVehicleRequests = new HashMap<>();

        refreshAndProcessData();
    }
    
    /**
     * Loads all data from MySQL database
     */
    public void refreshAndProcessData() {
        this.jobs = jobDataManager.getAllJobs();
        this.parkingLot = vehicleDataManager.getAllVehicles();
        
        this.vehicleJobQueues.clear();
        initializeVehicleQueues();
        
        this.nextVehicleIndex = 0;
        assignAllJobsToVehicles();
    }

    /**
     * Initialize job queues for all vehicles in the parking lot
     */
    private void initializeVehicleQueues() {
        if (parkingLot == null || parkingLot.isEmpty()) {
            System.out.println("Warning: No vehicles available in parking lot");
            return;
        }
        
        for (Vehicle vehicle : parkingLot) {
            vehicleJobQueues.put(vehicle.getVehicleId(), new LinkedList<>());
        }
    }

    /**
     * Assign all jobs to vehicles using round-robin algorithm
     */
    private void assignAllJobsToVehicles() {
        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs to assign");
            return;
        }
        
        if (parkingLot == null || parkingLot.isEmpty()) {
            System.out.println("Warning: Cannot assign jobs - no vehicles available");
            return;
        }
        
        // Sort jobs by submission timestamp
        jobs.sort(Comparator.comparing(Job::getSubmissionTimestamp));
        
        // Assign each job to a vehicle
        for (Job job : jobs) {
            assignJobToNextAvailableVehicle(job);
        }
        
        // Calculate completion times for each vehicle
        calculateCompletionTimesPerVehicle();
    }

    /**
     * Simple round-robin job assignment algorithm
     * Assigns a job to the next available vehicle in rotation
     */
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

        // Add job to the vehicle's queue
        vehicleJobQueues.get(vehicleId).offer(job);
        
        // Move to next vehicle (round-robin)
        nextVehicleIndex = (nextVehicleIndex + 1) % parkingLot.size();
    }

    /**
     * Calculate completion times for jobs in each vehicle's queue using FIFO
     * UPDATED: Now persists the calculated time to the database
     */
    private void calculateCompletionTimesPerVehicle() {
        for (Map.Entry<Integer, Queue<Job>> entry : vehicleJobQueues.entrySet()) {
            Queue<Job> jobQueue = entry.getValue();
            int cumulativeTime = 0;
            
            for (Job job : jobQueue) {
                // FIFO Algorithm: Add current job duration to cumulative time
                cumulativeTime += job.getDuration();
                
                // Update Memory Object
                job.setCompletionTime(cumulativeTime);
                
                // NEW: Update Database Immediately
                jobDataManager.updateJobCompletionTime(job.getJobId(), cumulativeTime);
            }
        }
        System.out.println("Completion times recalculated and saved to database.");
    }

    /**
     * Get jobs assigned to a specific vehicle
     */
    public List<Job> getJobsForVehicle(int vehicleId) {
        Queue<Job> queue = vehicleJobQueues.get(vehicleId);
        if (queue == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(queue);
    }

    /**
     * Get all vehicle IDs that have jobs assigned
     */
    public List<Integer> getAllVehicleIDs() {
        return new ArrayList<>(vehicleJobQueues.keySet());
    }
    
    /**
     * Get vehicle by ID
     */
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

    /**
     * Get the vehicle job queues map
     */
    public Map<Integer, Queue<Job>> getVehicleJobQueues() {
        return vehicleJobQueues;
    }

    /**
     * Legacy method - returns all jobs across all vehicles
     */
    public List<Job> calculateAllCompletionTimes() {
        List<Job> allJobs = new ArrayList<>();
        
        for (Queue<Job> jobQueue : vehicleJobQueues.values()) {
            allJobs.addAll(jobQueue);
        }
        
        // Sort by submission timestamp for display
        allJobs.sort(Comparator.comparing(Job::getSubmissionTimestamp));
        
        this.jobs = allJobs;
        return allJobs;
    }

    /**
     * Add a new job and automatically assign it to a vehicle
     */
    public void addNewJob(Job newJob) {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }
        
        jobs.add(newJob);
        assignJobToNextAvailableVehicle(newJob);
        
        // Recalculate completion times for all vehicles
        calculateCompletionTimesPerVehicle();
    }

    /**
     * Legacy method –
     * Recalculate completion times for a specific vehicle's queue
     */
    private void recalculateCompletionTimesForVehicle(int vehicleId) {
        Queue<Job> jobQueue = vehicleJobQueues.get(vehicleId);
        if (jobQueue == null) {
            return;
        }
        
        int cumulativeTime = 0;
        for (Job job : jobQueue) {
            cumulativeTime += job.getDuration();
            job.setCompletionTime(cumulativeTime);
            // Also update DB here just in case this legacy path is used
            jobDataManager.updateJobCompletionTime(job.getJobId(), cumulativeTime);
        }
    }

    public void assignJobToVehicle(String vehicleID, int jobID, int redundancyLevel){
        // Implementation for manual job assignment
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

    /**
     * Legacy method - calculates completion time for a specific job
     */
    public int calculateCompletionTime(int jobID) { 
        if (jobs == null || jobs.isEmpty()) {
            return -1;
        }
        
        // Search through all vehicle queues
        for (Queue<Job> jobQueue : vehicleJobQueues.values()) {
            for (Job job : jobQueue) {
                if (job.getJobId() == jobID) {
                    return job.getCompletionTime();
                }
            }
        }
        
        return -1;
    }

    //New Controller("Server-Side") Recieving Methods
    public void receiveVehicleSubmissionRequest(Owner owner, Vehicle vehicle) {
        pendingVehicleRequests.put(vehicle.getVehicleId(), new PendingVehicleRequest(owner, vehicle));

        owner.receiveNotification("SERVER: Received submission request for vehicle "
                + vehicle.getLicensePlate() + ". Awaiting VC Controller approval.");

        System.out.println("CONTROLLER: New pending vehicle for approval from "
                + owner.getUsername() + ". Vehicle ID: " + vehicle.getVehicleId());
    }

    //Method For UI To Get Pending Requests
    public List<Vehicle> getPendingVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (PendingVehicleRequest req : pendingVehicleRequests.values()) {
            vehicles.add(req.vehicle);
        }
        return vehicles;
    }

    //Method to Accept Vehicle Submission - NOW USES DATABASE
    public void acceptVehicleSubmission(int vehicleId) {
        PendingVehicleRequest request = pendingVehicleRequests.remove(vehicleId);
        if (request == null) {
            System.err.println("CONTROLLER: No pending request found for vehicle ID: " + vehicleId);
            return;
        }

        // Add vehicle to database instead of file
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

    //Method to Reject Vehicle Submission
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