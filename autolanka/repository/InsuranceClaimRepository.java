package org.web.autolanka.repository;

import org.web.autolanka.entity.InsuranceClaim;
import org.web.autolanka.entity.InsurancePolicy;
import org.web.autolanka.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {
    List<InsuranceClaim> findByPolicy(InsurancePolicy policy);
    List<InsuranceClaim> findByVehicle(Vehicle vehicle);
    List<InsuranceClaim> findByClaimStatus(String claimStatus);
    List<InsuranceClaim> findByPolicyPolicyId(Long policyId);
    List<InsuranceClaim> findByVehicleVehicleId(Long vehicleId);
    InsuranceClaim findByClaimNumber(String claimNumber);
    
    @Modifying
    @Query("DELETE FROM InsuranceClaim ic WHERE ic.vehicle.vehicleId = ?1")
    void deleteByVehicleVehicleId(Long vehicleId);
}