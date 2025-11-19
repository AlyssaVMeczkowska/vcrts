package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Owner extends User
{
    private List<Vehicle> vehicles;

    public Owner(int id, String firstName, String lastName, String email, String username, String phoneNumber, String hashedPassword, String accountType, String creationTimestamp, boolean hasAgreedToTerms, List<Vehicle> vehicles) {
        super(id, firstName, lastName, email, username, phoneNumber, hashedPassword, accountType, creationTimestamp, hasAgreedToTerms);
        this.vehicles = vehicles;
    }


    public List<Vehicle> getVehicles()
    {
        return new ArrayList<>(vehicles);
    }


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


    public void requestVehicleSubmission(Vehicle vehicle, Controller controller)
    {
        if (controller == null || vehicle == null)
        {
            throw new IllegalArgumentException("Controller or vehicle cannot be null.");
        }

        boolean isOwner = vehicles.stream()
                .anyMatch(v -> v.getVehicleId() == vehicle.getVehicleId());

        if (!isOwner)
        {
            throw new IllegalArgumentException("Owner does not own this vehicle. Cannot submit.");
        }

        System.out.println("OWNER (" + getUsername() + "): Sending request to server for vehicle " + vehicle.getLicensePlate());
        controller.receiveVehicleSubmissionRequest(this, vehicle);
    }


    public void receiveNotification(String message)
    {
        System.out.println("--- NOTIFICATION for " + getUsername() + " ---");
        System.out.println(message);
        System.out.println("----------------------------------------");
    }


}

