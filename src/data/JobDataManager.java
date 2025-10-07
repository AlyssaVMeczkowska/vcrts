package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import model.Job;

public class JobDataManager {
    private static final String FILE_PATH = "data/clients_data.txt";

    private int getNextJobId() {
        int maxId = 0;
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 1; // Start with ID 1 if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("job_id:")) {
                    try {
                        int currentId = Integer.parseInt(line.split(":")[1].trim());
                        if (currentId > maxId) {
                            maxId = currentId;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Could not parse job ID from line: " + line);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading client data file to get next job ID: " + ex.getMessage());
        }
        return maxId + 1;
    }

    public boolean addJob(Job job) {
        int newJobId = getNextJobId();
        job.setJobId(newJobId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("job_id: " + job.getJobId());
            writer.newLine();
            writer.write("user_id: " + job.getUserId());
            writer.newLine();
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
