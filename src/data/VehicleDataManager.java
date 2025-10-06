package data;

import model.Vehicle; // Import the Vehicle model

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class VehicleDataManager {

    private static final String FILE_PATH = "owners_data.txt";

    public boolean addVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
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
