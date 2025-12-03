package data;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Job;

/**
 * JobDataManager - MySQL implementation
 * Manages all job-related database operations
 */
public class JobDataManager {
    private DatabaseManager dbManager;

    public JobDataManager() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Add a new job to the database
     * @return true if successful, false otherwise
     */
    public boolean addJob(Job job) {
        String sql = "INSERT INTO jobs (client_id, job_type, duration_hours, deadline, description, " +
                    "submission_timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, job.getAccountId());
            pstmt.setString(2, job.getJobType());
            pstmt.setInt(3, job.getDuration());
            pstmt.setString(4, job.getDeadline());
            pstmt.setString(5, job.getDescription());
            pstmt.setString(6, job.getSubmissionTimestamp());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated job ID
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    job.setJobId(rs.getInt(1));
                    System.out.println("Job added successfully with ID: " + job.getJobId());
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding job: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all jobs from database
     */
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs ORDER BY submission_timestamp";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all jobs: " + e.getMessage());
        }
        return jobs;
    }

    /**
     * Get job by ID
     */
    public Job getJobById(int jobId) {
        String sql = "SELECT * FROM jobs WHERE job_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractJobFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting job by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all jobs for a specific client
     */
    public List<Job> getJobsByClientId(int clientId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE client_id = ? ORDER BY submission_timestamp";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting jobs by client ID: " + e.getMessage());
        }
        return jobs;
    }

    /**
     * Update job status
     */
    public boolean updateJobStatus(int jobId, String status) {
        String sql = "UPDATE jobs SET status = ? WHERE job_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, jobId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating job status: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update job completion time
     */
    public boolean updateJobCompletionTime(int jobId, int completionTime) {
        String sql = "UPDATE jobs SET completion_time = ? WHERE job_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, completionTime);
            pstmt.setInt(2, jobId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating job completion time: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update job progress
     */
    public boolean updateJobProgress(int jobId, double progress) {
        String sql = "UPDATE jobs SET progress = ? WHERE job_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, progress);
            pstmt.setInt(2, jobId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating job progress: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete job by ID
     */
    public boolean deleteJob(int jobId) {
        String sql = "DELETE FROM jobs WHERE job_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, jobId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Job deleted successfully: " + jobId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting job: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get jobs by status
     */
    public List<Job> getJobsByStatus(String status) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE status = ? ORDER BY submission_timestamp";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting jobs by status: " + e.getMessage());
        }
        return jobs;
    }

    /**
     * Extract Job object from ResultSet
     */
    private Job extractJobFromResultSet(ResultSet rs) throws SQLException {
        Job job = new Job(
            rs.getInt("job_id"),
            rs.getInt("client_id"),
            rs.getTimestamp("submission_timestamp").toString(),
            rs.getString("job_type"),
            rs.getInt("duration_hours"),
            rs.getString("deadline"),
            rs.getString("description")
        );
        job.setCompletionTime(rs.getInt("completion_time"));
        return job;
    }
}
