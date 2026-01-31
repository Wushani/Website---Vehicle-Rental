package org.web.autolanka.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.autolanka.entity.InsuranceOfficer;
import org.web.autolanka.entity.StaffMember;
import org.web.autolanka.entity.Vehicle;
import org.web.autolanka.repository.BookingRepository;
import org.web.autolanka.repository.InsuranceClaimRepository;
import org.web.autolanka.repository.InsuranceOfficerRepository;
import org.web.autolanka.repository.InsuranceOfficerVehicleApprovalRepository;
import org.web.autolanka.repository.InsurancePolicyRepository;
import org.web.autolanka.repository.StaffMemberRepository;
import org.web.autolanka.repository.VehicleRepository;
import org.web.autolanka.service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;
    
    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;
    
    @Autowired
    private InsuranceOfficerRepository insuranceOfficerRepository;
    
    @Autowired
    private StaffMemberRepository staffMemberRepository;
    
    @Autowired
    private InsuranceOfficerVehicleApprovalRepository insuranceOfficerVehicleApprovalRepository;
    
    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        vehicle.setUpdatedDate(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }
    
    @Override
    public Optional<Vehicle> getVehicleById(Long id) {
        System.out.println("VehicleServiceImpl: Finding vehicle with ID: " + id);
        Optional<Vehicle> result = vehicleRepository.findById(id);
        System.out.println("VehicleServiceImpl: Found vehicle: " + (result.isPresent() ? "Yes" : "No"));
        if (result.isPresent()) {
            System.out.println("VehicleServiceImpl: Vehicle details - ID: " + result.get().getVehicleId() + 
                              ", Number: " + result.get().getVehicleNumber() + 
                              ", Owner ID: " + result.get().getOwner().getOwnerId());
        }
        return result;
    }
    
    @Override
    public List<Vehicle> getVehiclesByOwnerId(Long ownerId) {
        System.out.println("VehicleServiceImpl: Finding vehicles for owner ID: " + ownerId);
        List<Vehicle> result = vehicleRepository.findByOwnerOwnerId(ownerId);
        System.out.println("VehicleServiceImpl: Found " + (result != null ? result.size() : "null") + " vehicles for owner ID: " + ownerId);
        return result;
    }
    
    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    @Override
    @Transactional
    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setVehicleNumber(vehicleDetails.getVehicleNumber());
            vehicle.setMake(vehicleDetails.getMake());
            vehicle.setModel(vehicleDetails.getModel());
            vehicle.setRegisterYear(vehicleDetails.getRegisterYear());
            vehicle.setVehicleType(vehicleDetails.getVehicleType());
            vehicle.setSeatCapacity(vehicleDetails.getSeatCapacity());
            vehicle.setRentalPricePerDay(vehicleDetails.getRentalPricePerDay());
            vehicle.setColor(vehicleDetails.getColor());
            vehicle.setFuelType(vehicleDetails.getFuelType());
            vehicle.setTransmission(vehicleDetails.getTransmission());
            vehicle.setLicenseNumber(vehicleDetails.getLicenseNumber());
            vehicle.setInsuranceDetails(vehicleDetails.getInsuranceDetails());
            
            // Handle license document path
            // If it's an empty string, that means user wants to remove it
            if (vehicleDetails.getLicenseDocumentPath() != null) {
                vehicle.setLicenseDocumentPath(vehicleDetails.getLicenseDocumentPath());
            }
            
            // Handle insurance document path
            // If it's an empty string, that means user wants to remove it
            if (vehicleDetails.getInsuranceDocumentPath() != null) {
                vehicle.setInsuranceDocumentPath(vehicleDetails.getInsuranceDocumentPath());
            }
            
            vehicle.setAvailabilityStatus(vehicleDetails.getAvailabilityStatus());
            
            // Handle photo paths
            // If it's an empty string, that means user wants to remove all photos
            if (vehicleDetails.getPhotoPaths() != null) {
                vehicle.setPhotoPaths(vehicleDetails.getPhotoPaths());
            }
            
            vehicle.setMaintenanceRecords(vehicleDetails.getMaintenanceRecords());
            vehicle.setUpdatedDate(LocalDateTime.now());
            return vehicleRepository.save(vehicle);
        }
        return null;
    }
    
    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        System.out.println("VehicleServiceImpl: Deleting vehicle with ID: " + id);
        try {
            // First, delete all related bookings
            System.out.println("VehicleServiceImpl: Deleting related bookings for vehicle ID: " + id);
            bookingRepository.deleteByVehicleVehicleId(id);
            
            // Then, delete all related insurance claims
            System.out.println("VehicleServiceImpl: Deleting related insurance claims for vehicle ID: " + id);
            insuranceClaimRepository.deleteByVehicleVehicleId(id);
            
            // Then, delete all related insurance policies
            System.out.println("VehicleServiceImpl: Deleting related insurance policies for vehicle ID: " + id);
            insurancePolicyRepository.deleteByVehicleVehicleId(id);
            
            // Then, delete all related insurance officer approvals
            System.out.println("VehicleServiceImpl: Deleting related insurance officer approvals for vehicle ID: " + id);
            insuranceOfficerVehicleApprovalRepository.deleteByVehicleVehicleId(id);
            
            // Finally, delete the vehicle itself
            System.out.println("VehicleServiceImpl: Deleting vehicle from database with ID: " + id);
            vehicleRepository.deleteById(id);
            System.out.println("VehicleServiceImpl: Successfully deleted vehicle with ID: " + id);
        } catch (Exception e) {
            System.err.println("VehicleServiceImpl: Error deleting vehicle with ID: " + id + " - " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception so it can be handled by the controller
        }
    }
    
    @Override
    public List<Vehicle> getVehiclesByAvailabilityStatus(String status) {
        return vehicleRepository.findByAvailabilityStatus(status);
    }
    
    @Override
    public List<Vehicle> getVehiclesByType(String type) {
        return vehicleRepository.findByVehicleType(type);
    }
    
    @Override
    public List<Vehicle> getVehiclesByAvailabilityAndApprovalStatus(String availabilityStatus, String approvalStatus) {
        // Get vehicles by availability status
        List<Vehicle> vehicles = vehicleRepository.findByAvailabilityStatus(availabilityStatus);
        
        // Filter by approval status
        return vehicles.stream()
            .filter(v -> approvalStatus.equals(v.getApprovalStatus()))
            .collect(Collectors.toList());
    }
    
    // Legacy methods for approval workflow
    @Override
    public List<Vehicle> getVehiclesByApprovalStatus(String approvalStatus) {
        return vehicleRepository.findByApprovalStatus(approvalStatus);
    }
    
    @Override
    @Transactional
    public Vehicle approveVehicle(Long vehicleId, Long staffId) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setApprovalStatus("APPROVED");
            vehicle.setAvailabilityStatus("AVAILABLE");
            vehicle.setUpdatedDate(LocalDateTime.now());
            return vehicleRepository.save(vehicle);
        }
        return null;
    }
    
    @Override
    @Transactional
    public Vehicle rejectVehicle(Long vehicleId, Long staffId) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setApprovalStatus("REJECTED");
            vehicle.setAvailabilityStatus("UNAVAILABLE");
            vehicle.setUpdatedDate(LocalDateTime.now());
            return vehicleRepository.save(vehicle);
        }
        return null;
    }
    
    // New methods for multi-step approval workflow
    @Override
    public List<Vehicle> getVehiclesByInsuranceApprovalStatus(String insuranceApprovalStatus) {
        return vehicleRepository.findByInsuranceApprovalStatus(insuranceApprovalStatus);
    }
    
    @Override
    public List<Vehicle> getVehiclesByStaffApprovalStatus(String staffApprovalStatus) {
        return vehicleRepository.findByStaffApprovalStatus(staffApprovalStatus);
    }
    
    @Override
    @Transactional
    public Vehicle approveVehicleInsurance(Long vehicleId, Long officerId, String notes) {
        System.out.println("Approving vehicle insurance - Vehicle ID: " + vehicleId + ", Officer ID: " + officerId);
        
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        // Use findByUserUserId instead of findById since officerId is actually userId
        Optional<InsuranceOfficer> optionalOfficer = insuranceOfficerRepository.findByUserUserId(officerId);
        
        if (optionalVehicle.isPresent() && optionalOfficer.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            InsuranceOfficer officer = optionalOfficer.get();
            
            System.out.println("Vehicle and officer found. Current insurance status: " + vehicle.getInsuranceApprovalStatus());
            
            // Update vehicle insurance approval status directly on the vehicle
            vehicle.setInsuranceApprovalStatus("APPROVED");
            vehicle.setInsuranceOfficer(officer);
            vehicle.setInsuranceApprovalDate(LocalDateTime.now());
            vehicle.setInsuranceOfficerNotes(notes);
            
            System.out.println("Set insurance approval status to APPROVED");
            
            // Update legacy approval status for backward compatibility
            // If staff has already approved, then fully approve the vehicle
            if ("APPROVED".equals(vehicle.getStaffApprovalStatus())) {
                vehicle.setApprovalStatus("APPROVED");
                vehicle.setAvailabilityStatus("AVAILABLE");
                System.out.println("Staff already approved, setting vehicle as fully approved and available");
            } else {
                // Staff approval is still pending
                vehicle.setApprovalStatus("PENDING");
                System.out.println("Staff approval still pending, keeping vehicle as PENDING");
            }
            
            vehicle.setUpdatedDate(LocalDateTime.now());
            
            // Also create a separate approval record
            org.web.autolanka.entity.InsuranceOfficerVehicleApproval approval = 
                new org.web.autolanka.entity.InsuranceOfficerVehicleApproval(vehicle, officer, "APPROVED");
            approval.setApprovalDate(LocalDateTime.now());
            approval.setNotes(notes);
            insuranceOfficerVehicleApprovalRepository.save(approval);
            
            System.out.println("Saving vehicle with updated insurance approval");
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            System.out.println("Vehicle saved. Insurance status: " + savedVehicle.getInsuranceApprovalStatus());
            
            return savedVehicle;
        }
        System.out.println("Vehicle or officer not found");
        return null;
    }
    
    @Override
    @Transactional
    public Vehicle rejectVehicleInsurance(Long vehicleId, Long officerId, String notes) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        // Use findByUserUserId instead of findById since officerId is actually userId
        Optional<InsuranceOfficer> optionalOfficer = insuranceOfficerRepository.findByUserUserId(officerId);
        
        if (optionalVehicle.isPresent() && optionalOfficer.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            InsuranceOfficer officer = optionalOfficer.get();
            
            // Update vehicle insurance approval status directly on the vehicle
            vehicle.setInsuranceApprovalStatus("REJECTED");
            vehicle.setInsuranceOfficer(officer);
            vehicle.setInsuranceApprovalDate(LocalDateTime.now());
            vehicle.setInsuranceOfficerNotes(notes);
            
            // Update legacy approval status for backward compatibility
            vehicle.setApprovalStatus("REJECTED"); // Fully rejected
            
            // Also reject staff approval since insurance is rejected
            vehicle.setStaffApprovalStatus("REJECTED");
            vehicle.setStaffApprovalDate(LocalDateTime.now());
            vehicle.setStaffMemberNotes("Automatically rejected due to insurance rejection");
            
            vehicle.setAvailabilityStatus("UNAVAILABLE");
            vehicle.setUpdatedDate(LocalDateTime.now());
            
            // Also create a separate approval record
            org.web.autolanka.entity.InsuranceOfficerVehicleApproval approval = 
                new org.web.autolanka.entity.InsuranceOfficerVehicleApproval(vehicle, officer, "REJECTED");
            approval.setApprovalDate(LocalDateTime.now());
            approval.setNotes(notes);
            insuranceOfficerVehicleApprovalRepository.save(approval);
            
            return vehicleRepository.save(vehicle);
        }
        return null;
    }
    
    @Override
    @Transactional
    public Vehicle approveVehicleByStaff(Long vehicleId, Long staffId, String notes) {
        System.out.println("Attempting to approve vehicle by staff - Vehicle ID: " + vehicleId + ", Staff ID: " + staffId);
        
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        // Use findByUserUserId instead of findById since staffId is actually userId
        Optional<StaffMember> optionalStaff = staffMemberRepository.findByUserUserId(staffId);
        
        System.out.println("Vehicle found: " + optionalVehicle.isPresent());
        System.out.println("Staff member found: " + optionalStaff.isPresent());
        
        if (optionalVehicle.isPresent() && optionalStaff.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            StaffMember staff = optionalStaff.get();
            
            // Debugging: Print current approval statuses
            System.out.println("Vehicle ID: " + vehicleId);
            System.out.println("Current insurance approval status: " + vehicle.getInsuranceApprovalStatus());
            System.out.println("Current staff approval status: " + vehicle.getStaffApprovalStatus());
            
            // Check if insurance is already approved (case insensitive)
            if (!"APPROVED".equalsIgnoreCase(vehicle.getInsuranceApprovalStatus())) {
                System.out.println("Insurance not approved. Current status: " + vehicle.getInsuranceApprovalStatus());
                return null; // Insurance must be approved first
            }
            
            // Update vehicle staff approval status
            vehicle.setStaffApprovalStatus("APPROVED");
            vehicle.setStaffMember(staff);
            vehicle.setStaffApprovalDate(LocalDateTime.now());
            vehicle.setStaffMemberNotes(notes);
            
            // Update legacy approval status for backward compatibility
            vehicle.setApprovalStatus("APPROVED");
            
            // Make vehicle available for customers
            vehicle.setAvailabilityStatus("AVAILABLE");
            vehicle.setUpdatedDate(LocalDateTime.now());
            
            System.out.println("About to save vehicle with staff approval");
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            System.out.println("Vehicle saved successfully with staff approval");
            return savedVehicle;
        } else {
            if (!optionalVehicle.isPresent()) {
                System.out.println("Vehicle not found with ID: " + vehicleId);
            }
            if (!optionalStaff.isPresent()) {
                System.out.println("Staff member not found with User ID: " + staffId);
                // Let's check what's in the database
                try {
                    List<StaffMember> allStaff = staffMemberRepository.findAll();
                    System.out.println("All staff members in database:");
                    for (StaffMember sm : allStaff) {
                        System.out.println("  Staff ID: " + sm.getStaffId() + ", User ID: " + sm.getUser().getUserId());
                    }
                } catch (Exception e) {
                    System.out.println("Error retrieving staff members: " + e.getMessage());
                }
            }
            return null;
        }
    }
    
    @Override
    @Transactional
    public Vehicle rejectVehicleByStaff(Long vehicleId, Long staffId, String notes) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        // Use findByUserUserId instead of findById since staffId is actually userId
        Optional<StaffMember> optionalStaff = staffMemberRepository.findByUserUserId(staffId);
        
        if (optionalVehicle.isPresent() && optionalStaff.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            StaffMember staff = optionalStaff.get();
            
            // Update vehicle staff approval status
            vehicle.setStaffApprovalStatus("REJECTED");
            vehicle.setStaffMember(staff);
            vehicle.setStaffApprovalDate(LocalDateTime.now());
            vehicle.setStaffMemberNotes(notes);
            
            // Update legacy approval status for backward compatibility
            vehicle.setApprovalStatus("REJECTED");
            
            vehicle.setAvailabilityStatus("UNAVAILABLE");
            vehicle.setUpdatedDate(LocalDateTime.now());
            
            return vehicleRepository.save(vehicle);
        }
        return null;
    }
    
    @Override
    public boolean isVehicleFullyApproved(Long vehicleId) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            return "APPROVED".equals(vehicle.getInsuranceApprovalStatus()) && 
                   "APPROVED".equals(vehicle.getStaffApprovalStatus());
        }
        return false;
    }
    
    // New method to get fully approved vehicles for customer search
    public List<Vehicle> getFullyApprovedVehicles() {
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        return allVehicles.stream()
                .filter(v -> "APPROVED".equals(v.getInsuranceApprovalStatus()) && 
                           "APPROVED".equals(v.getStaffApprovalStatus()))
                .collect(Collectors.toList());
    }
}