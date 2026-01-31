package org.web.autolanka.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.InsuranceOfficerVehicleApproval;

@Repository
public interface InsuranceOfficerVehicleApprovalRepository extends JpaRepository<InsuranceOfficerVehicleApproval, Long> {
    List<InsuranceOfficerVehicleApproval> findByVehicleVehicleId(Long vehicleId);
    List<InsuranceOfficerVehicleApproval> findByInsuranceOfficerOfficerId(Long officerId);
    List<InsuranceOfficerVehicleApproval> findByApprovalStatus(String approvalStatus);
    Optional<InsuranceOfficerVehicleApproval> findByVehicleVehicleIdAndInsuranceOfficerOfficerId(Long vehicleId, Long officerId);
    List<InsuranceOfficerVehicleApproval> findByVehicleVehicleIdAndApprovalStatus(Long vehicleId, String approvalStatus);
    
    @Modifying
    @Query("DELETE FROM InsuranceOfficerVehicleApproval a WHERE a.vehicle.vehicleId = ?1")
    void deleteByVehicleVehicleId(Long vehicleId);
}