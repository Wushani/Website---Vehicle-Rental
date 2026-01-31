// InsuranceOfficerController.java
package org.web.autolanka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.web.autolanka.service.InsuranceService;
import org.web.autolanka.service.VehicleService;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/insurance_officer")
public class InsuranceOfficerController {
    
    @Autowired
    private InsuranceService insuranceService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping("/dashboard")
    public String insuranceOfficerDashboard(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get statistics for the dashboard
            List<org.web.autolanka.entity.InsurancePolicy> activePolicies = insuranceService.getPoliciesByStatus("ACTIVE");
            List<org.web.autolanka.entity.InsuranceClaim> pendingClaims = insuranceService.getClaimsByStatus("PENDING");
            List<org.web.autolanka.entity.InsuranceClaim> approvedClaims = insuranceService.getClaimsByStatus("APPROVED");
            
            model.addAttribute("activePoliciesCount", activePolicies.size());
            model.addAttribute("pendingClaimsCount", pendingClaims.size());
            model.addAttribute("approvedClaimsCount", approvedClaims.size());
            
            // Calculate total claims paid (simplified)
            BigDecimal totalClaimsPaid = BigDecimal.ZERO;
            for (org.web.autolanka.entity.InsuranceClaim claim : approvedClaims) {
                if (claim.getApprovedAmount() != null) {
                    totalClaimsPaid = totalClaimsPaid.add(claim.getApprovedAmount());
                }
            }
            model.addAttribute("totalClaimsPaid", totalClaimsPaid);
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving dashboard data: " + e.getMessage());
        }
        
        return "insurance-dashboard";
    }
    
    @GetMapping("/claims")
    public String viewClaims(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            List<org.web.autolanka.entity.InsuranceClaim> claims = insuranceService.getAllClaims();
            model.addAttribute("claims", claims);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving claims: " + e.getMessage());
        }
        
        return "insurance-claims";
    }
    
    @GetMapping("/policies")
    public String viewPolicies(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            List<org.web.autolanka.entity.InsurancePolicy> policies = insuranceService.getAllPolicies();
            model.addAttribute("policies", policies);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving policies: " + e.getMessage());
        }
        
        return "insurance-policies";
    }
    
    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get statistics for the dashboard
            List<org.web.autolanka.entity.InsurancePolicy> activePolicies = insuranceService.getPoliciesByStatus("ACTIVE");
            List<org.web.autolanka.entity.InsuranceClaim> pendingClaims = insuranceService.getClaimsByStatus("PENDING");
            List<org.web.autolanka.entity.InsuranceClaim> approvedClaims = insuranceService.getClaimsByStatus("APPROVED");
            
            model.addAttribute("activePoliciesCount", activePolicies.size());
            model.addAttribute("pendingClaimsCount", pendingClaims.size());
            model.addAttribute("approvedClaimsCount", approvedClaims.size());
            
            // Calculate total claims paid (simplified)
            BigDecimal totalClaimsPaid = BigDecimal.ZERO;
            for (org.web.autolanka.entity.InsuranceClaim claim : approvedClaims) {
                if (claim.getApprovedAmount() != null) {
                    totalClaimsPaid = totalClaimsPaid.add(claim.getApprovedAmount());
                }
            }
            model.addAttribute("totalClaimsPaid", totalClaimsPaid);
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving reports data: " + e.getMessage());
        }
        
        return "insurance-reports";
    }
    
    @GetMapping("/support")
    public String insuranceSupport(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        // For now, just show the support page
        return "insurance-support";
    }
    
    @GetMapping("/vehicle-insurance")
    public String viewVehicleInsurance(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            List<org.web.autolanka.entity.Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving vehicles: " + e.getMessage());
        }
        
        return "insurance-vehicle-coverage";
    }
}