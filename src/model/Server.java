package model;

import java.util.List;

import java.util.Date;

public class Server {
    private int serverID;
    private List<Job> completedJobs;

    public Server(int serverID, List<Job> completedJobs) {
        this.serverID = serverID;
        this.completedJobs = completedJobs;
    }

    public void eraseJobOnVehicle(String jobID, String vehicleID, Date departureDate){

    }
}

