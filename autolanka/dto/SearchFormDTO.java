// SearchFormDTO.java
package org.web.autolanka.dto;

public class SearchFormDTO {
    private String location;
    private String pickupDate;
    private String returnDate;
    private String vehicleType;

    // Constructors
    public SearchFormDTO() {}

    public SearchFormDTO(String location, String pickupDate, String returnDate, String vehicleType) {
        this.location = location;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.vehicleType = vehicleType;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPickupDate() { return pickupDate; }
    public void setPickupDate(String pickupDate) { this.pickupDate = pickupDate; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}