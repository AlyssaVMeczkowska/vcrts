package model;

import java.time.LocalDateTime;

public class Vehicle {
    private String make;
    private String model;
    private int year;
    private String computingPower;
    private String startDate;
    private String endDate;
    private String submissionTimestamp;

    public Vehicle(String make, String model, int year, String computingPower, String startDate, String endDate) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.computingPower = computingPower;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submissionTimestamp = LocalDateTime.now().toString();
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getComputingPower() { return computingPower; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getSubmissionTimestamp() { return submissionTimestamp; }
}
