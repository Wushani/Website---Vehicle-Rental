package org.web.autolanka.service;

import java.util.List;
import java.util.Optional;

import org.web.autolanka.entity.Vehicle;

public interface VehicleService {
    Vehicle saveVehicle(Vehicle vehicle);
    Optional<Vehicle> getVehicleById(Long id);
    List<Vehicle> getVehiclesByOwnerId(Long ownerId);
    List<Vehicle> getAllVehicles();
    Vehicle updateVehicle(Long id, Vehicle vehicleDetails);
    void deleteVehicle(Long id);
    List<Vehicle> getVehiclesByAvailabilityStatus(String status);
    List<Vehicle> getVehiclesByType(String type);
    
    // Legacy methods for approval workflow
    List<Vehicle> getVehiclesByApprovalStatus(String approvalStatus);
    Vehicle approveVehicle(Long vehicleId, Long staffId);
    Vehicle rejectVehicle(Long vehicleId, Long staffId);
    
    // New methods for multi-step approval workflow
    List<Vehicle> getVehiclesByInsuranceApprovalStatus(String insuranceApprovalStatus);
    List<Vehicle> getVehiclesByStaffApprovalStatus(String staffApprovalStatus);
    Vehicle approveVehicleInsurance(Long vehicleId, Long officerId, String notes);
    Vehicle rejectVehicleInsurance(Long vehicleId, Long officerId, String notes);
    Vehicle approveVehicleByStaff(Long vehicleId, Long staffId, String notes);
    Vehicle rejectVehicleByStaff(Long vehicleId, Long staffId, String notes);
    
    // New method to get vehicles by both availability and approval status
    List<Vehicle> getVehiclesByAvailabilityAndApprovalStatus(String availabilityStatus, String approvalStatus);
    
    // Method to check if vehicle is fully approved (both insurance and staff)
    boolean isVehicleFullyApproved(Long vehicleId);
    
    // Method to get all fully approved vehicles
    List<Vehicle> getFullyApprovedVehicles();
}