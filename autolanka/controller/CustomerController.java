// CustomerController.java
package org.web.autolanka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web.autolanka.entity.Customer;
import org.web.autolanka.entity.Payment;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.service.BookingService;
import org.web.autolanka.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String customerDashboard(HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            java.util.Optional<Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Get real booking statistics
                List<org.web.autolanka.entity.Booking> allBookings = bookingService.getBookingsByCustomerId(customerId);
                long totalBookings = allBookings.size();
                
                long upcomingBookings = allBookings.stream()
                    .filter(booking -> "CONFIRMED".equals(booking.getBookingStatus()))
                    .count();
                
                long completedBookings = allBookings.stream()
                    .filter(booking -> "COMPLETED".equals(booking.getBookingStatus()))
                    .count();
                
                model.addAttribute("welcomeMessage", "Welcome to your dashboard!");
                model.addAttribute("totalBookings", totalBookings);
                model.addAttribute("upcomingBookings", upcomingBookings);
                model.addAttribute("completedBookings", completedBookings);
            } else {
                // Fallback to default values if customer not found
                model.addAttribute("welcomeMessage", "Welcome to your dashboard!");
                model.addAttribute("totalBookings", 0);
                model.addAttribute("upcomingBookings", 0);
                model.addAttribute("completedBookings", 0);
            }
        } catch (Exception e) {
            // Fallback to default values if error occurs
            model.addAttribute("welcomeMessage", "Welcome to your dashboard!");
            model.addAttribute("totalBookings", 0);
            model.addAttribute("upcomingBookings", 0);
            model.addAttribute("completedBookings", 0);
        }
        
        return "customer-dashboard";
    }
    
    @GetMapping("/bookings/new")
    public String newBooking(HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        // Add booking data
        return "customer-new-booking";
    }
    
    // Removed conflicting mapping - using BookingController instead
    // @GetMapping("/bookings/history")
    // public String bookingHistory(HttpSession session, Model model) {
    //     // Check if user is logged in as customer
    //     String userType = (String) session.getAttribute("userType");
    //     if (!"customer".equals(userType)) {
    //         return "redirect:/customer-login";
    //     }
    //     
    //     // Add history data
    //     return "customer-booking-history";
    // }
    
    @GetMapping("/payments")
    public String payments(HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            java.util.Optional<Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Get all payments for this customer
                List<Payment> allPayments = paymentService.getPaymentsByCustomerId(customerId);
                
                // Separate pending and completed payments
                List<Payment> pendingPayments = allPayments.stream()
                    .filter(payment -> "PENDING".equals(payment.getPaymentStatus()) || "PENDING".equals(payment.getApprovalStatus()))
                    .toList();
                
                List<Payment> completedPayments = allPayments.stream()
                    .filter(payment -> !"PENDING".equals(payment.getPaymentStatus()) && !"PENDING".equals(payment.getApprovalStatus()))
                    .toList();
                
                model.addAttribute("pendingPayments", pendingPayments);
                model.addAttribute("completedPayments", completedPayments);
                model.addAttribute("totalPayments", allPayments.size());
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving payment information: " + e.getMessage());
        }
        
        return "customer-payments";
    }
    
    @GetMapping("/payments/pending")
    public String pendingPayments(HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            java.util.Optional<Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Get pending payments for this customer
                List<Payment> allPayments = paymentService.getPaymentsByCustomerId(customerId);
                List<Payment> pendingPayments = allPayments.stream()
                    .filter(payment -> "PENDING".equals(payment.getPaymentStatus()) || "PENDING".equals(payment.getApprovalStatus()))
                    .toList();
                
                model.addAttribute("pendingPayments", pendingPayments);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving pending payments: " + e.getMessage());
        }
        
        return "customer-pending-payments";
    }
    
    @GetMapping("/payments/pending/{id}")
    public String pendingPaymentDetails(@PathVariable Long id, HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            java.util.Optional<Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Get payment by ID
                java.util.Optional<Payment> paymentOpt = paymentService.getPaymentById(id);
                
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    
                    // Verify that the payment belongs to this customer
                    if (payment.getBooking().getCustomer().getCustomerId().equals(customerId)) {
                        model.addAttribute("payment", payment);
                    } else {
                        model.addAttribute("errorMessage", "You are not authorized to view this payment.");
                    }
                } else {
                    model.addAttribute("errorMessage", "Payment not found.");
                }
            } else {
                model.addAttribute("errorMessage", "Customer information not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving payment details: " + e.getMessage());
        }
        
        return "customer-payment-details";
    }
    
    @PostMapping("/payments/delete/{id}")
    public String deletePayment(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            java.util.Optional<Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Verify that the payment belongs to this customer
                java.util.Optional<Payment> paymentOpt = paymentService.getPaymentById(id);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    // Check if the payment belongs to this customer
                    if (payment.getBooking().getCustomer().getCustomerId().equals(customerId)) {
                        // Delete the payment
                        paymentService.deletePayment(id);
                        redirectAttributes.addFlashAttribute("successMessage", "Payment deleted successfully!");
                    } else {
                        redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this payment.");
                    }
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Payment not found.");
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Customer information not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting payment: " + e.getMessage());
        }
        
        return "redirect:/customer/payments";
    }
    
    @GetMapping("/feedback")
    public String feedback(HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        // Add feedback data
        return "customer-feedback";
    }
    
    // Removed conflicting mapping - using FeedbackController instead
    // @GetMapping("/feedback/history")
    // public String feedbackHistory(HttpSession session, Model model) {
    //     // Check if user is logged in as customer
    //     String userType = (String) session.getAttribute("userType");
    //     if (!"customer".equals(userType)) {
    //         return "redirect:/customer-login";
    //     }
    //     
    //     // Add feedback history data
    //     return "customer-feedback-history";
    // }
}