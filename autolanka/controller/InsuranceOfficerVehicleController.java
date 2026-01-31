package org.web.autolanka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web.autolanka.entity.Vehicle;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.service.VehicleService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/insurance-officer/vehicles")
public class InsuranceOfficerVehicleController {

    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private AuthService authService;

    @GetMapping("/pending-approvals")
    public String viewPendingVehicles(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get pending vehicles for insurance approval
            List<Vehicle> pendingVehicles = vehicleService.getVehiclesByInsuranceApprovalStatus("PENDING");
            model.addAttribute("vehicles", pendingVehicles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving pending vehicles: " + e.getMessage());
        }
        
        return "insurance-officer-vehicle-approvals";
    }
    
    @GetMapping("/view/{id}")
    public String viewVehicleDetails(@PathVariable Long id, HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get vehicle details
            vehicleService.getVehicleById(id).ifPresent(vehicle -> {
                model.addAttribute("vehicle", vehicle);
            });
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving vehicle details: " + e.getMessage());
            return "redirect:/insurance-officer/vehicles/pending-approvals";
        }
        
        return "insurance-officer-vehicle-details";
    }
    
    @PostMapping("/approve/{id}")
    public String approveVehicle(@PathVariable Long id, 
                               @RequestParam(required = false) String notes,
                               HttpSession session, 
                               RedirectAttributes redirectAttributes) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get insurance officer ID from session
            Long officerId = (Long) session.getAttribute("userId");
            
            if (officerId == null) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Session error. Please log in again.");
                return "redirect:/insurance-officer-login";
            }
            
            System.out.println("Insurance officer approval request - Vehicle ID: " + id + ", Officer ID: " + officerId);
            
            // Approve vehicle insurance
            Vehicle approvedVehicle = vehicleService.approveVehicleInsurance(id, officerId, notes);
            if (approvedVehicle != null) {
                System.out.println("Vehicle insurance approved successfully");
                System.out.println("Vehicle insurance status after approval: " + approvedVehicle.getInsuranceApprovalStatus());
                System.out.println("Vehicle staff approval status after insurance approval: " + approvedVehicle.getStaffApprovalStatus());
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Vehicle insurance approved successfully! The vehicle will now be reviewed by staff members.");
                // Redirect to approved vehicles page so officer can see the approved vehicle
                return "redirect:/insurance-officer/vehicles/approved";
            } else {
                System.out.println("Failed to approve vehicle insurance");
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Failed to approve vehicle insurance. Vehicle or officer not found.");
            }
        } catch (Exception e) {
            System.out.println("Error in insurance approval: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error approving vehicle insurance: " + e.getMessage());
        }
        
        return "redirect:/insurance-officer/vehicles/pending-approvals";
    }
    
    @PostMapping("/reject/{id}")
    public String rejectVehicle(@PathVariable Long id, 
                               @RequestParam(required = false) String notes,
                               HttpSession session, 
                               RedirectAttributes redirectAttributes) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get insurance officer ID from session
            Long officerId = (Long) session.getAttribute("userId");
            
            if (officerId == null) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Session error. Please log in again.");
                return "redirect:/insurance-officer-login";
            }
            
            // Reject vehicle insurance
            Vehicle rejectedVehicle = vehicleService.rejectVehicleInsurance(id, officerId, notes);
            if (rejectedVehicle != null) {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Vehicle insurance rejected successfully! The vehicle owner will be notified.");
                // Redirect to rejected vehicles page so officer can see the rejected vehicle
                return "redirect:/insurance-officer/vehicles/rejected";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Failed to reject vehicle insurance. Vehicle or officer not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error rejecting vehicle insurance: " + e.getMessage());
        }
        
        return "redirect:/insurance-officer/vehicles/pending-approvals";
    }
    
    @GetMapping("/approved")
    public String viewApprovedVehicles(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get approved vehicles
            List<Vehicle> approvedVehicles = vehicleService.getVehiclesByInsuranceApprovalStatus("APPROVED");
            model.addAttribute("vehicles", approvedVehicles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving approved vehicles: " + e.getMessage());
        }
        
        return "insurance-officer-vehicle-approved";
    }
    
    @GetMapping("/rejected")
    public String viewRejectedVehicles(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get rejected vehicles
            List<Vehicle> rejectedVehicles = vehicleService.getVehiclesByInsuranceApprovalStatus("REJECTED");
            model.addAttribute("vehicles", rejectedVehicles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving rejected vehicles: " + e.getMessage());
        }
        
        return "insurance-officer-vehicle-rejected";
    }
}