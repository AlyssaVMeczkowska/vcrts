package model;

import java.time.LocalDateTime;

public class Vehicle {
    private int userId;
    private String make;
    private String model;
    private int year;
    private String vin;
    private String licensePlate;
    private String computingPower;
    private String startDate;
    private String endDate;
    private String submissionTimestamp;

    public Vehicle(int userId, String make, String model, int year, String vin, String licensePlate, String computingPower, String startDate, String endDate) {
        this.userId = userId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.computingPower = computingPower;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submissionTimestamp = LocalDateTime.now().toString();
    }

    // --- Getters ---
    public int getUserId() { return userId; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getVin() { return vin; }
    public String getLicensePlate() { return licensePlate; }
    public String getComputingPower() { return computingPower; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getSubmissionTimestamp() { return submissionTimestamp; }
}