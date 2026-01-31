// VehicleOwnerController.java
package org.web.autolanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/vehicle_owner")
public class VehicleOwnerController {

    @GetMapping("/dashboard")
    public String vehicleOwnerDashboard(HttpSession session, Model model) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            return "redirect:/vehicle-owner-login";
        }
        
        // Add dashboard data
        model.addAttribute("totalVehicles", 12);
        model.addAttribute("activeBookings", 45);
        model.addAttribute("averageRating", 4.8);
        model.addAttribute("monthlyEarnings", 3250);
        
        return "vehicle_owner-dashboard";
    }
    
    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            return "redirect:/vehicle-owner-login";
        }
        
        // Add report data
        return "vehicle-owner-reports";
    }
    
    @GetMapping("/earnings")
    public String viewEarnings(HttpSession session, Model model) {
        // Check if user is logged in as vehicle owner
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            return "redirect:/vehicle-owner-login";
        }
        
        // Add earnings data
        return "vehicle-owner-earnings";
    }
}