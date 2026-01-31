package org.web.autolanka.service.impl;

import org.web.autolanka.entity.Payment;
import org.web.autolanka.entity.FinancialReport;
import org.web.autolanka.entity.Booking;
import org.web.autolanka.repository.PaymentRepository;
import org.web.autolanka.repository.FinancialReportRepository;
import org.web.autolanka.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceServiceImpl implements FinanceService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private FinancialReportRepository reportRepository;
    
    // Payment methods
    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    @Override
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
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
    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
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
            return paymentRepository.save(payment);
        }
        return null;
    }
    
    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
    
    @Override
    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return Optional.ofNullable(paymentRepository.findByTransactionId(transactionId));
    }
    
    @Override
    public Payment processPayment(Booking booking, BigDecimal amount, String paymentMethod) {
        Payment payment = new Payment(booking, amount, paymentMethod);
        payment.setPaymentStatus("COMPLETED");
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment refundPayment(Long paymentId, String reason) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setPaymentStatus("REFUNDED");
            payment.setRefundDate(LocalDateTime.now());
            payment.setUpdatedDate(LocalDateTime.now());
            return paymentRepository.save(payment);
        }
        return null;
    }
    
    // Financial Report methods
    @Override
    public FinancialReport saveReport(FinancialReport report) {
        return reportRepository.save(report);
    }
    
    @Override
    public Optional<FinancialReport> getReportById(Long id) {
        return reportRepository.findById(id);
    }
    
    @Override
    public List<FinancialReport> getReportsByType(String reportType) {
        return reportRepository.findByReportType(reportType);
    }
    
    @Override
    public List<FinancialReport> getReportsByGeneratedBy(String generatedBy) {
        return reportRepository.findByGeneratedBy(generatedBy);
    }
    
    @Override
    public List<FinancialReport> getAllReports() {
        return reportRepository.findAll();
    }
    
    @Override
    public FinancialReport updateReport(Long id, FinancialReport reportDetails) {
        Optional<FinancialReport> reportOpt = reportRepository.findById(id);
        if (reportOpt.isPresent()) {
            FinancialReport report = reportOpt.get();
            report.setReportName(reportDetails.getReportName());
            report.setReportType(reportDetails.getReportType());
            report.setReportPeriodStart(reportDetails.getReportPeriodStart());
            report.setReportPeriodEnd(reportDetails.getReportPeriodEnd());
            report.setTotalRevenue(reportDetails.getTotalRevenue());
            report.setTotalExpenses(reportDetails.getTotalExpenses());
            report.setNetProfit(reportDetails.getNetProfit());
            report.setGeneratedBy(reportDetails.getGeneratedBy());
            report.setReportData(reportDetails.getReportData());
            report.setGeneratedDate(reportDetails.getGeneratedDate());
            report.setUpdatedDate(LocalDateTime.now());
            return reportRepository.save(report);
        }
        return null;
    }
    
    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
    
    @Override
    public Optional<FinancialReport> getReportByName(String reportName) {
        return Optional.ofNullable(reportRepository.findByReportName(reportName));
    }
    
    @Override
    public FinancialReport generateRevenueReport(LocalDateTime startDate, LocalDateTime endDate, String generatedBy) {
        // In a real implementation, this would calculate actual revenue
        // For now, we'll create a placeholder report
        FinancialReport report = new FinancialReport("Revenue Report " + startDate.toLocalDate() + " to " + endDate.toLocalDate(), 
                                                   "REVENUE", startDate, endDate, generatedBy);
        report.setTotalRevenue(new BigDecimal("50000.00")); // Placeholder value
        report.setReportData("{\"totalBookings\": 250, \"averageBookingValue\": 200.00}");
        report.setGeneratedDate(LocalDateTime.now());
        return reportRepository.save(report);
    }
    
    @Override
    public FinancialReport generateProfitLossReport(LocalDateTime startDate, LocalDateTime endDate, String generatedBy) {
        // In a real implementation, this would calculate actual profit/loss
        // For now, we'll create a placeholder report
        FinancialReport report = new FinancialReport("Profit & Loss Report " + startDate.toLocalDate() + " to " + endDate.toLocalDate(), 
                                                   "PROFIT_LOSS", startDate, endDate, generatedBy);
        report.setTotalRevenue(new BigDecimal("50000.00")); // Placeholder value
        report.setTotalExpenses(new BigDecimal("30000.00")); // Placeholder value
        report.setNetProfit(new BigDecimal("20000.00")); // Placeholder value
        report.setReportData("{\"operatingExpenses\": 25000.00, \"otherExpenses\": 5000.00}");
        report.setGeneratedDate(LocalDateTime.now());
        return reportRepository.save(report);
    }
}