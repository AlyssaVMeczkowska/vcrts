package model;

import java.util.List;

public class Controller {
    private int vcID;
    private List<Job> jobs;
    private List<Checkpoint> checkpoint;
    private List<Vehicle> parkingLot;

    public Controller(int vcID, List<Job> jobs, List<Checkpoint> checkpoint, List<Vehicle> parkingLot) {
        this.vcID = vcID;
        this.jobs = jobs;
        this.checkpoint = checkpoint;
        this.parkingLot = parkingLot;
    }

    public void assignVehicleToJob(String vehicleID, int jobID, int redundancyLevel){

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
        return null;
    }

    public List<Job> addJobs(){
        return null;
    }

    public Job updateJobStatus(){
        return null;
    }
}

