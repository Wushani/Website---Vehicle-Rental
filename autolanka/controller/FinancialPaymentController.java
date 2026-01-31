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
import org.web.autolanka.entity.Payment;
import org.web.autolanka.service.BookingService;
import org.web.autolanka.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/financial/payments")
public class FinancialPaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private BookingService bookingService;
    
    @GetMapping("/pending")
    public String viewPendingPayments(HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            // Get all pending payments
            List<Payment> pendingPayments = paymentService.getPaymentsByStatus("PENDING");
            model.addAttribute("payments", pendingPayments);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving pending payments: " + e.getMessage());
        }
        
        return "financial-pending-payments";
    }
    
    @GetMapping("/pending/{id}")
    public String viewPendingPaymentDetails(@PathVariable Long id, HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            // Get payment by ID
            java.util.Optional<Payment> paymentOpt = paymentService.getPaymentById(id);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                model.addAttribute("payment", payment);
                return "financial-payment-details";
            } else {
                model.addAttribute("errorMessage", "Payment not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving payment details: " + e.getMessage());
        }
        
        return "redirect:/financial/payments/pending";
    }
    
    @PostMapping("/approve/{id}")
    public String approvePayment(@PathVariable Long id, 
                               @RequestParam(required = false) String remarks,
                               HttpSession session, 
                               RedirectAttributes redirectAttributes) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            // Get financial executive name from session
            String executiveName = (String) session.getAttribute("username");
            if (executiveName == null || executiveName.isEmpty()) {
                executiveName = "Financial Executive";
            }
            
            // Approve payment
            Payment approvedPayment = paymentService.approvePayment(id, executiveName);
            if (approvedPayment != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Payment approved successfully!");
                
                // Update the associated booking status to COMPLETED through the booking service
                org.web.autolanka.entity.Booking booking = approvedPayment.getBooking();
                booking.setBookingStatus("COMPLETED");
                bookingService.updateBooking(booking.getBookingId(), booking);
                
                return "redirect:/financial/payments/pending";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Payment not found.");
                return "redirect:/financial/payments/pending";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving payment: " + e.getMessage());
            return "redirect:/financial/payments/pending";
        }
    }
    
    @PostMapping("/reject/{id}")
    public String rejectPayment(@PathVariable Long id, 
                              @RequestParam(required = false) String remarks,
                              HttpSession session, 
                              RedirectAttributes redirectAttributes) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            // Get financial executive name from session
            String executiveName = (String) session.getAttribute("username");
            if (executiveName == null || executiveName.isEmpty()) {
                executiveName = "Financial Executive";
            }
            
            // Reject payment
            Payment rejectedPayment = paymentService.rejectPayment(id, executiveName);
            if (rejectedPayment != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Payment rejected successfully!");
                
                // Update the associated booking status to PAYMENT_REJECTED through the booking service
                org.web.autolanka.entity.Booking booking = rejectedPayment.getBooking();
                booking.setBookingStatus("PAYMENT_REJECTED");
                bookingService.updateBooking(booking.getBookingId(), booking);
                
                return "redirect:/financial/payments/pending";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Payment not found.");
                return "redirect:/financial/payments/pending";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting payment: " + e.getMessage());
            return "redirect:/financial/payments/pending";
        }
    }
}