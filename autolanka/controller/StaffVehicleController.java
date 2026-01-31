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
@RequestMapping("/staff/vehicles")
public class StaffVehicleController {

    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private AuthService authService;

    @GetMapping("/pending-approvals")
    public String viewPendingVehicles(HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        // Check if user is logged in as staff
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Get pending vehicles for staff approval (insurance must be approved first)
        List<Vehicle> pendingVehicles = vehicleService.getVehiclesByInsuranceApprovalStatus("APPROVED");
        System.out.println("Found " + pendingVehicles.size() + " vehicles with approved insurance");
        
        pendingVehicles = pendingVehicles.stream()
            .filter(v -> "PENDING".equals(v.getStaffApprovalStatus()))
            .toList();
            
        System.out.println("After filtering by pending staff approval: " + pendingVehicles.size() + " vehicles");
        
        // Debug: Print details of each vehicle
        for (Vehicle v : pendingVehicles) {
            System.out.println("Vehicle ID: " + v.getVehicleId() + 
                             ", Insurance Status: " + v.getInsuranceApprovalStatus() + 
                             ", Staff Status: " + v.getStaffApprovalStatus());
        }
        
        model.addAttribute("vehicles", pendingVehicles);
        
        return "staff-vehicle-approvals";
    }
    
    @GetMapping("/view/{id}")
    public String viewVehicleDetails(@PathVariable Long id, HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        // Check if user is logged in as staff
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Get vehicle details
        vehicleService.getVehicleById(id).ifPresent(vehicle -> {
            model.addAttribute("vehicle", vehicle);
        });
        
        return "staff-vehicle-details";
    }
    
    @PostMapping("/approve/{id}")
    public String approveVehicle(@PathVariable Long id, 
                               @RequestParam(required = false) String notes,
                               HttpSession session, 
                               RedirectAttributes redirectAttributes) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        // Check if user is logged in as staff
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Get staff ID from session
        Long staffId = (Long) session.getAttribute("userId");
        
        System.out.println("Staff approval request - Vehicle ID: " + id + ", Staff ID: " + staffId);
        
        // Approve vehicle by staff (second step)
        Vehicle approvedVehicle = vehicleService.approveVehicleByStaff(id, staffId, notes);
        if (approvedVehicle != null) {
            System.out.println("Vehicle approved successfully by staff");
            redirectAttributes.addFlashAttribute("successMessage", 
                "Vehicle approved successfully by staff! It is now available for customers to book.");
        } else {
            System.out.println("Failed to approve vehicle by staff");
            // Check what went wrong
            java.util.Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
            if (!vehicleOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Vehicle not found. Please try again.");
            } else {
                Vehicle vehicle = vehicleOpt.get();
                if (!"APPROVED".equalsIgnoreCase(vehicle.getInsuranceApprovalStatus())) {
                    redirectAttributes.addFlashAttribute("errorMessage", 
                        "Insurance has not been approved for this vehicle. Please ensure insurance officer has approved it first.");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", 
                        "Failed to approve vehicle. Please ensure you are logged in as a valid staff member and try again.");
                }
            }
        }
        
        return "redirect:/staff/vehicles/pending-approvals";
    }
    
    @PostMapping("/reject/{id}")
    public String rejectVehicle(@PathVariable Long id, 
                               @RequestParam(required = false) String notes,
                               HttpSession session, 
                               RedirectAttributes redirectAttributes) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        // Check if user is logged in as staff
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Get staff ID from session
        Long staffId = (Long) session.getAttribute("userId");
        
        // Reject vehicle by staff
        Vehicle rejectedVehicle = vehicleService.rejectVehicleByStaff(id, staffId, notes);
        if (rejectedVehicle != null) {
            redirectAttributes.addFlashAttribute("successMessage", 
                "Vehicle rejected successfully by staff! The vehicle owner will be notified.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to reject vehicle. Please try again.");
        }
        
        return "redirect:/staff/vehicles/pending-approvals";
    }
    
    @GetMapping("/insurance-pending")
    public String viewInsurancePendingVehicles(HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        // Check if user is logged in as staff
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Get vehicles where insurance is still pending
        List<Vehicle> insurancePendingVehicles = vehicleService.getVehiclesByInsuranceApprovalStatus("PENDING");
        model.addAttribute("vehicles", insurancePendingVehicles);
        
        return "staff-vehicle-insurance-pending";
    }
}