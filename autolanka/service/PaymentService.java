package org.web.autolanka.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.web.autolanka.entity.Booking;
import org.web.autolanka.entity.Payment;

public interface PaymentService {
    Payment savePayment(Payment payment);
    Optional<Payment> getPaymentById(Long id);
    List<Payment> getPaymentsByBooking(Booking booking);
    List<Payment> getPaymentsByBookingId(Long bookingId);
    List<Payment> getPaymentsByStatus(String status);
    List<Payment> getPaymentsByMethod(String method);
    List<Payment> getAllPayments();
    Payment updatePayment(Long id, Payment paymentDetails);
    void deletePayment(Long id);
    Payment createPaymentForBooking(Booking booking, BigDecimal amount, String paymentMethod);
    
    // New methods for payment approval
    Payment approvePayment(Long paymentId, String approvedBy);
    Payment rejectPayment(Long paymentId, String rejectedBy);
    
    // New method to get payments by customer ID
    List<Payment> getPaymentsByCustomerId(Long customerId);
}