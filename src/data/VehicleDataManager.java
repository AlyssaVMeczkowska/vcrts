package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import model.Vehicle;

public class VehicleDataManager {

    private static final String FILE_PATH = "data/owners_data.txt";

    private boolean isValueTaken(String key, String value) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(key)) {
                    String fileValue = line.split(":", 2)[1].trim();
                    if (fileValue.equalsIgnoreCase(value)) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading owners data file: " + ex.getMessage());
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
            writer.write("user_id: " + vehicle.getUserId());
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
            writer.write("start_date: " + vehicle.getStartDate());
            writer.newLine();
            writer.write("end_date: " + vehicle.getEndDate());
            writer.newLine();
            writer.write("---");
            writer.newLine();
            return true;
        } catch (IOException ex) {
            System.err.println("Error writing to file: " + ex.getMessage());
            return false;
        }
    }
}
