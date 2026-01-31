package org.web.autolanka.repository;

import org.web.autolanka.entity.Payment;
import org.web.autolanka.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBooking(Booking booking);
    List<Payment> findByBookingBookingId(Long bookingId);
    List<Payment> findByPaymentStatus(String paymentStatus);
    List<Payment> findByPaymentMethod(String paymentMethod);
    Payment findByTransactionId(String transactionId);
    List<Payment> findByPaymentDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    // New method to find payments by customer ID
    List<Payment> findByBookingCustomerCustomerId(Long customerId);
}