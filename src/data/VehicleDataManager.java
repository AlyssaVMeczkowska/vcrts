package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Vehicle;
import model.VehicleStatus;

/**
 * VehicleDataManager - MySQL implementation
 * Manages all vehicle-related database operations
 */
public class VehicleDataManager {
    private DatabaseManager dbManager;

    public VehicleDataManager() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Get the Vehicle ID associated with a specific Request ID
     */
    public int getVehicleIdByRequestId(int requestId) {
        String sql = "SELECT vehicle_id FROM vehicles WHERE request_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("vehicle_id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Vehicle ID for Request " + requestId + ": " + e.getMessage());
        }
        // Return -1 if no vehicle is found
        return -1;
    }

    /**
     * Check if VIN is already taken
     */
    public boolean isVinTaken(String vin) {
        String sql = "SELECT COUNT(*) FROM vehicles WHERE vin = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vin);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking VIN: " + e.getMessage());
        }
        return false;
    }

    /**
     * Check if license plate is already taken
     */
    public boolean isLicensePlateTaken(String licensePlate) {
        String sql = "SELECT COUNT(*) FROM vehicles WHERE license_plate = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, licensePlate);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking license plate: " + e.getMessage());
        }
        return false;
    }

    /**
     * Add a new vehicle to the database
     * UPDATED: Now inserts request_id correctly
     * @return true if successful, false otherwise
     */
    public boolean addVehicle(Vehicle vehicle) {
        // Updated SQL to include request_id
        String sql = "INSERT INTO vehicles (owner_id, request_id, vin, license_plate, vehicle_make, vehicle_model, " +
                    "vehicle_year, computing_power, arrival_date, departure_date, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, vehicle.getOwnerId());
            
            // Check if request_id exists on the vehicle object
            if (vehicle.getRequestId() > 0) {
                pstmt.setInt(2, vehicle.getRequestId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(3, vehicle.getVin());
            pstmt.setString(4, vehicle.getLicensePlate());
            pstmt.setString(5, vehicle.getMake());
            pstmt.setString(6, vehicle.getModel());
            pstmt.setInt(7, vehicle.getYear());
            pstmt.setString(8, vehicle.getComputingPower());
            pstmt.setDate(9, Date.valueOf(vehicle.getArrivalDate()));
            pstmt.setDate(10, Date.valueOf(vehicle.getDepartureDate()));
            pstmt.setString(11, vehicle.getStatus().toString());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated vehicle ID
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int vehicleId = rs.getInt(1);
                    System.out.println("Vehicle added successfully with ID: " + vehicleId);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all vehicles from database
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                vehicles.add(extractVehicleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Get vehicle by ID
     */
    public Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractVehicleFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting vehicle by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all vehicles for a specific owner
     */
    public List<Vehicle> getVehiclesByOwnerId(int ownerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE owner_id = ? ORDER BY vehicle_id";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                vehicles.add(extractVehicleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting vehicles by owner ID: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Get vehicles by status
     */
    public List<Vehicle> getVehiclesByStatus(VehicleStatus status) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE status = ? ORDER BY vehicle_id";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.toString());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                vehicles.add(extractVehicleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting vehicles by status: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Update vehicle status
     */
    public boolean updateVehicleStatus(int vehicleId, VehicleStatus status) {
        String sql = "UPDATE vehicles SET status = ? WHERE vehicle_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.toString());
            pstmt.setInt(2, vehicleId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle status updated for ID: " + vehicleId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating vehicle status: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete vehicle by ID
     */
    public boolean deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle deleted successfully: " + vehicleId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get available vehicles (AVAILABLE status)
     */
    public List<Vehicle> getAvailableVehicles() {
        return getVehiclesByStatus(VehicleStatus.AVAILABLE);
    }

    /**
     * Extract Vehicle object from ResultSet
     */
    private Vehicle extractVehicleFromResultSet(ResultSet rs) throws SQLException {
        VehicleStatus status = VehicleStatus.AVAILABLE;
        try {
            status = VehicleStatus.valueOf(rs.getString("status"));
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid vehicle status, defaulting to AVAILABLE");
        }

        Vehicle vehicle = new Vehicle(
            rs.getInt("vehicle_id"),
            rs.getInt("owner_id"),
            rs.getString("vehicle_make"),
            rs.getString("vehicle_model"),
            rs.getInt("vehicle_year"),
            rs.getString("vin"),
            rs.getString("license_plate"),
            rs.getString("computing_power"),
            rs.getDate("arrival_date").toLocalDate(),
            rs.getDate("departure_date").toLocalDate(),
            status
        );
        
        // Set submission timestamp if available
        Timestamp submissionTimestamp = rs.getTimestamp("submission_timestamp");
        if (submissionTimestamp != null) {
            vehicle.setSubmissionTimestamp(submissionTimestamp.toString());
        }
        
        // Set request ID if available
        int reqId = rs.getInt("request_id");
        if (!rs.wasNull()) {
            vehicle.setRequestId(reqId);
        }
        
        return vehicle;
    }
    
    public Vehicle getLatestVehicleByOwner(int ownerId) {
        String sql = "SELECT * FROM vehicles WHERE owner_id = ? ORDER BY vehicle_id DESC LIMIT 1";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractVehicleFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching latest vehicle by owner: " + e.getMessage());
        }

        return null;
    }
}