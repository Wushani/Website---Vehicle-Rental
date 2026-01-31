package org.web.autolanka.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.autolanka.entity.Booking;
import org.web.autolanka.entity.Payment;
import org.web.autolanka.repository.PaymentRepository;
import org.web.autolanka.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    @Override
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
    
    @Override
    public List<Payment> getPaymentsByBooking(Booking booking) {
        return paymentRepository.findByBooking(booking);
    }
    
    @Override
    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingBookingId(bookingId);
    }
    
    @Override
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByPaymentStatus(status);
    }
    
    @Override
    public List<Payment> getPaymentsByMethod(String method) {
        return paymentRepository.findByPaymentMethod(method);
    }
    
    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    @Override
    public Payment updatePayment(Long id, Payment paymentDetails) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setPaymentAmount(paymentDetails.getPaymentAmount());
            payment.setPaymentMethod(paymentDetails.getPaymentMethod());
            payment.setPaymentStatus(paymentDetails.getPaymentStatus());
            payment.setTransactionId(paymentDetails.getTransactionId());
            payment.setPaymentDate(paymentDetails.getPaymentDate());
            payment.setRefundDate(paymentDetails.getRefundDate());
            payment.setUpdatedDate(LocalDateTime.now());
            // Set new fields for bank slip functionality
            payment.setBankSlipPath(paymentDetails.getBankSlipPath());
            payment.setApprovalStatus(paymentDetails.getApprovalStatus());
            payment.setApprovalDate(paymentDetails.getApprovalDate());
            payment.setApprovedBy(paymentDetails.getApprovedBy());
            return paymentRepository.save(payment);
        }
        return null;
    }
    
    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
    
    @Override
    public Payment createPaymentForBooking(Booking booking, BigDecimal amount, String paymentMethod) {
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        
        // Set initial status based on payment method
        if ("BANK_TRANSFER_SLIP_UPLOAD".equals(paymentMethod)) {
            payment.setPaymentStatus("PENDING");
            payment.setApprovalStatus("PENDING");
        } else {
            payment.setPaymentStatus("COMPLETED");
            payment.setApprovalStatus("APPROVED");
        }
        
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
    
    // New method to approve a payment
    public Payment approvePayment(Long paymentId, String approvedBy) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setPaymentStatus("COMPLETED");
            payment.setApprovalStatus("APPROVED");
            payment.setApprovalDate(LocalDateTime.now());
            payment.setApprovedBy(approvedBy);
            payment.setUpdatedDate(LocalDateTime.now());
            return paymentRepository.save(payment);
        }
        return null;
    }
    
    // New method to reject a payment
    public Payment rejectPayment(Long paymentId, String rejectedBy) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setPaymentStatus("REJECTED");
            payment.setApprovalStatus("REJECTED");
            payment.setApprovalDate(LocalDateTime.now());
            payment.setApprovedBy(rejectedBy);
            payment.setUpdatedDate(LocalDateTime.now());
            return paymentRepository.save(payment);
        }
        return null;
    }
    
    // New method to get payments by customer ID
    @Override
    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByBookingCustomerCustomerId(customerId);
    }
}