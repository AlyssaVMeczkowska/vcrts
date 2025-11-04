package model;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

public class Server {
    private int serverID;
    private List<Job> completedJobs;

    public Server(int serverID, List<Job> completedJobs) {
        this.serverID = serverID;
        this.completedJobs = completedJobs;
    }
    /* Placeholder until TransferJobToServer is completed in Controller
     * 
     * A method that will be called by the Controller after a job is completed 
     * to add the job to the server's storage
     */
    public void addCompletedJob(Job job) {
    	if(job != null) {
    		this.completedJobs.add(job);
    	}
    }
    
    /* Placeholder until TransferJobToServer is completed in Controller
     * 
     * Uses activeVehicle to check whether a job is completed by a departing vehicle
     */
    public void eraseJobOnVehicle(String vehicleID, Date departureDate){
    	if(vehicleID == null || departureDate == null) {
    		return;
    	}
    	Date currentDate = new Date();
    	
    	if(currentDate.before(departureDate)) {
    		return;
    	}
    	Iterator<Job> iterator = completedJobs.iterator();
    	while(iterator.hasNext()) {
    		Job job = iterator.next();
    		
    		if(job.getActiveVehicle() != null && vehicleID.equals(String.valueOf(job.getActiveVehicle().getVehicleId()))) {
    			iterator.remove();
    		}
    	}
    }
    public int getServerID() {
    	return serverID;
    }
    public List<Job> getCompletedJobs() {
    	return completedJobs;
    }
}

