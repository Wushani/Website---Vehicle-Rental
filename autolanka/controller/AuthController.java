// AuthController.java
package org.web.autolanka.controller;

import org.web.autolanka.dto.LoginRequestDTO;
import org.web.autolanka.dto.RegistrationRequestDTO;
import org.web.autolanka.dto.DeleteRequestDTO;
import org.web.autolanka.dto.PasswordResetRequestDTO;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // Individual Login Page Routes
    @GetMapping("/admin-login")
    public String adminLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "admin-login";
    }

    @GetMapping("/customer-login")
    public String customerLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "customer-login";
    }

    @GetMapping("/staff-login")
    public String staffLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "staff-login";
    }

    @GetMapping("/vehicle-owner-login")
    public String vehicleOwnerLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "vehicle-owner-login";
    }

    @GetMapping("/insurance-officer-login")
    public String insuranceOfficerLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "insurance-officer-login";
    }

    @GetMapping("/financial-executive-login")
    public String financialExecutiveLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "financial-executive-login";
    }

    // Individual Delete Page Routes
    @GetMapping("/admin-delete")
    public String adminDeletePage(Model model) {
        model.addAttribute("deleteRequest", new DeleteRequestDTO());
        return "admin-delete";
    }

    @GetMapping("/customer-delete")
    public String customerDeletePage(Model model) {
        model.addAttribute("deleteRequest", new DeleteRequestDTO());
        return "customer-delete";
    }

    @GetMapping("/staff-delete")
    public String staffDeletePage(Model model) {
        model.addAttribute("deleteRequest", new DeleteRequestDTO());
        return "staff-delete";
    }

    @GetMapping("/vehicle-owner-delete")
    public String vehicleOwnerDeletePage(Model model) {
        model.addAttribute("deleteRequest", new DeleteRequestDTO());
        return "vehicle-owner-delete";
    }

    @GetMapping("/insurance-officer-delete")
    public String insuranceOfficerDeletePage(Model model) {
        model.addAttribute("deleteRequest", new DeleteRequestDTO());
        return "insurance-officer-delete";
    }

    @GetMapping("/financial-executive-delete")
    public String financialExecutiveDeletePage(Model model) {
        model.addAttribute("deleteRequest", new DeleteRequestDTO());
        return "financial-executive-delete";
    }

    // Individual Password Reset Page Routes
    @GetMapping("/admin-reset-password")
    public String adminResetPasswordPage(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequestDTO());
        return "admin-reset-password";
    }

    @GetMapping("/customer-reset-password")
    public String customerResetPasswordPage(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequestDTO());
        return "customer-reset-password";
    }

    @GetMapping("/staff-reset-password")
    public String staffResetPasswordPage(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequestDTO());
        return "staff-reset-password";
    }

    @GetMapping("/vehicle-owner-reset-password")
    public String vehicleOwnerResetPasswordPage(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequestDTO());
        return "vehicle-owner-reset-password";
    }

    @GetMapping("/insurance-officer-reset-password")
    public String insuranceOfficerResetPasswordPage(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequestDTO());
        return "insurance-officer-reset-password";
    }

    @GetMapping("/financial-executive-reset-password")
    public String financialExecutiveResetPasswordPage(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequestDTO());
        return "financial-executive-reset-password";
    }

    // Individual Registration Page Routes (Only for Customer and Vehicle Owner)
    @GetMapping("/customer-register")
    public String customerRegisterPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequestDTO());
        return "customer-register";
    }

    @GetMapping("/vehicle-owner-register")
    public String vehicleOwnerRegisterPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequestDTO());
        return "vehicle-owner-register";
    }

    // Individual Admin Registration Page Routes (Admin Only)
    @GetMapping("/admin/register/staff")
    public String staffRegisterPage(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String sessionUserType = (String) session.getAttribute("userType");
        if (!"admin".equals(sessionUserType)) {
            return "redirect:/admin-login";
        }
        
        model.addAttribute("registrationRequest", new RegistrationRequestDTO());
        return "staff-register";
    }

    @GetMapping("/admin/register/insurance-officer")
    public String insuranceOfficerRegisterPage(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String sessionUserType = (String) session.getAttribute("userType");
        if (!"admin".equals(sessionUserType)) {
            return "redirect:/admin-login";
        }
        
        model.addAttribute("registrationRequest", new RegistrationRequestDTO());
        return "insurance-officer-register";
    }

    @GetMapping("/admin/register/financial-executive")
    public String financialExecutiveRegisterPage(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String sessionUserType = (String) session.getAttribute("userType");
        if (!"admin".equals(sessionUserType)) {
            return "redirect:/admin-login";
        }
        
        model.addAttribute("registrationRequest", new RegistrationRequestDTO());
        return "financial-executive-register";
    }

    @GetMapping("/admin/register/admin")
    public String adminRegisterIndividualPage(HttpSession session, Model model) {
        // Check if user is logged in as admin
        String sessionUserType = (String) session.getAttribute("userType");
        if (!"admin".equals(sessionUserType)) {
            return "redirect:/admin-login";
        }
        
        model.addAttribute("registrationRequest", new RegistrationRequestDTO());
        return "admin-register-individual";
    }

    // Legacy login route (for backward compatibility)
    @GetMapping("/login")
    public String loginPage(@RequestParam("type") String userType, Model model) {
        // Redirect to individual login pages
        switch (userType.toLowerCase()) {
            case "admin":
                return "redirect:/admin-login";
            case "customer":
                return "redirect:/customer-login";
            case "staff":
                return "redirect:/staff-login";
            case "vehicle_owner":
                return "redirect:/vehicle-owner-login";
            case "insurance_officer":
                return "redirect:/insurance-officer-login";
            case "financial_executive":
                return "redirect:/financial-executive-login";
            default:
                return "redirect:/user-selection";
        }
    }

    // Individual Login Processing Routes
    @PostMapping("/admin-login")
    public String processAdminLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                                    BindingResult bindingResult,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        return processLoginByType(loginRequest, "admin", bindingResult, session, redirectAttributes, model, "admin-login");
    }

    @PostMapping("/customer-login")
    public String processCustomerLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                                       BindingResult bindingResult,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        return processLoginByType(loginRequest, "customer", bindingResult, session, redirectAttributes, model, "customer-login");
    }

    @PostMapping("/staff-login")
    public String processStaffLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                                    BindingResult bindingResult,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        return processLoginByType(loginRequest, "staff", bindingResult, session, redirectAttributes, model, "staff-login");
    }

    @PostMapping("/vehicle-owner-login")
    public String processVehicleOwnerLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                                           BindingResult bindingResult,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        return processLoginByType(loginRequest, "vehicle_owner", bindingResult, session, redirectAttributes, model, "vehicle-owner-login");
    }

    @PostMapping("/insurance-officer-login")
    public String processInsuranceOfficerLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                                               BindingResult bindingResult,
                                               HttpSession session,
                                               RedirectAttributes redirectAttributes,
                                               Model model) {
        return processLoginByType(loginRequest, "insurance_officer", bindingResult, session, redirectAttributes, model, "insurance-officer-login");
    }

    @PostMapping("/financial-executive-login")
    public String processFinancialExecutiveLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                                                 BindingResult bindingResult,
                                                 HttpSession session,
                                                 RedirectAttributes redirectAttributes,
                                                 Model model) {
        return processLoginByType(loginRequest, "financial_executive", bindingResult, session, redirectAttributes, model, "financial-executive-login");
    }

    // Individual Delete Processing Routes
    @PostMapping("/admin-delete")
    public String processAdminDelete(@Valid @ModelAttribute("deleteRequest") DeleteRequestDTO deleteRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        return processDeleteByType(deleteRequest, "admin", bindingResult, redirectAttributes, model, "admin-delete");
    }

    @PostMapping("/customer-delete")
    public String processCustomerDelete(@Valid @ModelAttribute("deleteRequest") DeleteRequestDTO deleteRequest,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        return processDeleteByType(deleteRequest, "customer", bindingResult, redirectAttributes, model, "customer-delete");
    }

    @PostMapping("/staff-delete")
    public String processStaffDelete(@Valid @ModelAttribute("deleteRequest") DeleteRequestDTO deleteRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        return processDeleteByType(deleteRequest, "staff", bindingResult, redirectAttributes, model, "staff-delete");
    }

    @PostMapping("/vehicle-owner-delete")
    public String processVehicleOwnerDelete(@Valid @ModelAttribute("deleteRequest") DeleteRequestDTO deleteRequest,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        return processDeleteByType(deleteRequest, "vehicle_owner", bindingResult, redirectAttributes, model, "vehicle-owner-delete");
    }

    @PostMapping("/insurance-officer-delete")
    public String processInsuranceOfficerDelete(@Valid @ModelAttribute("deleteRequest") DeleteRequestDTO deleteRequest,
                                                BindingResult bindingResult,
                                                RedirectAttributes redirectAttributes,
                                                Model model) {
        return processDeleteByType(deleteRequest, "insurance_officer", bindingResult, redirectAttributes, model, "insurance-officer-delete");
    }

    @PostMapping("/financial-executive-delete")
    public String processFinancialExecutiveDelete(@Valid @ModelAttribute("deleteRequest") DeleteRequestDTO deleteRequest,
                                                  BindingResult bindingResult,
                                                  RedirectAttributes redirectAttributes,
                                                  Model model) {
        return processDeleteByType(deleteRequest, "financial_executive", bindingResult, redirectAttributes, model, "financial-executive-delete");
    }

    // Individual Password Reset Processing Routes
    @PostMapping("/admin-reset-password")
    public String processAdminResetPassword(@Valid @ModelAttribute("passwordResetRequest") PasswordResetRequestDTO passwordResetRequest,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        return processPasswordResetByType(passwordResetRequest, "admin", bindingResult, redirectAttributes, model, "admin-reset-password");
    }

    @PostMapping("/customer-reset-password")
    public String processCustomerResetPassword(@Valid @ModelAttribute("passwordResetRequest") PasswordResetRequestDTO passwordResetRequest,
                                               BindingResult bindingResult,
                                               RedirectAttributes redirectAttributes,
                                               Model model) {
        return processPasswordResetByType(passwordResetRequest, "customer", bindingResult, redirectAttributes, model, "customer-reset-password");
    }

    @PostMapping("/staff-reset-password")
    public String processStaffResetPassword(@Valid @ModelAttribute("passwordResetRequest") PasswordResetRequestDTO passwordResetRequest,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {
        return processPasswordResetByType(passwordResetRequest, "staff", bindingResult, redirectAttributes, model, "staff-reset-password");
    }

    @PostMapping("/vehicle-owner-reset-password")
    public String processVehicleOwnerResetPassword(@Valid @ModelAttribute("passwordResetRequest") PasswordResetRequestDTO passwordResetRequest,
                                                   BindingResult bindingResult,
                                                   RedirectAttributes redirectAttributes,
                                                   Model model) {
        return processPasswordResetByType(passwordResetRequest, "vehicle_owner", bindingResult, redirectAttributes, model, "vehicle-owner-reset-password");
    }

    @PostMapping("/insurance-officer-reset-password")
    public String processInsuranceOfficerResetPassword(@Valid @ModelAttribute("passwordResetRequest") PasswordResetRequestDTO passwordResetRequest,
                                                       BindingResult bindingResult,
                                                       RedirectAttributes redirectAttributes,
                                                       Model model) {
        return processPasswordResetByType(passwordResetRequest, "insurance_officer", bindingResult, redirectAttributes, model, "insurance-officer-reset-password");
    }

    @PostMapping("/financial-executive-reset-password")
    public String processFinancialExecutiveResetPassword(@Valid @ModelAttribute("passwordResetRequest") PasswordResetRequestDTO passwordResetRequest,
                                                         BindingResult bindingResult,
                                                         RedirectAttributes redirectAttributes,
                                                         Model model) {
        return processPasswordResetByType(passwordResetRequest, "financial_executive", bindingResult, redirectAttributes, model, "financial-executive-reset-password");
    }

    // Common login processing method
    private String processLoginByType(LoginRequestDTO loginRequest, String userType, 
                                     BindingResult bindingResult, HttpSession session, 
                                     RedirectAttributes redirectAttributes, Model model, String viewName) {
        if (bindingResult.hasErrors()) {
            return viewName;
        }

        try {
            var user = authService.authenticateUser(loginRequest, UserType.valueOf(userType.toUpperCase()));

            if (user != null) {
                session.setAttribute("currentUser", user);
                session.setAttribute("userType", userType);
                session.setAttribute("userId", user.getUserId());

                // Redirect based on user type
                switch (userType.toLowerCase()) {
                    case "admin":
                        return "redirect:/home/admin";
                    case "staff":
                        return "redirect:/home/staff";
                    case "insurance_officer":
                        return "redirect:/home/insurance-officer";
                    case "financial_executive":
                        return "redirect:/home/financial-executive";
                    case "customer":
                        return "redirect:/home/customer";
                    case "vehicle_owner":
                        return "redirect:/home/vehicle-owner";
                    default:
                        return "redirect:/home/" + userType.toLowerCase();
                }
            } else {
                model.addAttribute("error", "Invalid credentials. Please try again.");
                return viewName;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return viewName;
        }
    }

    // Common delete processing method
    private String processDeleteByType(DeleteRequestDTO deleteRequest, String userType,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                       Model model, String viewName) {
        // Check if confirmation is checked
        if (!deleteRequest.isConfirmation()) {
            model.addAttribute("error", "Please confirm that you want to delete your account.");
            return viewName;
        }

        if (bindingResult.hasErrors()) {
            return viewName;
        }

        try {
            boolean deleted = authService.deleteUser(deleteRequest.getIdentifier(), deleteRequest.getPassword(), UserType.valueOf(userType.toUpperCase()));

            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Account deleted successfully.");
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Invalid credentials or no account found with the provided email or username.");
                return viewName;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete account: " + e.getMessage());
            return viewName;
        }
    }

    // Common password reset processing method
    private String processPasswordResetByType(PasswordResetRequestDTO passwordResetRequest, String userType,
                                             BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                             Model model, String viewName) {
        if (bindingResult.hasErrors()) {
            return viewName;
        }

        // Validate that new password and current password are not the same
        if (passwordResetRequest.getCurrentPassword().equals(passwordResetRequest.getNewPassword())) {
            model.addAttribute("error", "New password must be different from current password.");
            return viewName;
        }

        try {
            boolean reset = authService.resetPassword(
                passwordResetRequest.getIdentifier(),
                passwordResetRequest.getCurrentPassword(),
                passwordResetRequest.getNewPassword(),
                UserType.valueOf(userType.toUpperCase())
            );

            if (reset) {
                redirectAttributes.addFlashAttribute("success", "Password reset successfully.");
                // Redirect to the login page for this user type
                return "redirect:/" + userType.toLowerCase().replace("_", "-") + "-login";
            } else {
                model.addAttribute("error", "Invalid credentials or no account found with the provided email or username.");
                return viewName;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to reset password: " + e.getMessage());
            return viewName;
        }
    }

    // Legacy login processing (for backward compatibility)
    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                               @RequestParam("type") String userType,
                               BindingResult bindingResult,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        
        // Redirect to appropriate individual login page with error
        String redirectUrl = "/" + userType.toLowerCase().replace("_", "-") + "-login";
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please check your input and try again.");
            return "redirect:" + redirectUrl;
        }

        try {
            var user = authService.authenticateUser(loginRequest, UserType.valueOf(userType.toUpperCase()));

            if (user != null) {
                session.setAttribute("currentUser", user);
                session.setAttribute("userType", userType);
                session.setAttribute("userId", user.getUserId());

                // Redirect based on user type
                if ("admin".equals(userType)) {
                    return "redirect:/home/admin";
                } else if ("staff".equals(userType)) {
                    return "redirect:/home/staff";
                } else if ("insurance_officer".equals(userType)) {
                    return "redirect:/home/insurance-officer";
                } else if ("financial_executive".equals(userType)) {
                    return "redirect:/home/financial-executive";
                } else if ("vehicle_owner".equals(userType)) {
                    return "redirect:/home/vehicle-owner";
                } else if ("customer".equals(userType)) {
                    return "redirect:/home/customer";
                } else {
                    return "redirect:/home/" + userType.toLowerCase();
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid credentials. Please try again.");
                return "redirect:" + redirectUrl;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:" + redirectUrl;
        }
    }

    // Individual Registration Processing Routes (Only for Customer and Vehicle Owner)
    @PostMapping("/customer-register")
    public String processCustomerRegistration(@Valid @ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest,
                                              BindingResult bindingResult,
                                              RedirectAttributes redirectAttributes,
                                              Model model) {
        return processRegistrationByType(registrationRequest, "customer", bindingResult, redirectAttributes, model, "customer-register", "/customer-login");
    }

    @PostMapping("/vehicle-owner-register")
    public String processVehicleOwnerRegistration(@Valid @ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest,
                                                  BindingResult bindingResult,
                                                  RedirectAttributes redirectAttributes,
                                                  Model model) {
        return processRegistrationByType(registrationRequest, "vehicle_owner", bindingResult, redirectAttributes, model, "vehicle-owner-register", "/vehicle-owner-login");
    }

    // Individual Admin Registration Processing Routes (Admin Only)
    @PostMapping("/admin/register/staff")
    public String processStaffRegistration(@Valid @ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest,
                                           BindingResult bindingResult,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        return processAdminRegistrationByType(registrationRequest, "staff", bindingResult, session, redirectAttributes, model, "staff-register");
    }

    @PostMapping("/admin/register/insurance-officer")
    public String processInsuranceOfficerRegistration(@Valid @ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest,
                                                       BindingResult bindingResult,
                                                       HttpSession session,
                                                       RedirectAttributes redirectAttributes,
                                                       Model model) {
        return processAdminRegistrationByType(registrationRequest, "insurance_officer", bindingResult, session, redirectAttributes, model, "insurance-officer-register");
    }

    @PostMapping("/admin/register/financial-executive")
    public String processFinancialExecutiveRegistration(@Valid @ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest,
                                                         BindingResult bindingResult,
                                                         HttpSession session,
                                                         RedirectAttributes redirectAttributes,
                                                         Model model) {
        return processAdminRegistrationByType(registrationRequest, "financial_executive", bindingResult, session, redirectAttributes, model, "financial-executive-register");
    }

    @PostMapping("/admin/register/admin")
    public String processAdminRegistrationIndividual(@Valid @ModelAttribute("registrationRequest") RegistrationRequestDTO registrationRequest,
                                                      BindingResult bindingResult,
                                                      HttpSession session,
                                                      RedirectAttributes redirectAttributes,
                                                      Model model) {
        return processAdminRegistrationByType(registrationRequest, "admin", bindingResult, session, redirectAttributes, model, "admin-register-individual");
    }

    // Common registration processing method
    private String processRegistrationByType(RegistrationRequestDTO registrationRequest, String userType,
                                            BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                            Model model, String viewName, String successRedirect) {
        // Custom validation for user-type-specific fields
        validateUserTypeSpecificFields(registrationRequest, userType, bindingResult);

        // Password confirmation validation
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "field.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return viewName;
        }

        try {
            String generatedUsername = authService.registerUser(registrationRequest, UserType.valueOf(userType.toUpperCase()));
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Your username is: " + generatedUsername + ". You can login using your username or email address.");
            
            // For vehicle owners, redirect directly to vehicle add page after registration
            if ("vehicle_owner".equals(userType)) {
                return "redirect:/vehicle-owner-login";
            }
            
            return "redirect:" + successRedirect;
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return viewName;
        }
    }

    // Common admin registration processing method
    private String processAdminRegistrationByType(RegistrationRequestDTO registrationRequest, String userType,
                                                 BindingResult bindingResult, HttpSession session,
                                                 RedirectAttributes redirectAttributes, Model model, String viewName) {
        // Check if user is logged in as admin
        String sessionUserType = (String) session.getAttribute("userType");
        if (!"admin".equals(sessionUserType)) {
            return "redirect:/admin-login";
        }

        // Custom validation for user-type-specific fields
        validateUserTypeSpecificFields(registrationRequest, userType, bindingResult);

        // Password confirmation validation
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "field.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return viewName;
        }

        try {
            String generatedUsername = authService.registerUser(registrationRequest, UserType.valueOf(userType.toUpperCase()));
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Generated username: " + generatedUsername);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return viewName;
        }
    }

    // Custom validation method for user-type-specific fields
    private void validateUserTypeSpecificFields(RegistrationRequestDTO request, String userType, BindingResult bindingResult) {
        // Username validation - only if provided
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            if (request.getUsername().trim().length() < 3 || request.getUsername().trim().length() > 50) {
                bindingResult.rejectValue("username", "field.invalid", "Username must be between 3 and 50 characters");
            }
        }
        
        switch (userType.toLowerCase()) {
            case "customer":
                if (request.getNic() == null || request.getNic().trim().isEmpty()) {
                    bindingResult.rejectValue("nic", "field.required", "NIC Number is required for customers");
                }
                break;
            case "vehicle_owner":
                if (request.getNic() == null || request.getNic().trim().isEmpty()) {
                    bindingResult.rejectValue("nic", "field.required", "NIC Number is required for vehicle owners");
                }
                break;
            case "staff":
                if (request.getSpecialCode() == null || request.getSpecialCode().trim().isEmpty()) {
                    bindingResult.rejectValue("specialCode", "field.required", "Staff ID is required");
                }
                if (request.getDepartment() == null || request.getDepartment().trim().isEmpty()) {
                    bindingResult.rejectValue("department", "field.required", "Department is required for staff");
                }
                if (request.getPosition() == null || request.getPosition().trim().isEmpty()) {
                    bindingResult.rejectValue("position", "field.required", "Position is required for staff");
                }
                break;
            case "insurance_officer":
                if (request.getSpecialCode() == null || request.getSpecialCode().trim().isEmpty()) {
                    bindingResult.rejectValue("specialCode", "field.required", "Officer Code is required");
                }
                if (request.getInsuranceCompany() == null || request.getInsuranceCompany().trim().isEmpty()) {
                    bindingResult.rejectValue("insuranceCompany", "field.required", "Insurance Company is required");
                }
                break;
            case "financial_executive":
                if (request.getSpecialCode() == null || request.getSpecialCode().trim().isEmpty()) {
                    bindingResult.rejectValue("specialCode", "field.required", "Employee Code is required");
                }
                if (request.getDepartment() == null || request.getDepartment().trim().isEmpty()) {
                    bindingResult.rejectValue("department", "field.required", "Department is required");
                }
                if (request.getFinancialQualification() == null || request.getFinancialQualification().trim().isEmpty()) {
                    bindingResult.rejectValue("financialQualification", "field.required", "Financial Qualification is required");
                }
                if (request.getExperienceYears() == null || request.getExperienceYears().trim().isEmpty()) {
                    bindingResult.rejectValue("experienceYears", "field.required", "Years of Experience is required");
                }
                break;
            case "admin":
                if (request.getSpecialCode() == null || request.getSpecialCode().trim().isEmpty()) {
                    bindingResult.rejectValue("specialCode", "field.required", "Admin Code is required");
                }
                if (request.getDepartment() == null || request.getDepartment().trim().isEmpty()) {
                    bindingResult.rejectValue("department", "field.required", "Department is required");
                }
                if (request.getAdminLevel() == null || request.getAdminLevel().trim().isEmpty()) {
                    bindingResult.rejectValue("adminLevel", "field.required", "Admin Level is required");
                }
                if (request.getAuthorizationLevel() == null || request.getAuthorizationLevel().trim().isEmpty()) {
                    bindingResult.rejectValue("authorizationLevel", "field.required", "Authorization Level is required");
                }
                break;
        }
    }
}