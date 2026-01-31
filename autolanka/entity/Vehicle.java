package org.web.autolanka.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonBackReference
    private VehicleOwner owner;
    
    @Column(name = "vehicle_number", unique = true, nullable = false, length = 20)
    private String vehicleNumber;
    
    @Column(name = "vehicle_type", nullable = false, length = 50)
    private String vehicleType;
    
    @Column(name = "register_year", nullable = false)
    private Integer registerYear;
    
    @Column(name = "license_number", nullable = false, length = 30)
    private String licenseNumber;
    
    @Column(name = "insurance_details", columnDefinition = "NVARCHAR(MAX)")
    private String insuranceDetails;
    
    @Column(name = "license_document_path", length = 255)
    private String licenseDocumentPath;
    
    @Column(name = "insurance_document_path", length = 255)
    private String insuranceDocumentPath;
    
    @Column(name = "availability_status", length = 20)
    private String availabilityStatus = "AVAILABLE";
    
    @Column(name = "photo_paths", columnDefinition = "NVARCHAR(MAX)")
    private String photoPaths;
    
    @Column(name = "maintenance_records", columnDefinition = "NVARCHAR(MAX)")
    private String maintenanceRecords;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
    
    // Fields for multi-step approval workflow
    @Column(name = "insurance_approval_status", length = 20)
    private String insuranceApprovalStatus = "PENDING"; // PENDING, APPROVED, REJECTED
    
    @Column(name = "staff_approval_status", length = 20)
    private String staffApprovalStatus = "PENDING"; // PENDING, APPROVED, REJECTED
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_officer_id")
    private InsuranceOfficer insuranceOfficer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_member_id")
    private StaffMember staffMember;
    
    @Column(name = "insurance_approval_date")
    private LocalDateTime insuranceApprovalDate;
    
    @Column(name = "staff_approval_date")
    private LocalDateTime staffApprovalDate;
    
    @Column(name = "insurance_officer_notes", columnDefinition = "NVARCHAR(MAX)")
    private String insuranceOfficerNotes;
    
    @Column(name = "staff_member_notes", columnDefinition = "NVARCHAR(MAX)")
    private String staffMemberNotes;
    
    // Legacy field for approval status (kept for backward compatibility)
    @Column(name = "approval_status", length = 20)
    private String approvalStatus = "PENDING"; // PENDING, APPROVED, REJECTED
    
    // New fields
    @Column(name = "make", length = 50)
    private String make;
    
    @Column(name = "model", length = 50)
    private String model;
    
    @Column(name = "seat_capacity")
    private Integer seatCapacity;
    
    @Column(name = "rental_price_per_day", precision = 10, scale = 2)
    private BigDecimal rentalPricePerDay;
    
    @Column(name = "color", length = 30)
    private String color;
    
    @Column(name = "fuel_type", length = 30)
    private String fuelType;
    
    @Column(name = "transmission", length = 20)
    private String transmission;
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(VehicleOwner owner, String vehicleNumber, String vehicleType, 
                   Integer registerYear) {
        this.owner = owner;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.registerYear = registerYear;
        this.approvalStatus = "PENDING"; // Default to PENDING for new vehicles
        this.insuranceApprovalStatus = "PENDING"; // Default to PENDING for new vehicles
        this.staffApprovalStatus = "PENDING"; // Default to PENDING for new vehicles
    }
    
    // Getters and Setters
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    
    public VehicleOwner getOwner() { return owner; }
    public void setOwner(VehicleOwner owner) { this.owner = owner; }
    
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
    
    public String getLicenseDocumentPath() { return licenseDocumentPath; }
    public void setLicenseDocumentPath(String licenseDocumentPath) { this.licenseDocumentPath = licenseDocumentPath; }
    
    public String getInsuranceDocumentPath() { return insuranceDocumentPath; }
    public void setInsuranceDocumentPath(String insuranceDocumentPath) { this.insuranceDocumentPath = insuranceDocumentPath; }
    
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    
    public String getPhotoPaths() { return photoPaths; }
    public void setPhotoPaths(String photoPaths) { this.photoPaths = photoPaths; }
    
    public String getMaintenanceRecords() { return maintenanceRecords; }
    public void setMaintenanceRecords(String maintenanceRecords) { this.maintenanceRecords = maintenanceRecords; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    
    // Getters and Setters for multi-step approval workflow
    public String getInsuranceApprovalStatus() { 
        return insuranceApprovalStatus != null ? insuranceApprovalStatus.toUpperCase() : "PENDING"; 
    }
    public void setInsuranceApprovalStatus(String insuranceApprovalStatus) { 
        this.insuranceApprovalStatus = insuranceApprovalStatus != null ? insuranceApprovalStatus.toUpperCase() : "PENDING"; 
    }
    
    public String getStaffApprovalStatus() { 
        return staffApprovalStatus != null ? staffApprovalStatus.toUpperCase() : "PENDING"; 
    }
    public void setStaffApprovalStatus(String staffApprovalStatus) { 
        this.staffApprovalStatus = staffApprovalStatus != null ? staffApprovalStatus.toUpperCase() : "PENDING"; 
    }
    
    public InsuranceOfficer getInsuranceOfficer() { return insuranceOfficer; }
    public void setInsuranceOfficer(InsuranceOfficer insuranceOfficer) { this.insuranceOfficer = insuranceOfficer; }
    
    public StaffMember getStaffMember() { return staffMember; }
    public void setStaffMember(StaffMember staffMember) { this.staffMember = staffMember; }
    
    public LocalDateTime getInsuranceApprovalDate() { return insuranceApprovalDate; }
    public void setInsuranceApprovalDate(LocalDateTime insuranceApprovalDate) { this.insuranceApprovalDate = insuranceApprovalDate; }
    
    public LocalDateTime getStaffApprovalDate() { return staffApprovalDate; }
    public void setStaffApprovalDate(LocalDateTime staffApprovalDate) { this.staffApprovalDate = staffApprovalDate; }
    
    public String getInsuranceOfficerNotes() { return insuranceOfficerNotes; }
    public void setInsuranceOfficerNotes(String insuranceOfficerNotes) { this.insuranceOfficerNotes = insuranceOfficerNotes; }
    
    public String getStaffMemberNotes() { return staffMemberNotes; }
    public void setStaffMemberNotes(String staffMemberNotes) { this.staffMemberNotes = staffMemberNotes; }
    
    // Getters and Setters for legacy approval status
    public String getApprovalStatus() { 
        // For backward compatibility, return APPROVED only if both insurance and staff have approved
        if ("APPROVED".equals(this.insuranceApprovalStatus) && "APPROVED".equals(this.staffApprovalStatus)) {
            return "APPROVED";
        } else if ("REJECTED".equals(this.insuranceApprovalStatus) || "REJECTED".equals(this.staffApprovalStatus)) {
            return "REJECTED";
        } else {
            return "PENDING";
        }
    }
    
    public void setApprovalStatus(String approvalStatus) { 
        this.approvalStatus = approvalStatus != null ? approvalStatus.toUpperCase() : "PENDING";
        // When setting the legacy approval status, also update the new fields for consistency
        if ("APPROVED".equals(approvalStatus)) {
            this.insuranceApprovalStatus = "APPROVED";
            this.staffApprovalStatus = "APPROVED";
        } else if ("REJECTED".equals(approvalStatus)) {
            this.insuranceApprovalStatus = "REJECTED";
            this.staffApprovalStatus = "REJECTED";
        } else {
            // For PENDING or any other status, we keep the individual statuses as they are
            // This allows for more granular control in the new workflow
        }
    }
    
    // New method to check if vehicle is fully approved
    public boolean isFullyApproved() {
        return "APPROVED".equals(this.insuranceApprovalStatus) && "APPROVED".equals(this.staffApprovalStatus);
    }
    
    // Getters and Setters for new fields
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Integer getSeatCapacity() { return seatCapacity; }
    public void setSeatCapacity(Integer seatCapacity) { this.seatCapacity = seatCapacity; }
    
    public BigDecimal getRentalPricePerDay() { return rentalPricePerDay; }
    public void setRentalPricePerDay(BigDecimal rentalPricePerDay) { this.rentalPricePerDay = rentalPricePerDay; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
}