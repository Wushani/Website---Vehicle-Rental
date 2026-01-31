package org.web.autolanka.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.autolanka.entity.Vehicle;
import org.web.autolanka.repository.VehicleRepository;
import org.web.autolanka.service.VehicleService;

@Controller
@RequestMapping("/admin/database-fix")
public class DatabaseFixController {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/fix-vehicle-approvals")
    @ResponseBody
    public String fixVehicleApprovals() {
        StringBuilder result = new StringBuilder();
        result.append("Fixing vehicle approval statuses using direct SQL...\n");
        
        try (Connection connection = dataSource.getConnection()) {
            // First, let's see what we're working with
            String selectQuery = "SELECT vehicle_id, vehicle_number, insurance_approval_status, staff_approval_status, approval_status FROM vehicles";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            ResultSet rs = selectStmt.executeQuery();
            
            result.append("Current vehicle statuses:\n");
            while (rs.next()) {
                result.append("Vehicle ID: ").append(rs.getLong("vehicle_id"))
                      .append(", Number: ").append(rs.getString("vehicle_number"))
                      .append(", Insurance: ").append(rs.getString("insurance_approval_status"))
                      .append(", Staff: ").append(rs.getString("staff_approval_status"))
                      .append(", Overall: ").append(rs.getString("approval_status"))
                      .append("\n");
            }
            rs.close();
            selectStmt.close();
            
            // Fix vehicles with NULL approval statuses
            String updateQuery = "UPDATE vehicles SET insurance_approval_status = COALESCE(insurance_approval_status, approval_status, 'PENDING'), staff_approval_status = COALESCE(staff_approval_status, approval_status, 'PENDING') WHERE insurance_approval_status IS NULL OR staff_approval_status IS NULL";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            int rowsUpdated = updateStmt.executeUpdate();
            result.append("Updated ").append(rowsUpdated).append(" vehicles with NULL approval statuses.\n");
            updateStmt.close();
            
            // Fix vehicles where overall status is APPROVED but new fields are not
            String updateApprovedQuery = "UPDATE vehicles SET insurance_approval_status = 'APPROVED', staff_approval_status = 'APPROVED' WHERE approval_status = 'APPROVED' AND (insurance_approval_status != 'APPROVED' OR staff_approval_status != 'APPROVED')";
            PreparedStatement updateApprovedStmt = connection.prepareStatement(updateApprovedQuery);
            int approvedRowsUpdated = updateApprovedStmt.executeUpdate();
            result.append("Updated ").append(approvedRowsUpdated).append(" vehicles to align APPROVED status.\n");
            updateApprovedStmt.close();
            
            // Fix vehicles where overall status is REJECTED but new fields are not
            String updateRejectedQuery = "UPDATE vehicles SET insurance_approval_status = 'REJECTED', staff_approval_status = 'REJECTED' WHERE approval_status = 'REJECTED' AND (insurance_approval_status != 'REJECTED' OR staff_approval_status != 'REJECTED')";
            PreparedStatement updateRejectedStmt = connection.prepareStatement(updateRejectedQuery);
            int rejectedRowsUpdated = updateRejectedStmt.executeUpdate();
            result.append("Updated ").append(rejectedRowsUpdated).append(" vehicles to align REJECTED status.\n");
            updateRejectedStmt.close();
            
            // Show final state
            result.append("\nFinal vehicle statuses:\n");
            selectStmt = connection.prepareStatement(selectQuery);
            rs = selectStmt.executeQuery();
            
            while (rs.next()) {
                result.append("Vehicle ID: ").append(rs.getLong("vehicle_id"))
                      .append(", Number: ").append(rs.getString("vehicle_number"))
                      .append(", Insurance: ").append(rs.getString("insurance_approval_status"))
                      .append(", Staff: ").append(rs.getString("staff_approval_status"))
                      .append(", Overall: ").append(rs.getString("approval_status"))
                      .append("\n");
            }
            rs.close();
            selectStmt.close();
            
        } catch (SQLException e) {
            result.append("Error fixing database: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        result.append("Database fix completed successfully!");
        return result.toString().replace("\n", "<br>");
    }
    
    @GetMapping("/test-staff-approval")
    @ResponseBody
    public String testStaffApproval() {
        StringBuilder result = new StringBuilder();
        result.append("Testing staff approval for vehicle ID 7...\n");
        
        try (Connection connection = dataSource.getConnection()) {
            // Check current status of vehicle 7
            String selectQuery = "SELECT vehicle_id, insurance_approval_status, staff_approval_status, approval_status FROM vehicles WHERE vehicle_id = 7";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next()) {
                result.append("Before staff approval:\n");
                result.append("Vehicle ID: ").append(rs.getLong("vehicle_id"))
                      .append(", Insurance: ").append(rs.getString("insurance_approval_status"))
                      .append(", Staff: ").append(rs.getString("staff_approval_status"))
                      .append(", Overall: ").append(rs.getString("approval_status"))
                      .append("\n");
                      
                // Check if insurance is approved (which it should be)
                String insuranceStatus = rs.getString("insurance_approval_status");
                if ("APPROVED".equals(insuranceStatus)) {
                    result.append("Insurance is approved. Proceeding with staff approval...\n");
                    
                    // Update staff approval status
                    String updateQuery = "UPDATE vehicles SET staff_approval_status = 'APPROVED', approval_status = 'APPROVED', availability_status = 'AVAILABLE' WHERE vehicle_id = 7";
                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                    int rowsUpdated = updateStmt.executeUpdate();
                    
                    if (rowsUpdated > 0) {
                        result.append("Staff approval successful!\n");
                    } else {
                        result.append("Staff approval failed - no rows updated.\n");
                    }
                    updateStmt.close();
                } else {
                    result.append("Insurance is not approved. Staff approval cannot proceed.\n");
                }
            } else {
                result.append("Vehicle ID 7 not found.\n");
            }
            rs.close();
            selectStmt.close();
            
            // Show final status
            selectStmt = connection.prepareStatement(selectQuery);
            rs = selectStmt.executeQuery();
            
            if (rs.next()) {
                result.append("After staff approval:\n");
                result.append("Vehicle ID: ").append(rs.getLong("vehicle_id"))
                      .append(", Insurance: ").append(rs.getString("insurance_approval_status"))
                      .append(", Staff: ").append(rs.getString("staff_approval_status"))
                      .append(", Overall: ").append(rs.getString("approval_status"))
                      .append("\n");
            }
            rs.close();
            selectStmt.close();
            
        } catch (SQLException e) {
            result.append("Error testing staff approval: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString().replace("\n", "<br>");
    }
    
    @GetMapping("/fix-staff-members")
    @ResponseBody
    public String fixStaffMembers() {
        StringBuilder result = new StringBuilder();
        result.append("Fixing missing staff member entries...\n");
        
        try (Connection connection = dataSource.getConnection()) {
            // Find staff users without staff_member entries
            String missingStaffQuery = "SELECT u.user_id, u.username, u.first_name, u.last_name FROM users u WHERE u.user_type = 'STAFF' AND NOT EXISTS (SELECT 1 FROM staff_members sm WHERE sm.user_id = u.user_id)";
            PreparedStatement missingStaffStmt = connection.prepareStatement(missingStaffQuery);
            ResultSet missingStaffRs = missingStaffStmt.executeQuery();
            
            int staffCreated = 0;
            while (missingStaffRs.next()) {
                long userId = missingStaffRs.getLong("user_id");
                String username = missingStaffRs.getString("username");
                String firstName = missingStaffRs.getString("first_name");
                String lastName = missingStaffRs.getString("last_name");
                
                result.append("Creating staff member entry for User ID: ").append(userId)
                      .append(" (").append(firstName).append(" ").append(lastName).append(")\n");
                
                // Create staff member entry
                String insertStaffQuery = "INSERT INTO staff_members (user_id, staff_code, department, position, hire_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStaffStmt = connection.prepareStatement(insertStaffQuery);
                insertStaffStmt.setLong(1, userId);
                insertStaffStmt.setString(2, "STAFF-" + userId); // Generate staff code
                insertStaffStmt.setString(3, "General"); // Default department
                insertStaffStmt.setString(4, "Staff Member"); // Default position
                insertStaffStmt.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now())); // Hire date
                insertStaffStmt.executeUpdate();
                insertStaffStmt.close();
                
                staffCreated++;
            }
            missingStaffRs.close();
            missingStaffStmt.close();
            
            result.append("Created ").append(staffCreated).append(" staff member entries.\n");
            
            // Show current staff members
            result.append("\nCurrent staff members:\n");
            String staffQuery = "SELECT staff_id, user_id, staff_code, department, position FROM staff_members";
            PreparedStatement staffStmt = connection.prepareStatement(staffQuery);
            ResultSet staffRs = staffStmt.executeQuery();
            
            while (staffRs.next()) {
                result.append("Staff ID: ").append(staffRs.getLong("staff_id"))
                      .append(", User ID: ").append(staffRs.getLong("user_id"))
                      .append(", Staff Code: ").append(staffRs.getString("staff_code"))
                      .append(", Department: ").append(staffRs.getString("department"))
                      .append(", Position: ").append(staffRs.getString("position"))
                      .append("\n");
            }
            staffRs.close();
            staffStmt.close();
            
        } catch (SQLException e) {
            result.append("Error fixing staff members: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        result.append("Staff member fix completed successfully!");
        return result.toString().replace("\n", "<br>");
    }
    
    @GetMapping("/test-full-staff-approval")
    @ResponseBody
    public String testFullStaffApproval() {
        StringBuilder result = new StringBuilder();
        result.append("Testing full staff approval process for Vehicle ID 8...\n");
        
        try {
            // Check current status
            result.append("Checking current status of Vehicle ID 8...\n");
            java.util.Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(8L);
            if (!vehicleOpt.isPresent()) {
                result.append("Vehicle ID 8 not found!\n");
                return result.toString().replace("\n", "<br>");
            }
            
            Vehicle vehicle = vehicleOpt.get();
            result.append("Vehicle found - Insurance Status: ").append(vehicle.getInsuranceApprovalStatus())
                  .append(", Staff Status: ").append(vehicle.getStaffApprovalStatus()).append("\n");
            
            // Try to approve with Staff User ID 52
            result.append("Attempting to approve vehicle with Staff User ID 52...\n");
            Vehicle approvedVehicle = vehicleService.approveVehicleByStaff(8L, 52L, "Approved by test");
            
            if (approvedVehicle != null) {
                result.append("SUCCESS: Vehicle approved successfully!\n");
                result.append("New Status - Insurance: ").append(approvedVehicle.getInsuranceApprovalStatus())
                      .append(", Staff: ").append(approvedVehicle.getStaffApprovalStatus())
                      .append(", Overall: ").append(approvedVehicle.getApprovalStatus()).append("\n");
            } else {
                result.append("FAILED: Vehicle approval failed!\n");
                
                // Debug information
                result.append("Debug information:\n");
                result.append("- Vehicle ID 8 insurance status: ").append(vehicle.getInsuranceApprovalStatus()).append("\n");
                result.append("- Is insurance approved? ").append("APPROVED".equalsIgnoreCase(vehicle.getInsuranceApprovalStatus())).append("\n");
                
                // Check if staff member exists
                try (Connection connection = dataSource.getConnection()) {
                    String staffQuery = "SELECT staff_id, user_id FROM staff_members WHERE user_id = 52";
                    PreparedStatement staffStmt = connection.prepareStatement(staffQuery);
                    ResultSet staffRs = staffStmt.executeQuery();
                    if (staffRs.next()) {
                        result.append("- Staff member found with Staff ID: ").append(staffRs.getLong("staff_id")).append("\n");
                    } else {
                        result.append("- Staff member NOT found for User ID 52\n");
                    }
                    staffRs.close();
                    staffStmt.close();
                }
            }
        } catch (Exception e) {
            result.append("Error during test: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return result.toString().replace("\n", "<br>");
    }
}