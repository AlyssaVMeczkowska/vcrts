package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;       
import model.Job;

public class JobDataManager {
    private static final String FILE_PATH = "data/vcrts_data.txt";
    
    private int getNextJobId() {
        int maxId = 0;
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 1;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isJobBlock = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("type: job_submission")) {
                    isJobBlock = true;
                } else if (line.equals("---")) {
                    isJobBlock = false;
                } else if (isJobBlock && line.startsWith("job_id:")) {
   
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
            System.err.println("Error reading data file to get next job ID: " + ex.getMessage());
        }
        return maxId + 1;
    }

    public boolean addJob(Job job) {
        int newJobId = getNextJobId();
        job.setJobId(newJobId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("type: job_submission");
            writer.newLine();
            writer.write("job_id: " + job.getJobId());
            writer.newLine();
            writer.write("client_id: " + job.getAccountId());
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
            System.err.println("Error writing to data file: " + ex.getMessage());
            return false;
        }
    }

    public List<Job> getAllJobs() {
        List<Job> allJobs = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return allJobs;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Map<String, String> jobData = new HashMap<>();
            boolean inJobBlock = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("type: job_submission")) {
                    inJobBlock = true;
                    jobData.clear();
                } else if (line.equals("---")) {
                    if (inJobBlock) {
                        try {
                            Job job = new Job(
                                Integer.parseInt(jobData.getOrDefault("job_id", "0")),
                                Integer.parseInt(jobData.getOrDefault("client_id", "0")),
                                jobData.get("timestamp"),
                                jobData.get("job_type"),
                                Integer.parseInt(jobData.getOrDefault("duration_hours", "0")),
                                jobData.get("deadline"),
                                jobData.get("description")
                            );
                            allJobs.add(job);
                        } catch (Exception e) {
                            System.err.println("Failed to parse job block: " + e.getMessage());
                        }
                    }
                    inJobBlock = false;
                } else if (inJobBlock) {
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        jobData.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allJobs;
    }
}