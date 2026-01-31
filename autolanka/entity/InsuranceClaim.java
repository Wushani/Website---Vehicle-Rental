package org.web.autolanka.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Long claimId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private InsurancePolicy policy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @Column(name = "claim_number", unique = true, nullable = false, length = 50)
    private String claimNumber;
    
    @Column(name = "incident_date", nullable = false)
    private LocalDateTime incidentDate;
    
    @Column(name = "incident_description", columnDefinition = "NVARCHAR(MAX)")
    private String incidentDescription;
    
    @Column(name = "claim_amount", precision = 10, scale = 2)
    private BigDecimal claimAmount;
    
    @Column(name = "approved_amount", precision = 10, scale = 2)
    private BigDecimal approvedAmount;
    
    @Column(name = "claim_status", length = 20)
    private String claimStatus = "PENDING"; // PENDING, APPROVED, REJECTED, PAID
    
    @Column(name = "document_paths", columnDefinition = "NVARCHAR(MAX)")
    private String documentPaths; // Comma-separated paths to claim documents
    
    @Column(name = "officer_notes", columnDefinition = "NVARCHAR(MAX)")
    private String officerNotes;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
    
    // Constructors
    public InsuranceClaim() {}
    
    public InsuranceClaim(InsurancePolicy policy, Vehicle vehicle, String claimNumber, 
                         LocalDateTime incidentDate, String incidentDescription, 
                         BigDecimal claimAmount) {
        this.policy = policy;
        this.vehicle = vehicle;
        this.claimNumber = claimNumber;
        this.incidentDate = incidentDate;
        this.incidentDescription = incidentDescription;
        this.claimAmount = claimAmount;
    }
    
    // Getters and Setters
    public Long getClaimId() { return claimId; }
    public void setClaimId(Long claimId) { this.claimId = claimId; }
    
    public InsurancePolicy getPolicy() { return policy; }
    public void setPolicy(InsurancePolicy policy) { this.policy = policy; }
    
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    
    public LocalDateTime getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDateTime incidentDate) { this.incidentDate = incidentDate; }
    
    public String getIncidentDescription() { return incidentDescription; }
    public void setIncidentDescription(String incidentDescription) { this.incidentDescription = incidentDescription; }
    
    public BigDecimal getClaimAmount() { return claimAmount; }
    public void setClaimAmount(BigDecimal claimAmount) { this.claimAmount = claimAmount; }
    
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public String getClaimStatus() { return claimStatus; }
    public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
    
    public String getDocumentPaths() { return documentPaths; }
    public void setDocumentPaths(String documentPaths) { this.documentPaths = documentPaths; }
    
    public String getOfficerNotes() { return officerNotes; }
    public void setOfficerNotes(String officerNotes) { this.officerNotes = officerNotes; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
}