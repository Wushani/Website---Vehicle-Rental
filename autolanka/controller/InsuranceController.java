package org.web.autolanka.controller;

import org.web.autolanka.entity.InsurancePolicy;
import org.web.autolanka.entity.InsuranceClaim;
import org.web.autolanka.service.InsuranceService;
import org.web.autolanka.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {
    
    @Autowired
    private InsuranceService insuranceService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping("/policies")
    public String viewPolicies(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            List<InsurancePolicy> policies = insuranceService.getAllPolicies();
            model.addAttribute("policies", policies);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving policies: " + e.getMessage());
        }
        
        return "insurance-policies";
    }
    
    @GetMapping("/policies/create")
    public String showCreatePolicyForm(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        model.addAttribute("policy", new InsurancePolicy());
        return "insurance-create-policy";
    }
    
    @PostMapping("/policies/create")
    public String createPolicy(@ModelAttribute InsurancePolicy policy,
                              @RequestParam Long vehicleId,
                              HttpSession session,
                              Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            Optional<org.web.autolanka.entity.Vehicle> vehicleOpt = vehicleService.getVehicleById(vehicleId);
            if (vehicleOpt.isPresent()) {
                policy.setVehicle(vehicleOpt.get());
                InsurancePolicy savedPolicy = insuranceService.savePolicy(policy);
                model.addAttribute("successMessage", "Insurance policy created successfully! Policy ID: " + savedPolicy.getPolicyId());
                model.addAttribute("policy", savedPolicy);
                return "insurance-policy-details";
            } else {
                model.addAttribute("errorMessage", "Vehicle not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating policy: " + e.getMessage());
        }
        
        return "insurance-create-policy";
    }
    
    @GetMapping("/claims")
    public String viewClaims(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            List<InsuranceClaim> claims = insuranceService.getAllClaims();
            model.addAttribute("claims", claims);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving claims: " + e.getMessage());
        }
        
        return "insurance-claims";
    }
    
    @GetMapping("/claims/create")
    public String showCreateClaimForm(@RequestParam(required = false) Long policyId,
                                     HttpSession session,
                                     Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        model.addAttribute("claim", new InsuranceClaim());
        if (policyId != null) {
            Optional<InsurancePolicy> policyOpt = insuranceService.getPolicyById(policyId);
            if (policyOpt.isPresent()) {
                model.addAttribute("policy", policyOpt.get());
            }
        }
        return "insurance-create-claim";
    }
    
    @PostMapping("/claims/create")
    public String createClaim(@ModelAttribute InsuranceClaim claim,
                             @RequestParam Long policyId,
                             @RequestParam Long vehicleId,
                             HttpSession session,
                             Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            Optional<InsurancePolicy> policyOpt = insuranceService.getPolicyById(policyId);
            Optional<org.web.autolanka.entity.Vehicle> vehicleOpt = vehicleService.getVehicleById(vehicleId);
            
            if (policyOpt.isPresent() && vehicleOpt.isPresent()) {
                claim.setPolicy(policyOpt.get());
                claim.setVehicle(vehicleOpt.get());
                InsuranceClaim savedClaim = insuranceService.saveClaim(claim);
                model.addAttribute("successMessage", "Insurance claim created successfully! Claim ID: " + savedClaim.getClaimId());
                model.addAttribute("claim", savedClaim);
                return "insurance-claim-details";
            } else {
                model.addAttribute("errorMessage", "Policy or vehicle not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating claim: " + e.getMessage());
        }
        
        return "insurance-create-claim";
    }
    
    @PostMapping("/claims/{id}/update-status")
    public String updateClaimStatus(@PathVariable Long id,
                                   @RequestParam String status,
                                   @RequestParam(required = false) BigDecimal approvedAmount,
                                   @RequestParam(required = false) String officerNotes,
                                   HttpSession session,
                                   Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            Optional<InsuranceClaim> claimOpt = insuranceService.getClaimById(id);
            if (claimOpt.isPresent()) {
                InsuranceClaim claim = claimOpt.get();
                claim.setClaimStatus(status);
                if (approvedAmount != null) {
                    claim.setApprovedAmount(approvedAmount);
                }
                if (officerNotes != null && !officerNotes.isEmpty()) {
                    claim.setOfficerNotes(officerNotes);
                }
                claim.setUpdatedDate(LocalDateTime.now());
                
                InsuranceClaim updatedClaim = insuranceService.updateClaim(id, claim);
                model.addAttribute("successMessage", "Claim status updated successfully!");
                model.addAttribute("claim", updatedClaim);
                return "insurance-claim-details";
            } else {
                model.addAttribute("errorMessage", "Claim not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating claim status: " + e.getMessage());
        }
        
        return "redirect:/insurance/claims";
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
    
    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        // Check if user is logged in as insurance officer
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/insurance-officer-login";
        }
        
        try {
            // Get statistics for the dashboard
            List<InsurancePolicy> activePolicies = insuranceService.getPoliciesByStatus("ACTIVE");
            List<InsuranceClaim> pendingClaims = insuranceService.getClaimsByStatus("PENDING");
            List<InsuranceClaim> approvedClaims = insuranceService.getClaimsByStatus("APPROVED");
            
            model.addAttribute("activePoliciesCount", activePolicies.size());
            model.addAttribute("pendingClaimsCount", pendingClaims.size());
            model.addAttribute("approvedClaimsCount", approvedClaims.size());
            
            // Calculate total claims paid (simplified)
            BigDecimal totalClaimsPaid = BigDecimal.ZERO;
            List<InsuranceClaim> paidClaims = insuranceService.getClaimsByStatus("PAID");
            for (InsuranceClaim claim : paidClaims) {
                if (claim.getApprovedAmount() != null) {
                    totalClaimsPaid = totalClaimsPaid.add(claim.getApprovedAmount());
                }
            }
            model.addAttribute("totalClaimsPaid", totalClaimsPaid);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving reports: " + e.getMessage());
        }
        
        return "insurance-reports";
    }
}