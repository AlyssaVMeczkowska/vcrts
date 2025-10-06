package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import model.Job;

public class JobDataManager {
    private static final String FILE_PATH = "data/clients_data.txt";

    public boolean addJob(Job job) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("timestamp: " + job.getSubmissionTimestamp());
            writer.newLine();
            writer.write("job_type: " + job.getJobType());
            writer.newLine();
            writer.write("duration_hours: " + job.getDuration());
            writer.newLine();
            writer.write("deadline: " + job.getDeadline());
            writer.newLine();
            writer.write("description: " + job.getDescription().replace("\n", " "));
            writer.newLine();
            writer.write("---");
            writer.newLine();
            return true;
        } catch (IOException ex) {
            System.err.println("Error writing to client data file: " + ex.getMessage());
            return false;
        }
    }
}