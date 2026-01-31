// FinancialController.java
package org.web.autolanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/financial_executive")
public class FinancialController {

    @GetMapping("/dashboard")
    public String financialDashboard(HttpSession session, Model model) {
        // Check if user is logged in as financial executive
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        // Add dashboard data
        model.addAttribute("monthlyRevenue", 485000);
        model.addAttribute("totalExpenses", 125000);
        model.addAttribute("netProfit", 360000);
        model.addAttribute("outstandingPayments", 45000);
        
        return "financial-dashboard";
    }

    @GetMapping("/revenue")
    public String revenueManagement(HttpSession session, Model model) {
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        // Add revenue management data
        return "financial-revenue";
    }

    @GetMapping("/expenses")
    public String expenseManagement(HttpSession session, Model model) {
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        // Add expense management data
        return "financial-expenses";
    }

    @GetMapping("/reports")
    public String financialReports(HttpSession session, Model model) {
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        // Add financial reports data
        return "financial-reports";
    }

    @GetMapping("/payroll")
    public String payrollManagement(HttpSession session, Model model) {
        String userType = (String) session.getAttribute("userType");
        if (!"financial_executive".equals(userType)) {
            return "redirect:/financial-executive-login";
        }
        
        // Add payroll management data
        return "financial-payroll";
    }
}