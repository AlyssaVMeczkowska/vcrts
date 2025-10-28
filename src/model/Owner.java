package model;

import java.util.List;

public class Owner extends User{
    private List<Vehicle> vehicles;

    public Owner(int id, String firstName, String lastName, String email, String username, String phoneNumber, String hashedPassword, String accountType, String creationTimestamp, boolean hasAgreedToTerms, List<Vehicle> vehicles) {
        super(id, firstName, lastName, email, username, phoneNumber, hashedPassword, accountType, creationTimestamp, hasAgreedToTerms);
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicleID){

    }

    public void deleteVehicle(Vehicle vehicleID){

    }
    
    public void submitVehicleToParkingLot(String vehicleID, Controller controllerID){

    }
}

