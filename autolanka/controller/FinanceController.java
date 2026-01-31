package org.web.autolanka.controller;

import org.web.autolanka.entity.Payment;
import org.web.autolanka.entity.FinancialReport;
import org.web.autolanka.service.FinanceService;
import org.web.autolanka.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/financial")
public class FinanceController {
    
    @Autowired
    private FinanceService financeService;
    
    @Autowired
    private BookingService bookingService;
    
    @GetMapping("/payments")
    public String viewPayments(HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            List<Payment> payments = financeService.getAllPayments();
            model.addAttribute("payments", payments);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving payments: " + e.getMessage());
        }
        
        return "financial-payments";
    }
    
    @GetMapping("/payments/process")
    public String showProcessPaymentForm(@RequestParam(required = false) Long bookingId,
                                        HttpSession session,
                                        Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        model.addAttribute("payment", new Payment());
        if (bookingId != null) {
            Optional<org.web.autolanka.entity.Booking> bookingOpt = bookingService.getBookingById(bookingId);
            if (bookingOpt.isPresent()) {
                model.addAttribute("booking", bookingOpt.get());
            }
        }
        return "financial-process-payment";
    }
    
    @PostMapping("/payments/process")
    public String processPayment(@RequestParam Long bookingId,
                               @RequestParam BigDecimal amount,
                               @RequestParam String paymentMethod,
                               HttpSession session,
                               Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            Optional<org.web.autolanka.entity.Booking> bookingOpt = bookingService.getBookingById(bookingId);
            if (bookingOpt.isPresent()) {
                Payment payment = financeService.processPayment(bookingOpt.get(), amount, paymentMethod);
                model.addAttribute("successMessage", "Payment processed successfully! Transaction ID: " + payment.getTransactionId());
                model.addAttribute("payment", payment);
                return "financial-payment-details";
            } else {
                model.addAttribute("errorMessage", "Booking not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error processing payment: " + e.getMessage());
        }
        
        return "financial-process-payment";
    }
    
    @PostMapping("/payments/{id}/refund")
    public String refundPayment(@PathVariable Long id,
                               @RequestParam String reason,
                               HttpSession session,
                               Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            Payment refundedPayment = financeService.refundPayment(id, reason);
            if (refundedPayment != null) {
                model.addAttribute("successMessage", "Payment refunded successfully!");
                model.addAttribute("payment", refundedPayment);
                return "financial-payment-details";
            } else {
                model.addAttribute("errorMessage", "Payment not found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error refunding payment: " + e.getMessage());
        }
        
        return "redirect:/financial/payments";
    }
    
    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            List<FinancialReport> reports = financeService.getAllReports();
            model.addAttribute("reports", reports);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving reports: " + e.getMessage());
        }
        
        return "financial-reports";
    }
    
    @GetMapping("/reports/generate")
    public String showGenerateReportForm(HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        model.addAttribute("report", new FinancialReport());
        return "financial-generate-report";
    }
    
    @PostMapping("/reports/generate")
    public String generateReport(@RequestParam String reportType,
                                @RequestParam String startDate,
                                @RequestParam String endDate,
                                HttpSession session,
                                Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            // Parse dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");
            
            // Get user info for generatedBy field
            String generatedBy = "Financial Executive"; // In a real app, this would come from session
            
            FinancialReport report;
            if ("REVENUE".equals(reportType)) {
                report = financeService.generateRevenueReport(startDateTime, endDateTime, generatedBy);
            } else if ("PROFIT_LOSS".equals(reportType)) {
                report = financeService.generateProfitLossReport(startDateTime, endDateTime, generatedBy);
            } else {
                model.addAttribute("errorMessage", "Invalid report type.");
                return "financial-generate-report";
            }
            
            model.addAttribute("successMessage", "Report generated successfully!");
            model.addAttribute("report", report);
            return "financial-report-details";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error generating report: " + e.getMessage());
        }
        
        return "financial-generate-report";
    }
    
    @GetMapping("/invoices")
    public String viewInvoices(HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        // In a real implementation, this would retrieve invoices
        // For now, we'll just show the invoices page
        return "financial-invoices";
    }
    
    @GetMapping("/analysis")
    public String viewFinancialAnalysis(HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        try {
            // Get statistics for the dashboard
            List<Payment> completedPayments = financeService.getPaymentsByStatus("COMPLETED");
            List<Payment> pendingPayments = financeService.getPaymentsByStatus("PENDING");
            List<Payment> refundedPayments = financeService.getPaymentsByStatus("REFUNDED");
            
            BigDecimal totalRevenue = BigDecimal.ZERO;
            for (Payment payment : completedPayments) {
                if (payment.getPaymentAmount() != null) {
                    totalRevenue = totalRevenue.add(payment.getPaymentAmount());
                }
            }
            
            model.addAttribute("totalRevenue", totalRevenue);
            model.addAttribute("pendingPaymentsCount", pendingPayments.size());
            model.addAttribute("refundedPaymentsCount", refundedPayments.size());
            model.addAttribute("completedPaymentsCount", completedPayments.size());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving financial data: " + e.getMessage());
        }
        
        return "financial-analysis";
    }
}