package org.web.autolanka.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.web.autolanka.entity.Booking;
import org.web.autolanka.entity.Payment;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.service.BookingService;
import org.web.autolanka.service.PaymentService;
import org.web.autolanka.service.VehicleService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer/bookings")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private PaymentService paymentService;
    
    @GetMapping("/search")
    public String searchVehicles(@RequestParam(required = false) String pickupLocation,
                                @RequestParam(required = false) String pickupDate,
                                @RequestParam(required = false) String dropoffDate,
                                @RequestParam(required = false) String vehicleType,
                                Model model) {
        // Add search parameters to model
        model.addAttribute("pickupLocation", pickupLocation);
        model.addAttribute("pickupDate", pickupDate);
        model.addAttribute("dropoffDate", dropoffDate);
        model.addAttribute("vehicleType", vehicleType);
        
        // Get available and approved vehicles based on search criteria
        List<org.web.autolanka.entity.Vehicle> availableVehicles = 
            vehicleService.getVehiclesByAvailabilityAndApprovalStatus("AVAILABLE", "APPROVED");
        
        // Apply additional filters if provided
        if (vehicleType != null && !vehicleType.isEmpty()) {
            availableVehicles = availableVehicles.stream()
                .filter(v -> vehicleType.equals(v.getVehicleType()))
                .collect(java.util.stream.Collectors.toList());
        }
        
        model.addAttribute("vehicles", availableVehicles);
        return "customer-vehicle-search";
    }

    @GetMapping("/find-vehicle")
    public String findVehiclePage(Model model) {
        // Add empty search parameters to model
        model.addAttribute("pickupLocation", "");
        model.addAttribute("pickupDate", "");
        model.addAttribute("dropoffDate", "");
        model.addAttribute("vehicleType", "");
        
        // Get all available and approved vehicles
        List<org.web.autolanka.entity.Vehicle> availableVehicles = 
            vehicleService.getVehiclesByAvailabilityAndApprovalStatus("AVAILABLE", "APPROVED");
        
        model.addAttribute("vehicles", availableVehicles);
        return "customer-vehicle-search";
    }
    
    @GetMapping("/book/{id}")
    public String bookVehicle(@PathVariable Long id, Model model) {
        // Get vehicle by ID
        Optional<org.web.autolanka.entity.Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
        
        if (vehicleOpt.isPresent()) {
            model.addAttribute("vehicle", vehicleOpt.get());
            return "customer-vehicle-booking";
        } else {
            model.addAttribute("errorMessage", "Vehicle not found.");
            return "customer-vehicle-search";
        }
    }
    
    @PostMapping("/create")
    public String createBooking(@RequestParam Long vehicleId,
                               @RequestParam String pickupLocation,
                               @RequestParam String dropoffLocation,
                               @RequestParam String pickupDate,
                               @RequestParam String dropoffDate,
                               @RequestParam String specialRequests,
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
                // Get vehicle
                Optional<org.web.autolanka.entity.Vehicle> vehicleOpt = vehicleService.getVehicleById(vehicleId);
                
                if (vehicleOpt.isPresent()) {
                    org.web.autolanka.entity.Vehicle vehicle = vehicleOpt.get();
                    
                    // Parse dates
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDate, formatter);
                    LocalDateTime dropoffDateTime = LocalDateTime.parse(dropoffDate, formatter);
                    
                    // Calculate total amount based on actual rental price
                    long days = java.time.Duration.between(pickupDateTime, dropoffDateTime).toDays();
                    if (days == 0) days = 1; // Minimum one day
                    
                    BigDecimal dailyRate = vehicle.getRentalPricePerDay() != null ? 
                                          vehicle.getRentalPricePerDay() : new BigDecimal("100.00");
                    BigDecimal totalAmount = dailyRate.multiply(new BigDecimal(days));
                    
                    // Create booking
                    Booking booking = new Booking();
                    booking.setCustomer(customerOpt.get());
                    booking.setVehicle(vehicle);
                    booking.setPickupLocation(pickupLocation);
                    booking.setDropoffLocation(dropoffLocation);
                    booking.setPickupDate(pickupDateTime);
                    booking.setDropoffDate(dropoffDateTime);
                    booking.setTotalAmount(totalAmount);
                    booking.setSpecialRequests(specialRequests);
                    
                    // Save booking
                    Booking savedBooking = bookingService.saveBooking(booking);
                    
                    // Redirect to payment page instead of confirmation page
                    model.addAttribute("booking", savedBooking);
                    return "customer-payment-updated";
                } else {
                    model.addAttribute("errorMessage", "Selected vehicle not found.");
                }
            } else {
                model.addAttribute("errorMessage", "Customer information not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating booking: " + e.getMessage());
        }
        
        return "customer-vehicle-booking";
    }
    
    @PostMapping("/payment/complete")
    public String completePayment(@RequestParam Long bookingId,
                                 @RequestParam String paymentMethod,
                                 @RequestParam(required = false) String cardNumber,
                                 @RequestParam(required = false) String cardName,
                                 @RequestParam(required = false) String expiryDate,
                                 @RequestParam(required = false) String cvv,
                                 @RequestParam(required = false) MultipartFile bankSlip,
                                 HttpSession session,
                                 Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            // Get booking by ID
            Optional<Booking> bookingOpt = bookingService.getBookingById(bookingId);
            
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                
                // Check if booking belongs to current customer
                Long userId = (Long) session.getAttribute("userId");
                Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
                
                if (customerOpt.isPresent() && 
                    booking.getCustomer().getCustomerId().equals(customerOpt.get().getCustomerId())) {
                    
                    // Create payment record
                    Payment payment = paymentService.createPaymentForBooking(
                        booking, 
                        booking.getTotalAmount(), 
                        paymentMethod
                    );
                    
                    // Handle bank slip upload
                    if ("BANK_TRANSFER_SLIP_UPLOAD".equals(paymentMethod)) {
                        // For bank slip payments, set status to PENDING for approval
                        payment.setPaymentStatus("PENDING");
                        payment.setApprovalStatus("PENDING");
                        
                        // Handle file upload if provided
                        if (bankSlip != null && !bankSlip.isEmpty()) {
                            // In a real implementation, you would save the file and set the path
                            // For now, we'll just set a placeholder path
                            payment.setBankSlipPath("uploads/bank_slips/" + bookingId + "_" + System.currentTimeMillis() + ".pdf");
                        }
                        
                        payment = paymentService.updatePayment(payment.getPaymentId(), payment);
                        
                        // Add success message and show payment completion page
                        model.addAttribute("infoMessage", "Your payment has been submitted and is pending approval by our financial executive. You will receive a confirmation email once approved.");
                        model.addAttribute("payment", payment);
                        return "customer-payment-completion";
                    } else if ("CARD_PAYMENT".equals(paymentMethod)) {
                        // For card payments, set as completed immediately
                        payment.setPaymentStatus("COMPLETED");
                        payment.setApprovalStatus("APPROVED");
                        payment = paymentService.updatePayment(payment.getPaymentId(), payment);
                        
                        // Update booking status to COMPLETED
                        booking.setBookingStatus("COMPLETED");
                        booking = bookingService.updateBooking(bookingId, booking);
                        
                        model.addAttribute("successMessage", "Payment completed successfully! Your booking is now confirmed.");
                        model.addAttribute("payment", payment);
                        return "customer-payment-completion";
                    }
                } else {
                    model.addAttribute("errorMessage", "You are not authorized to complete payment for this booking.");
                }
            } else {
                model.addAttribute("errorMessage", "Booking not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error completing payment: " + e.getMessage());
        }
        
        return "customer-payment-updated";
    }
    
    @GetMapping("/history")
    public String viewBookingHistory(HttpSession session, Model model) {
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
                // Get only completed bookings for this customer
                List<Booking> bookings = bookingService.getBookingsByCustomerIdAndStatus(customerId, "COMPLETED");
                model.addAttribute("bookings", bookings);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving booking history: " + e.getMessage());
        }
        
        return "customer-booking-history";
    }
    
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, HttpSession session, Model model) {
        // Check if user is logged in as customer
        String userType = (String) session.getAttribute("userType");
        if (!"customer".equals(userType)) {
            return "redirect:/customer-login";
        }
        
        try {
            Optional<Booking> bookingOpt = bookingService.getBookingById(id);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                
                // Check if booking belongs to current customer
                Long userId = (Long) session.getAttribute("userId");
                Optional<org.web.autolanka.entity.Customer> customerOpt = authService.findCustomerByUserId(userId);
                
                if (customerOpt.isPresent() && 
                    booking.getCustomer().getCustomerId().equals(customerOpt.get().getCustomerId())) {
                    booking.setBookingStatus("CANCELLED");
                    bookingService.updateBooking(id, booking);
                    model.addAttribute("successMessage", "Booking cancelled successfully.");
                } else {
                    model.addAttribute("errorMessage", "You are not authorized to cancel this booking.");
                }
            } else {
                model.addAttribute("errorMessage", "Booking not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error cancelling booking: " + e.getMessage());
        }
        
        return "redirect:/customer/bookings/history";
    }
}