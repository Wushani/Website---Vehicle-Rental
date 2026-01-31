package org.web.autolanka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.autolanka.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByOwnerOwnerId(Long ownerId);
    List<Vehicle> findByVehicleNumber(String vehicleNumber);
    List<Vehicle> findByAvailabilityStatus(String availabilityStatus);
    List<Vehicle> findByVehicleType(String vehicleType);
    
    // Methods for legacy approval workflow
    List<Vehicle> findByApprovalStatus(String approvalStatus);
    
    // Methods for multi-step approval workflow
    List<Vehicle> findByInsuranceApprovalStatus(String insuranceApprovalStatus);
    List<Vehicle> findByStaffApprovalStatus(String staffApprovalStatus);
    List<Vehicle> findByInsuranceApprovalStatusAndStaffApprovalStatus(String insuranceApprovalStatus, String staffApprovalStatus);
    
    // New search methods for the additional fields
    List<Vehicle> findByMakeContainingIgnoreCase(String make);
    List<Vehicle> findByModelContainingIgnoreCase(String model);
    List<Vehicle> findByColorContainingIgnoreCase(String color);
    List<Vehicle> findByFuelType(String fuelType);
    List<Vehicle> findByTransmission(String transmission);
}