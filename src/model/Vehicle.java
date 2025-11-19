package model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Vehicle {
    private int vehicleId;
    private int ownerId;
    private String make;
    private String model;
    private int year;
    private String vin;
    private String licensePlate;
    private String computingPower;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private VehicleStatus status;
    private String submissionTimestamp;

    public Vehicle(int vehicleId, int ownerId, String make, String model, int year, String vin, String licensePlate, String computingPower, LocalDate arrivalDate, LocalDate departureDate, VehicleStatus status) {
        this.vehicleId = vehicleId;
        this.ownerId = ownerId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.computingPower = computingPower;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.status = status;
        this.submissionTimestamp = OffsetDateTime.now().toString();
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getVin() {
        return vin;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getComputingPower() {
        return computingPower;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public VehicleStatus getStatus() {
        return status;
    }
    public void setSubmissionTimestamp(String timestamp) {
        this.submissionTimestamp = timestamp;
    }

    public String getSubmissionTimestamp() {
        return submissionTimestamp;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
    public int getOwnerId() {
        return ownerId;
    }
}