package model;

import data.JobDataManager;
import data.VehicleDataManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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
    
    private static final String FILE_PATH = "data/vcrts_data.txt";

    public Controller(int vcID, List<Job> jobs, List<Checkpoint> checkpoint, List<Vehicle> parkingLot, List<Client> clients, List<Owner> owners) {
        this.vcID = vcID;
        this.jobs = jobs; 
        this.checkpoint = checkpoint;
        this.parkingLot = parkingLot;
        this.clients = clients;
        this.owners = owners;

        this.jobDataManager = new JobDataManager();
        this.vehicleDataManager = new VehicleDataManager();
        

        this.jobs = new ArrayList<>();
        this.parkingLot = new ArrayList<>();
        this.vehicleJobQueues = new HashMap<>();
        
        refreshAndProcessData();
    }
    
    /**
     * Loads all data from files
     */
    public void refreshAndProcessData() {
        this.jobs = jobDataManager.getAllJobs();
        this.parkingLot = loadAllVehicles();
        
        this.vehicleJobQueues.clear();
        initializeVehicleQueues();
        
        this.nextVehicleIndex = 0;
        assignAllJobsToVehicles();
    }
    
    private List<Vehicle> loadAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            System.out.println("Data file not found: " + FILE_PATH);
            return vehicles;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isVehicleBlock = false;
            
            Integer vehicleId = null;
            Integer ownerId = null;
            String make = null;
            String model = null;
            Integer year = null;
            String vin = null;
            String licensePlate = null;
            String computingPower = null;
            LocalDate arrivalDate = null;
            LocalDate departureDate = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.startsWith("type: vehicle_availability")) {
                    isVehicleBlock = true;
                    vehicleId = null;
                    ownerId = null;
                    make = null;
                    model = null;
                    year = null;
                    vin = null;
                    licensePlate = null;
                    computingPower = null;
                    arrivalDate = null;
                    departureDate = null;
                    
                } else if (line.equals("---")) {
                    if (isVehicleBlock && vehicleId != null) {
                        try {
                            Vehicle vehicle = new Vehicle(
                                vehicleId,
                                ownerId != null ? ownerId : 0,
                                make,
                                model,
                                year != null ? year : 0,
                                vin,
                                licensePlate,
                                computingPower,
                                arrivalDate,
                                departureDate,
                                VehicleStatus.AVAILABLE // Default status
                            );
                            vehicles.add(vehicle);
                        } catch (Exception e) {
                            System.err.println("Error creating vehicle: " + e.getMessage());
                        }
                    }
                    isVehicleBlock = false;
                    
                } else if (isVehicleBlock && line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        
                        switch (key) {
                            case "user_id":
                                try {
                                    vehicleId = Integer.parseInt(value);
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid vehicle ID: " + value);
                                }
                                break;
                            case "owner_id":
                                try {
                                    ownerId = Integer.parseInt(value);
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid owner ID: " + value);
                                }
                                break;
                            case "vehicle_make":
                                make = value;
                                break;
                            case "vehicle_model":
                                model = value;
                                break;
                            case "vehicle_year":
                                try {
                                    year = Integer.parseInt(value);
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid year: " + value);
                                }
                                break;
                            case "vin":
                                vin = value;
                                break;
                            case "license_plate":
                                licensePlate = value;
                                break;
                            case "computing_power":
                                computingPower = value;
                                break;
                            case "start_date":
                                try {
                                    arrivalDate = LocalDate.parse(value);
                                } catch (Exception e) {
                                    System.err.println("Invalid arrival date: " + value);
                                }
                                break;
                            case "end_date":
                                try {
                                    departureDate = LocalDate.parse(value);
                                } catch (Exception e) {
                                    System.err.println("Invalid departure date: " + value);
                                }
                                break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading vehicle data: " + ex.getMessage());
        }
        
        System.out.println("Loaded " + vehicles.size() + " vehicles from file");
        return vehicles;
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
     */
    private void calculateCompletionTimesPerVehicle() {
        for (Map.Entry<Integer, Queue<Job>> entry : vehicleJobQueues.entrySet()) {
            Queue<Job> jobQueue = entry.getValue();
            int cumulativeTime = 0;
            for (Job job : jobQueue) {
                cumulativeTime += job.getDuration();
                job.setCompletionTime(cumulativeTime);
            }
        }
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
}