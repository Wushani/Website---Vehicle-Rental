package org.web.autolanka.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "vehicle_owners")
public class VehicleOwner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long ownerId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
    
    @Column(unique = true, nullable = false, length = 12)
    @Pattern(regexp = "^([0-9]{12}|[0-9]{9}[vV])$", 
             message = "Please provide a valid NIC number (12 digits or 9 digits followed by V/v)")
    private String nic;
    
    @Column(name = "business_name", length = 100)
    private String businessName;
    
    @Column(name = "driving_license", length = 20)
    private String drivingLicense;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    // Constructors
    public VehicleOwner() {}
    
    public VehicleOwner(User user, String nic, String businessName) {
        this.user = user;
        this.nic = nic;
        this.businessName = businessName;
    }
    
    // Getters and Setters
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    
    public String getDrivingLicense() { return drivingLicense; }
    public void setDrivingLicense(String drivingLicense) { this.drivingLicense = drivingLicense; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}