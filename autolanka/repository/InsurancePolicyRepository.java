package org.web.autolanka.repository;

import org.web.autolanka.entity.InsurancePolicy;
import org.web.autolanka.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {
    List<InsurancePolicy> findByVehicle(Vehicle vehicle);
    List<InsurancePolicy> findByVehicleVehicleId(Long vehicleId);
    List<InsurancePolicy> findByPolicyStatus(String policyStatus);
    List<InsurancePolicy> findByInsuranceCompany(String insuranceCompany);
    InsurancePolicy findByPolicyNumber(String policyNumber);
    
    @Modifying
    @Query("DELETE FROM InsurancePolicy ip WHERE ip.vehicle.vehicleId = ?1")
    void deleteByVehicleVehicleId(Long vehicleId);
}