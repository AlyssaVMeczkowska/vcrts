package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Owner extends User
{
    private List<Vehicle> vehicles;

    public Owner(int id, String firstName, String lastName, String email, String username, String phoneNumber, String hashedPassword, String accountType, String creationTimestamp, boolean hasAgreedToTerms, List<Vehicle> vehicles) {
        super(id, firstName, lastName, email, username, phoneNumber, hashedPassword, accountType, creationTimestamp, hasAgreedToTerms);
        this.vehicles = vehicles;
    }

    //New Method to get list of vehicles
    public List<Vehicle> getVehicles()
    {
        return new ArrayList<>(vehicles);
    }

    //Implemented Method to add new vehicles
    public void addVehicle(Vehicle vehicle)
    {
        if (vehicle == null) return;

        boolean exists = vehicles.stream()
                .anyMatch(v -> v.getVehicleId() == vehicle.getVehicleId());
        if (exists)
        {
            throw new IllegalArgumentException("Vehicle already exists for this owner.");
        }
        vehicles.add(vehicle);
    }

    //Implemented Method to delete new vehicles
    public void deleteVehicle(Vehicle vehicle)
    {
        if (vehicle == null) return;

        Iterator<Vehicle> iterator = vehicles.iterator();
        while (iterator.hasNext())
        {
            if (iterator.next().getVehicleId() == vehicle.getVehicleId())
            {
                iterator.remove();
                return;
            }
        }
        throw new IllegalArgumentException("Vehicle to be deleted does not exist for this owner.");
    }

    //To be possibly updated when Controller.java is implemented
    public void submitVehicleToParkingLot(String vehicleID, Controller controller)
    {
        if (controller == null || vehicleID == null || vehicleID.isEmpty())
        {
            throw new IllegalArgumentException("Controller or vehicle ID cannot be null or empty.");
        }

        Vehicle vehicle = vehicles.stream()
                .filter(v -> String.valueOf(v.getVehicleId()).equals(vehicleID))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found for submission."));

        // Placeholder: to be integrated with Controller.java later
        // controller.submitVehicle(vehicle);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
    }
}

