// HomeController.java
package org.web.autolanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.web.autolanka.dto.SearchFormDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        // Check if user is logged in
        Object currentUser = session.getAttribute("currentUser");
        String userType = (String) session.getAttribute("userType");
        
        // Add homepage data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information if logged in
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("userType", userType);
        }
        
        // Add search form object to prevent template errors
        model.addAttribute("searchForm", new SearchFormDTO());
        
        // Serve homepage-simple.html directly for the root path
        return "homepage-simple";
    }

    @GetMapping("/home")
    public String homepage(Model model, HttpSession session) {
        // Check if user is logged in
        Object currentUser = session.getAttribute("currentUser");
        String userType = (String) session.getAttribute("userType");
        
        // Add homepage data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information if logged in
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("userType", userType);
        }
        
        // Add search form object to prevent template errors
        model.addAttribute("searchForm", new SearchFormDTO());
        
        return "homepage-simple";
    }
    
    // User-specific homepage methods
    @GetMapping("/home/customer")
    public String customerHomepage(Model model, HttpSession session) {
        // Check if user is logged in as customer
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/"; // Redirect to main homepage if not logged in
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/"; // Redirect if not the correct user type
        }
        
        // Add customer-specific data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information
        model.addAttribute("currentUser", currentUser);
        
        return "homepage-customer";
    }
    
    @GetMapping("/home/vehicle-owner")
    public String vehicleOwnerHomepage(Model model, HttpSession session) {
        // Check if user is logged in as vehicle owner
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/"; // Redirect to main homepage if not logged in
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"vehicle_owner".equals(userType)) {
            return "redirect:/"; // Redirect if not the correct user type
        }
        
        // Add vehicle owner-specific data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information
        model.addAttribute("currentUser", currentUser);
        
        return "homepage-vehicle-owner";
    }
    
    @GetMapping("/home/staff")
    public String staffHomepage(Model model, HttpSession session) {
        // Check if user is logged in as staff
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/"; // Redirect to main homepage if not logged in
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"staff".equals(userType)) {
            return "redirect:/"; // Redirect if not the correct user type
        }
        
        // Add staff-specific data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information
        model.addAttribute("currentUser", currentUser);
        
        return "homepage-staff";
    }
    
    @GetMapping("/home/admin")
    public String adminHomepage(Model model, HttpSession session) {
        // Check if user is logged in as admin
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/"; // Redirect to main homepage if not logged in
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/"; // Redirect if not the correct user type
        }
        
        // Add admin-specific data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information
        model.addAttribute("currentUser", currentUser);
        
        return "homepage-admin";
    }
    
    @GetMapping("/home/insurance-officer")
    public String insuranceOfficerHomepage(Model model, HttpSession session) {
        // Check if user is logged in as insurance officer
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/"; // Redirect to main homepage if not logged in
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"insurance_officer".equals(userType)) {
            return "redirect:/"; // Redirect if not the correct user type
        }
        
        // Add insurance officer-specific data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information
        model.addAttribute("currentUser", currentUser);
        
        return "homepage-insurance-officer";
    }
    
    @GetMapping("/home/financial-executive")
    public String financialExecutiveHomepage(Model model, HttpSession session) {
        // Check if user is logged in as financial executive
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/"; // Redirect to main homepage if not logged in
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/"; // Redirect if not the correct user type
        }
        
        // Add financial executive-specific data to model
        model.addAttribute("totalVehicles", 150);
        model.addAttribute("totalBookings", 2500);
        model.addAttribute("activeCustomers", 1200);
        
        // Add user information
        model.addAttribute("currentUser", currentUser);
        
        return "homepage-financial-executive";
    }

    @GetMapping("/user-selection")
    public String userSelection() {
        return "user-selection";
    }
    
    @GetMapping("/customer-portal")
    public String customerPortal() {
        return "customer-portal";
    }
}