package model;

import java.util.Date;

public class Checkpoint {
    private int checkpointID;
    private int jobID;
    private Date createdTimestamp;

    public Checkpoint(int checkpointID, int jobID, Date createdTimestamp) {
        this.checkpointID = checkpointID;
        this.jobID = jobID;
        this.createdTimestamp = createdTimestamp;
    }

    public void saveImage(){

    }

    public void loadImage(){

    }
}

