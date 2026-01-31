package org.web.autolanka.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.autolanka.entity.InsuranceClaim;
import org.web.autolanka.entity.InsuranceOfficerVehicleApproval;
import org.web.autolanka.entity.InsurancePolicy;
import org.web.autolanka.repository.InsuranceClaimRepository;
import org.web.autolanka.repository.InsuranceOfficerVehicleApprovalRepository;
import org.web.autolanka.repository.InsurancePolicyRepository;
import org.web.autolanka.service.InsuranceService;

@Service
public class InsuranceServiceImpl implements InsuranceService {
    
    @Autowired
    private InsurancePolicyRepository policyRepository;
    
    @Autowired
    private InsuranceClaimRepository claimRepository;
    
    @Autowired
    private InsuranceOfficerVehicleApprovalRepository insuranceOfficerVehicleApprovalRepository;
    
    // Insurance Policy methods
    @Override
    public InsurancePolicy savePolicy(InsurancePolicy policy) {
        return policyRepository.save(policy);
    }
    
    @Override
    public Optional<InsurancePolicy> getPolicyById(Long id) {
        return policyRepository.findById(id);
    }
    
    @Override
    public List<InsurancePolicy> getPoliciesByVehicleId(Long vehicleId) {
        return policyRepository.findByVehicleVehicleId(vehicleId);
    }
    
    @Override
    public List<InsurancePolicy> getPoliciesByStatus(String status) {
        return policyRepository.findByPolicyStatus(status);
    }
    
    @Override
    public List<InsurancePolicy> getAllPolicies() {
        return policyRepository.findAll();
    }
    
    @Override
    public InsurancePolicy updatePolicy(Long id, InsurancePolicy policyDetails) {
        Optional<InsurancePolicy> policyOpt = policyRepository.findById(id);
        if (policyOpt.isPresent()) {
            InsurancePolicy policy = policyOpt.get();
            policy.setPolicyNumber(policyDetails.getPolicyNumber());
            policy.setInsuranceCompany(policyDetails.getInsuranceCompany());
            policy.setPolicyType(policyDetails.getPolicyType());
            policy.setStartDate(policyDetails.getStartDate());
            policy.setEndDate(policyDetails.getEndDate());
            policy.setPremiumAmount(policyDetails.getPremiumAmount());
            policy.setCoverageAmount(policyDetails.getCoverageAmount());
            policy.setPolicyStatus(policyDetails.getPolicyStatus());
            policy.setDocumentPath(policyDetails.getDocumentPath());
            policy.setUpdatedDate(java.time.LocalDateTime.now());
            return policyRepository.save(policy);
        }
        return null;
    }
    
    @Override
    public void deletePolicy(Long id) {
        policyRepository.deleteById(id);
    }
    
    @Override
    public Optional<InsurancePolicy> getPolicyByNumber(String policyNumber) {
        return Optional.ofNullable(policyRepository.findByPolicyNumber(policyNumber));
    }
    
    // Insurance Claim methods
    @Override
    public InsuranceClaim saveClaim(InsuranceClaim claim) {
        return claimRepository.save(claim);
    }
    
    @Override
    public Optional<InsuranceClaim> getClaimById(Long id) {
        return claimRepository.findById(id);
    }
    
    @Override
    public List<InsuranceClaim> getClaimsByPolicyId(Long policyId) {
        return claimRepository.findByPolicyPolicyId(policyId);
    }
    
    @Override
    public List<InsuranceClaim> getClaimsByVehicleId(Long vehicleId) {
        return claimRepository.findByVehicleVehicleId(vehicleId);
    }
    
    @Override
    public List<InsuranceClaim> getClaimsByStatus(String status) {
        return claimRepository.findByClaimStatus(status);
    }
    
    @Override
    public List<InsuranceClaim> getAllClaims() {
        return claimRepository.findAll();
    }
    
    @Override
    public InsuranceClaim updateClaim(Long id, InsuranceClaim claimDetails) {
        Optional<InsuranceClaim> claimOpt = claimRepository.findById(id);
        if (claimOpt.isPresent()) {
            InsuranceClaim claim = claimOpt.get();
            claim.setClaimNumber(claimDetails.getClaimNumber());
            claim.setIncidentDate(claimDetails.getIncidentDate());
            claim.setIncidentDescription(claimDetails.getIncidentDescription());
            claim.setClaimAmount(claimDetails.getClaimAmount());
            claim.setApprovedAmount(claimDetails.getApprovedAmount());
            claim.setClaimStatus(claimDetails.getClaimStatus());
            claim.setDocumentPaths(claimDetails.getDocumentPaths());
            claim.setOfficerNotes(claimDetails.getOfficerNotes());
            claim.setUpdatedDate(java.time.LocalDateTime.now());
            return claimRepository.save(claim);
        }
        return null;
    }
    
    @Override
    public void deleteClaim(Long id) {
        claimRepository.deleteById(id);
    }
    
    @Override
    public Optional<InsuranceClaim> getClaimByNumber(String claimNumber) {
        return Optional.ofNullable(claimRepository.findByClaimNumber(claimNumber));
    }
    
    // Insurance Officer Vehicle Approval methods
    @Override
    public Optional<InsuranceOfficerVehicleApproval> getInsuranceOfficerVehicleApprovalByVehicleAndOfficer(Long vehicleId, Long officerId) {
        return insuranceOfficerVehicleApprovalRepository.findByVehicleVehicleIdAndInsuranceOfficerOfficerId(vehicleId, officerId);
    }
}