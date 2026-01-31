// StaffController.java
package org.web.autolanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @GetMapping("/dashboard")
    public String staffDashboard(HttpSession session, Model model) {
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
        
        // Add dashboard data
        model.addAttribute("totalCustomers", 1200);
        model.addAttribute("activeBookings", 85);
        model.addAttribute("pendingRequests", 12);
        model.addAttribute("todayCheckIns", 8);
        
        return "staff-dashboard";
    }

    @GetMapping("/customers")
    public String manageCustomers(HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Add customer management data
        return "staff-customers";
    }

    @GetMapping("/bookings")
    public String manageBookings(HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Add booking management data
        return "staff-bookings";
    }

    @GetMapping("/vehicles")
    public String manageVehicles(HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Add vehicle management data
        return "staff-vehicles";
    }
    
    @GetMapping("/vehicle-approvals")
    public String vehicleApprovals(HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/staff-login";
        }
        
        // Redirect to pending approvals page
        return "redirect:/staff/vehicles/pending-approvals";
    }
}