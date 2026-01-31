package org.web.autolanka.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.autolanka.entity.Feedback;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.service.BookingService;
import org.web.autolanka.service.FeedbackService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer/feedback")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/submit")
    public String showFeedbackForm(HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                List<org.web.autolanka.entity.Booking> customerBookings = bookingService.getBookingsByCustomerId(customerId);
                model.addAttribute("bookings", customerBookings);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving customer bookings: " + e.getMessage());
        }
        
        model.addAttribute("feedback", new Feedback());
        return "customer-feedback";
    }
    
    @PostMapping("/submit")
    public String submitFeedback(@RequestParam(required = false) Long bookingId,
                                @RequestParam Integer overallRating,
                                @RequestParam(required = false) Integer vehicleConditionRating,
                                @RequestParam(required = false) Integer cleanlinessRating,
                                @RequestParam(required = false) Integer valueRating,
                                @RequestParam(required = false) String comments,
                                @RequestParam(required = false) String suggestions,
                                HttpSession session,
                                Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Feedback feedback = new Feedback();
                feedback.setCustomer(customerOpt.get());
                
                // Set booking if provided
                if (bookingId != null) {
                    Optional<org.web.autolanka.entity.Booking> bookingOpt = bookingService.getBookingById(bookingId);
                    if (bookingOpt.isPresent()) {
                        feedback.setBooking(bookingOpt.get());
                    }
                }
                
                feedback.setOverallRating(overallRating);
                feedback.setVehicleConditionRating(vehicleConditionRating);
                feedback.setCleanlinessRating(cleanlinessRating);
                feedback.setValueRating(valueRating);
                feedback.setComments(comments);
                feedback.setSuggestions(suggestions);
                
                Feedback savedFeedback = feedbackService.saveFeedback(feedback);
                model.addAttribute("successMessage", "Thank you for your feedback! Your input helps us improve our service.");
                
                // If this feedback is for a specific booking, redirect back to that booking confirmation
                if (bookingId != null) {
                    Optional<org.web.autolanka.entity.Booking> bookingOpt = bookingService.getBookingById(bookingId);
                    if (bookingOpt.isPresent()) {
                        model.addAttribute("booking", bookingOpt.get());
                        model.addAttribute("feedbackSuccess", true);
                        return "customer-booking-confirmation";
                    }
                }
                
                // Otherwise, redirect to dashboard
                return "redirect:/customer/dashboard";
            } else {
                model.addAttribute("errorMessage", "Customer information not found.");
                return "customer-feedback";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error submitting feedback: " + e.getMessage());
            return "customer-feedback";
        }
    }
    
    @GetMapping("/history")
    public String viewFeedbackHistory(HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                List<Feedback> feedbackList = feedbackService.getFeedbackByCustomerId(customerId);
                model.addAttribute("feedbackList", feedbackList);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving feedback history: " + e.getMessage());
        }
        
        return "customer-feedback-history";
    }
    
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteFeedback(@RequestParam Long feedbackId, HttpSession session) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return ResponseEntity.status(403).body("Unauthorized access");
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Verify that the feedback belongs to this customer before deleting
                Optional<Feedback> feedbackOpt = feedbackService.getFeedbackById(feedbackId);
                if (feedbackOpt.isPresent()) {
                    Feedback feedback = feedbackOpt.get();
                    if (feedback.getCustomer().getCustomerId().equals(customerId)) {
                        feedbackService.deleteFeedback(feedbackId);
                        return ResponseEntity.ok("Feedback deleted successfully");
                    } else {
                        return ResponseEntity.status(403).body("You are not authorized to delete this feedback");
                    }
                } else {
                    return ResponseEntity.status(404).body("Feedback not found");
                }
            } else {
                return ResponseEntity.status(401).body("Customer information not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting feedback: " + e.getMessage());
        }
    }
    
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateFeedback(@RequestParam Long feedbackId,
                                               @RequestParam Integer overallRating,
                                               @RequestParam(required = false) Integer vehicleConditionRating,
                                               @RequestParam(required = false) Integer cleanlinessRating,
                                               @RequestParam(required = false) Integer valueRating,
                                               @RequestParam(required = false) String comments,
                                               @RequestParam(required = false) String suggestions,
                                               HttpSession session) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return ResponseEntity.status(403).body("Unauthorized access");
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Verify that the feedback belongs to this customer before updating
                Optional<Feedback> feedbackOpt = feedbackService.getFeedbackById(feedbackId);
                if (feedbackOpt.isPresent()) {
                    Feedback feedback = feedbackOpt.get();
                    if (feedback.getCustomer().getCustomerId().equals(customerId)) {
                        // Update feedback details
                        feedback.setOverallRating(overallRating);
                        feedback.setVehicleConditionRating(vehicleConditionRating);
                        feedback.setCleanlinessRating(cleanlinessRating);
                        feedback.setValueRating(valueRating);
                        feedback.setComments(comments);
                        feedback.setSuggestions(suggestions);
                        
                        Feedback updatedFeedback = feedbackService.updateFeedback(feedbackId, feedback);
                        if (updatedFeedback != null) {
                            return ResponseEntity.ok("Feedback updated successfully");
                        } else {
                            return ResponseEntity.status(500).body("Error updating feedback");
                        }
                    } else {
                        return ResponseEntity.status(403).body("You are not authorized to update this feedback");
                    }
                } else {
                    return ResponseEntity.status(404).body("Feedback not found");
                }
            } else {
                return ResponseEntity.status(401).body("Customer information not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating feedback: " + e.getMessage());
        }
    }
    
    @GetMapping("/edit")
    @ResponseBody
    public ResponseEntity<Feedback> getFeedbackForEdit(@RequestParam Long feedbackId, HttpSession session) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return ResponseEntity.status(403).body(null);
        }
        
        try {
            // Get customer from session
            Long userId = (Long) session.getAttribute("userId");
            Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
            
            if (customerOpt.isPresent()) {
                Long customerId = customerOpt.get().getCustomerId();
                
                // Verify that the feedback belongs to this customer
                Optional<Feedback> feedbackOpt = feedbackService.getFeedbackById(feedbackId);
                if (feedbackOpt.isPresent()) {
                    Feedback feedback = feedbackOpt.get();
                    if (feedback.getCustomer().getCustomerId().equals(customerId)) {
                        return ResponseEntity.ok(feedback);
                    } else {
                        return ResponseEntity.status(403).body(null);
                    }
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}