// ForgotPasswordController.java
package org.web.autolanka.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web.autolanka.dto.ForgotPasswordRequestDTO;
import org.web.autolanka.enums.UserType;
import org.web.autolanka.service.AuthService;

import jakarta.validation.Valid;

@Controller
public class ForgotPasswordController {
    
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);
    
    @Autowired
    private AuthService authService;
    
    // Show forgot password form for customer
    @GetMapping("/customer-forgot-password")
    public String showCustomerForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequestDTO());
        model.addAttribute("userType", "customer");
        return "customer-forgot-password";
    }
    
    // Show forgot password form for vehicle owner
    @GetMapping("/vehicle-owner-forgot-password")
    public String showVehicleOwnerForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequestDTO());
        model.addAttribute("userType", "vehicle_owner");
        return "vehicle-owner-forgot-password";
    }
    
    // Show forgot password form for staff
    @GetMapping("/staff-forgot-password")
    public String showStaffForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequestDTO());
        model.addAttribute("userType", "staff");
        return "staff-forgot-password";
    }
    
    // Show forgot password form for admin
    @GetMapping("/admin-forgot-password")
    public String showAdminForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequestDTO());
        model.addAttribute("userType", "admin");
        return "admin-forgot-password";
    }
    
    // Show forgot password form for insurance officer
    @GetMapping("/insurance-officer-forgot-password")
    public String showInsuranceOfficerForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequestDTO());
        model.addAttribute("userType", "insurance_officer");
        return "insurance-officer-forgot-password";
    }
    
    // Show forgot password form for financial executive
    @GetMapping("/financial-executive-forgot-password")
    public String showFinancialExecutiveForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequestDTO());
        model.addAttribute("userType", "financial_executive");
        return "financial-executive-forgot-password";
    }
    
    // Process forgot password request for customer
    @PostMapping("/customer-forgot-password")
    public String processCustomerForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequestDTO forgotPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        return processForgotPassword(forgotPasswordRequest, bindingResult, redirectAttributes, model, UserType.CUSTOMER, "customer");
    }
    
    // Process forgot password request for vehicle owner
    @PostMapping("/vehicle-owner-forgot-password")
    public String processVehicleOwnerForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequestDTO forgotPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        return processForgotPassword(forgotPasswordRequest, bindingResult, redirectAttributes, model, UserType.VEHICLE_OWNER, "vehicle-owner");
    }
    
    // Process forgot password request for staff
    @PostMapping("/staff-forgot-password")
    public String processStaffForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequestDTO forgotPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        return processForgotPassword(forgotPasswordRequest, bindingResult, redirectAttributes, model, UserType.STAFF, "staff");
    }
    
    // Process forgot password request for admin
    @PostMapping("/admin-forgot-password")
    public String processAdminForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequestDTO forgotPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        return processForgotPassword(forgotPasswordRequest, bindingResult, redirectAttributes, model, UserType.ADMIN, "admin");
    }
    
    // Process forgot password request for insurance officer
    @PostMapping("/insurance-officer-forgot-password")
    public String processInsuranceOfficerForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequestDTO forgotPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        return processForgotPassword(forgotPasswordRequest, bindingResult, redirectAttributes, model, UserType.INSURANCE_OFFICER, "insurance-officer");
    }
    
    // Process forgot password request for financial executive
    @PostMapping("/financial-executive-forgot-password")
    public String processFinancialExecutiveForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequestDTO forgotPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        return processForgotPassword(forgotPasswordRequest, bindingResult, redirectAttributes, model, UserType.FINANCIAL_EXECUTIVE, "financial-executive");
    }
    
    // Common method to process forgot password requests
    private String processForgotPassword(
            ForgotPasswordRequestDTO forgotPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            UserType userType,
            String userTypePath) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("userType", userTypePath);
            return userTypePath + "-forgot-password";
        }
        
        try {
            boolean success = authService.forgotPassword(forgotPasswordRequest, userType);
            
            if (success) {
                redirectAttributes.addFlashAttribute("success", 
                    "If an account with that email exists, a password reset link has been sent to your email address.");
                return "redirect:/" + userTypePath + "-login";
            } else {
                // Always show success message to prevent email enumeration attacks
                redirectAttributes.addFlashAttribute("success", 
                    "If an account with that email exists, a password reset link has been sent to your email address.");
                return "redirect:/" + userTypePath + "-login";
            }
        } catch (Exception e) {
            logger.error("Error processing forgot password request for user type: {}", userType, e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while processing your request. Please try again.");
            return "redirect:/" + userTypePath + "-forgot-password";
        }
    }
    
    // Show reset password form with token
    @GetMapping("/reset-password")
    public String showResetPasswordForm(
            @RequestParam("token") String token,
            Model model) {
        
        boolean isValid = authService.validatePasswordResetToken(token);
        
        if (isValid) {
            model.addAttribute("token", token);
            return "reset-password";
        } else {
            model.addAttribute("error", "Invalid or expired password reset token.");
            return "reset-password-error";
        }
    }
    
    // Process reset password with token
    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }
        
        // Validate password strength (at least 6 characters)
        if (password.length() < 6) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Password must be at least 6 characters long.");
            return "reset-password";
        }
        
        try {
            boolean success = authService.resetPasswordWithToken(token, password);
            
            if (success) {
                redirectAttributes.addFlashAttribute("success", "Your password has been reset successfully. You can now login with your new password.");
                return "redirect:/user-selection";
            } else {
                model.addAttribute("error", "Invalid or expired password reset token.");
                return "reset-password-error";
            }
        } catch (Exception e) {
            logger.error("Error resetting password with token: {}", token, e);
            model.addAttribute("token", token);
            model.addAttribute("error", "An error occurred while resetting your password. Please try again.");
            return "reset-password";
        }
    }
}