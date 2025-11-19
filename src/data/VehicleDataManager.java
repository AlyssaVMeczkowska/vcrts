package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Vehicle;
import model.VehicleStatus;

public class VehicleDataManager {
    private static final String FILE_PATH = "data/vcrts_data.txt";
    
    private boolean isValueTaken(String key, String value) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isVehicleBlock = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("type: vehicle_availability")) {
                    isVehicleBlock = true;
                } else if (line.equals("---")) {
                    isVehicleBlock = false; 
                } else if (isVehicleBlock && line.startsWith(key)) {
                    String fileValue = line.split(":", 2)[1].trim();
                    if (fileValue.equalsIgnoreCase(value)) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading data file: " + ex.getMessage());
        }
        return false;
    }
    
    public boolean isVinTaken(String vin) {
        return isValueTaken("vin:", vin);
    }
    
    public boolean isLicensePlateTaken(String licensePlate) {
        return isValueTaken("license_plate:", licensePlate);
    }
    
    public boolean addVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("type: vehicle_availability"); 
            writer.newLine();
            writer.write("user_id: " + vehicle.getVehicleId()); 
            writer.newLine();
            writer.write("vin: " + vehicle.getVin());
            writer.newLine();
            writer.write("license_plate: " + vehicle.getLicensePlate());
            writer.newLine();
            writer.write("timestamp: " + vehicle.getSubmissionTimestamp());
            writer.newLine();
            writer.write("vehicle_make: " + vehicle.getMake());
            writer.newLine();
            writer.write("vehicle_model: " + vehicle.getModel());
            writer.newLine();
            writer.write("vehicle_year: " + vehicle.getYear());
            writer.newLine();
            writer.write("computing_power: " + vehicle.getComputingPower());
            writer.newLine();
            writer.write("start_date: " + vehicle.getArrivalDate());
            writer.newLine();
            writer.write("end_date: " + vehicle.getDepartureDate());
            writer.newLine();
            writer.write("---");
            writer.newLine();
            return true;
        } catch (IOException ex) {
            System.err.println("Error writing to data file: " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Get all vehicles from the data file
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            return vehicles;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Map<String, String> vehicleData = new HashMap<>();
            boolean isVehicleBlock = false;
            int vehicleIdCounter = 1; // Auto-increment vehicle ID since it's not stored
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("type: vehicle_availability")) {
                    isVehicleBlock = true;
                    vehicleData.clear();
                } else if (line.equals("---")) {
                    if (isVehicleBlock && !vehicleData.isEmpty()) {
                        try {
                            // user_id in file is the OWNER ID, not vehicle ID
                            int ownerId = Integer.parseInt(vehicleData.getOrDefault("user_id", "0"));
                            
                            Vehicle vehicle = new Vehicle(
                                vehicleIdCounter++, // Auto-increment vehicle ID
                                ownerId, // This is the owner's user ID
                                vehicleData.getOrDefault("vehicle_make", ""),
                                vehicleData.getOrDefault("vehicle_model", ""),
                                Integer.parseInt(vehicleData.getOrDefault("vehicle_year", "0")),
                                vehicleData.getOrDefault("vin", ""),
                                vehicleData.getOrDefault("license_plate", ""),
                                vehicleData.getOrDefault("computing_power", "Medium"),
                                LocalDate.parse(vehicleData.getOrDefault("start_date", "2025-01-01")),
                                LocalDate.parse(vehicleData.getOrDefault("end_date", "2025-12-31")),
                                VehicleStatus.AVAILABLE
                            );
                            
                            // Set the timestamp from file if it exists
                            if (vehicleData.containsKey("timestamp")) {
                                vehicle.setSubmissionTimestamp(vehicleData.get("timestamp"));
                            }
                            
                            vehicles.add(vehicle);
                        } catch (Exception e) {
                            System.err.println("Error parsing vehicle: " + e.getMessage());
                        }
                    }
                    isVehicleBlock = false;
                } else if (isVehicleBlock && line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        vehicleData.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading vehicles: " + e.getMessage());
        }
        
        return vehicles;
    }
}