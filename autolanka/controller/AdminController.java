// AdminController.java
package org.web.autolanka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web.autolanka.entity.Booking;
import org.web.autolanka.entity.Payment;
import org.web.autolanka.entity.User;
import org.web.autolanka.repository.BookingRepository;
import org.web.autolanka.repository.PaymentRepository;
import org.web.autolanka.repository.UserRepository;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.service.BookingService;
import org.web.autolanka.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/admin-login";
        }
        
        // Add dashboard data
        model.addAttribute("totalStaff", 45);
        model.addAttribute("totalInsuranceOfficers", 12);
        model.addAttribute("totalFinancialExecutives", 8);
        model.addAttribute("totalAdmins", 3);
        
        return "admin-dashboard";
    }
    
    // View all users
    @GetMapping("/users")
    public String viewAllUsers(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/admin-login";
        }
        
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-users";
    }
    
    // View all bookings
    @GetMapping("/bookings")
    public String viewAllBookings(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/admin-login";
        }
        
        List<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "admin-bookings";
    }
    
    // View all payments
    @GetMapping("/payments")
    public String viewAllPayments(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/admin-login";
        }
        
        List<Payment> payments = paymentRepository.findAll();
        model.addAttribute("payments", payments);
        return "admin-payments";
    }
    
    // Delete user
    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") Long userId, 
                            HttpSession session, 
                            RedirectAttributes redirectAttributes) {
        // Check if user is logged in as admin
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            return "redirect:/admin-login";
        }
        
        try {
            userRepository.deleteById(userId);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
}