package org.web.autolanka.service;

import org.web.autolanka.entity.Payment;
import org.web.autolanka.entity.FinancialReport;
import org.web.autolanka.entity.Booking;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FinanceService {
    // Payment methods
    Payment savePayment(Payment payment);
    Optional<Payment> getPaymentById(Long id);
    List<Payment> getPaymentsByBookingId(Long bookingId);
    List<Payment> getPaymentsByStatus(String status);
    List<Payment> getPaymentsByMethod(String method);
    List<Payment> getPaymentsByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    List<Payment> getAllPayments();
    Payment updatePayment(Long id, Payment paymentDetails);
    void deletePayment(Long id);
    Optional<Payment> getPaymentByTransactionId(String transactionId);
    Payment processPayment(Booking booking, BigDecimal amount, String paymentMethod);
    Payment refundPayment(Long paymentId, String reason);
    
    // Financial Report methods
    FinancialReport saveReport(FinancialReport report);
    Optional<FinancialReport> getReportById(Long id);
    List<FinancialReport> getReportsByType(String reportType);
    List<FinancialReport> getReportsByGeneratedBy(String generatedBy);
    List<FinancialReport> getAllReports();
    FinancialReport updateReport(Long id, FinancialReport reportDetails);
    void deleteReport(Long id);
    Optional<FinancialReport> getReportByName(String reportName);
    FinancialReport generateRevenueReport(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, String generatedBy);
    FinancialReport generateProfitLossReport(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, String generatedBy);
}