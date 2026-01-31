package org.web.autolanka.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_officers")
public class InsuranceOfficer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "officer_id")
    private Long officerId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "officer_code", unique = true, nullable = false, length = 20)
    private String officerCode;
    
    @Column(name = "insurance_company", nullable = false, length = 100)
    private String insuranceCompany;
    
    @Column(name = "license_number", length = 30)
    private String licenseNumber;
    
    @Column(name = "hire_date")
    private LocalDateTime hireDate = LocalDateTime.now();
    
    // Constructors
    public InsuranceOfficer() {}
    
    public InsuranceOfficer(User user, String officerCode, String insuranceCompany, String licenseNumber) {
        this.user = user;
        this.officerCode = officerCode;
        this.insuranceCompany = insuranceCompany;
        this.licenseNumber = licenseNumber;
    }
    
    // Getters and Setters
    public Long getOfficerId() { return officerId; }
    public void setOfficerId(Long officerId) { this.officerId = officerId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getOfficerCode() { return officerCode; }
    public void setOfficerCode(String officerCode) { this.officerCode = officerCode; }
    
    public String getInsuranceCompany() { return insuranceCompany; }
    public void setInsuranceCompany(String insuranceCompany) { this.insuranceCompany = insuranceCompany; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public LocalDateTime getHireDate() { return hireDate; }
    public void setHireDate(LocalDateTime hireDate) { this.hireDate = hireDate; }
}