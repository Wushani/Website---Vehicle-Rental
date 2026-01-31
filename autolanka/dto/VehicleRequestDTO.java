package org.web.autolanka.dto;

public class VehicleRequestDTO {
    private String vehicleNumber;
    private String vehicleType;
    private Integer registerYear;
    private String licenseNumber;
    private String insuranceDetails;
    private String availabilityStatus;
    private String photoPaths;
    private String maintenanceRecords;
    
    // Constructors
    public VehicleRequestDTO() {}
    
    public VehicleRequestDTO(String vehicleNumber, String vehicleType, Integer registerYear, 
                            String licenseNumber, String insuranceDetails, String availabilityStatus, 
                            String photoPaths, String maintenanceRecords) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.registerYear = registerYear;
        this.licenseNumber = licenseNumber;
        this.insuranceDetails = insuranceDetails;
        this.availabilityStatus = availabilityStatus;
        this.photoPaths = photoPaths;
        this.maintenanceRecords = maintenanceRecords;
    }
    
    // Getters and Setters
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    
    public Integer getRegisterYear() { return registerYear; }
    public void setRegisterYear(Integer registerYear) { this.registerYear = registerYear; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getInsuranceDetails() { return insuranceDetails; }
    public void setInsuranceDetails(String insuranceDetails) { this.insuranceDetails = insuranceDetails; }
    
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    
    public String getPhotoPaths() { return photoPaths; }
    public void setPhotoPaths(String photoPaths) { this.photoPaths = photoPaths; }
    
    public String getMaintenanceRecords() { return maintenanceRecords; }
    public void setMaintenanceRecords(String maintenanceRecords) { this.maintenanceRecords = maintenanceRecords; }
}