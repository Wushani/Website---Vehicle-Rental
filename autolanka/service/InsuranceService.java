package org.web.autolanka.service;

import java.util.List;
import java.util.Optional;

import org.web.autolanka.entity.InsuranceClaim;
import org.web.autolanka.entity.InsuranceOfficerVehicleApproval;
import org.web.autolanka.entity.InsurancePolicy;

public interface InsuranceService {
    // Insurance Policy methods
    InsurancePolicy savePolicy(InsurancePolicy policy);
    Optional<InsurancePolicy> getPolicyById(Long id);
    List<InsurancePolicy> getPoliciesByVehicleId(Long vehicleId);
    List<InsurancePolicy> getPoliciesByStatus(String status);
    List<InsurancePolicy> getAllPolicies();
    InsurancePolicy updatePolicy(Long id, InsurancePolicy policyDetails);
    void deletePolicy(Long id);
    Optional<InsurancePolicy> getPolicyByNumber(String policyNumber);
    
    // Insurance Claim methods
    InsuranceClaim saveClaim(InsuranceClaim claim);
    Optional<InsuranceClaim> getClaimById(Long id);
    List<InsuranceClaim> getClaimsByPolicyId(Long policyId);
    List<InsuranceClaim> getClaimsByVehicleId(Long vehicleId);
    List<InsuranceClaim> getClaimsByStatus(String status);
    List<InsuranceClaim> getAllClaims();
    InsuranceClaim updateClaim(Long id, InsuranceClaim claimDetails);
    void deleteClaim(Long id);
    Optional<InsuranceClaim> getClaimByNumber(String claimNumber);
    
    // Insurance Officer Vehicle Approval methods
    Optional<InsuranceOfficerVehicleApproval> getInsuranceOfficerVehicleApprovalByVehicleAndOfficer(Long vehicleId, Long officerId);
}