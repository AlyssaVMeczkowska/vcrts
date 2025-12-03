package data;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.Request;
import model.RequestStatus;

/**
 * RequestDataManager - MySQL implementation
 * Manages all request-related database operations
 */
public class RequestDataManager {
    private DatabaseManager dbManager;

    public RequestDataManager() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Add a new request to the database
     * @return the generated request ID, or -1 if failed
     */
    public int addRequest(String requestType, int userId, String userName, String data) {
        String sql = "INSERT INTO requests (request_type, user_id, user_name, request_data, " +
                    "status, notification_viewed) VALUES (?, ?, ?, ?, 'PENDING', TRUE)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, requestType);
            pstmt.setInt(2, userId);
            pstmt.setString(3, userName);
            pstmt.setString(4, data);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int requestId = rs.getInt(1);
                    System.out.println("Request added successfully with ID: " + requestId);
                    return requestId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding request: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get all pending requests
     */
    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE status = 'PENDING' ORDER BY submission_timestamp";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(extractRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending requests: " + e.getMessage());
        }
        return requests;
    }

    /**
     * Get all requests
     */
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests ORDER BY submission_timestamp DESC";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(extractRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all requests: " + e.getMessage());
        }
        return requests;
    }

    /**
     * Get request by ID
     */
    public Request getRequestById(int requestId) {
        String sql = "SELECT * FROM requests WHERE request_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractRequestFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting request by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get unnotified requests for a specific user and request type
     */
    public Map<String, List<Integer>> getUnnotifiedRequests(int userId, String targetRequestType) {
        Map<String, List<Integer>> result = new HashMap<>();
        result.put("ACCEPTED", new ArrayList<>());
        result.put("REJECTED", new ArrayList<>());
        
        String sql = "SELECT request_id, status FROM requests " +
                    "WHERE user_id = ? AND request_type = ? AND notification_viewed = FALSE " +
                    "AND status != 'PENDING'";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, targetRequestType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                String status = rs.getString("status");
                
                if (status.equals("ACCEPTED")) {
                    result.get("ACCEPTED").add(requestId);
                } else if (status.equals("REJECTED")) {
                    result.get("REJECTED").add(requestId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting unnotified requests: " + e.getMessage());
        }
        return result;
    }

    /**
     * Mark requests as viewed
     */
    public void markAsViewed(List<Integer> requestIds) {
        if (requestIds == null || requestIds.isEmpty()) return;
        
        // Build SQL with placeholders for IN clause
        StringBuilder sql = new StringBuilder("UPDATE requests SET notification_viewed = TRUE WHERE request_id IN (");
        for (int i = 0; i < requestIds.size(); i++) {
            sql.append("?");
            if (i < requestIds.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < requestIds.size(); i++) {
                pstmt.setInt(i + 1, requestIds.get(i));
            }
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Marked " + rowsAffected + " requests as viewed");
        } catch (SQLException e) {
            System.err.println("Error marking requests as viewed: " + e.getMessage());
        }
    }

    /**
     * Update request status
     */
    public boolean updateRequestStatus(int requestId, RequestStatus newStatus, String rejectionReason) {
        String sql = "UPDATE requests SET status = ?, rejection_reason = ?, notification_viewed = FALSE " +
                    "WHERE request_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus.toString());
            pstmt.setString(2, rejectionReason != null ? rejectionReason : "");
            pstmt.setInt(3, requestId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Request status updated for ID: " + requestId + " to " + newStatus);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating request status: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get requests by user ID
     */
    public List<Request> getRequestsByUserId(int userId) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE user_id = ? ORDER BY submission_timestamp DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                requests.add(extractRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting requests by user ID: " + e.getMessage());
        }
        return requests;
    }

    /**
     * Get requests by type
     */
    public List<Request> getRequestsByType(String requestType) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE request_type = ? ORDER BY submission_timestamp DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, requestType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                requests.add(extractRequestFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting requests by type: " + e.getMessage());
        }
        return requests;
    }

    /**
     * Delete request by ID
     */
    public boolean deleteRequest(int requestId) {
        String sql = "DELETE FROM requests WHERE request_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Request deleted successfully: " + requestId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting request: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extract Request object from ResultSet
     */
    private Request extractRequestFromResultSet(ResultSet rs) throws SQLException {
        RequestStatus status = RequestStatus.PENDING;
        try {
            status = RequestStatus.valueOf(rs.getString("status"));
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request status, defaulting to PENDING");
        }

        Request request = new Request(
            rs.getInt("request_id"),
            rs.getString("request_type"),
            rs.getInt("user_id"),
            rs.getString("user_name"),
            rs.getTimestamp("submission_timestamp").toString(),
            status,
            rs.getString("request_data")
        );
        
        String rejectionReason = rs.getString("rejection_reason");
        if (rejectionReason != null && !rejectionReason.isEmpty()) {
            request.setRejectionReason(rejectionReason);
        }
        
        return request;
    }
}
