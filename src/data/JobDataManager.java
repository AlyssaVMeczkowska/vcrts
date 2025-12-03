package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Job;

public class JobDataManager {
    private DatabaseManager dbManager;

    public JobDataManager() {
        this.dbManager = DatabaseManager.getInstance();
    }


    public boolean addJob(Job job) {
        String sql = "INSERT INTO jobs (client_id, request_id, job_type, duration_hours, deadline, description, " +
                    "submission_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, job.getAccountId());
            
            if (job.getRequestId() > 0) {
                pstmt.setInt(2, job.getRequestId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(3, job.getJobType());
            pstmt.setInt(4, job.getDuration());
            pstmt.setString(5, job.getDeadline());
            pstmt.setString(6, job.getDescription());
            pstmt.setString(7, job.getSubmissionTimestamp());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {

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


    public int getJobIdByRequestId(int requestId) {
        String sql = "SELECT job_id FROM jobs WHERE request_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("job_id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Job ID for Request " + requestId + ": " + e.getMessage());
        }

        return -1;
    }


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
        

        int reqId = rs.getInt("request_id");
        if (!rs.wasNull()) {
            job.setRequestId(reqId);
        }
        
        return job;
    }
}