package org.web.autolanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
public class StaffPinController {
    
    // Secret PIN for staff access (in a real application, this would be stored securely)
    private static final String SECRET_PIN = "1234";
    
    @GetMapping("/staff-portal")
    public String showStaffPortal() {
        return "staff-portal";
    }
    
    @GetMapping("/staff-pin-auth")
    public String showPinAuthPage(HttpSession session, Model model) {
        // Always show the PIN authentication page, regardless of previous authentication
        // Remove any existing authentication from session to force re-authentication
        session.removeAttribute("pinAuthenticated");
        
        return "staff-pin-auth";
    }
    
    @PostMapping("/staff-pin-auth")
    public String authenticatePin(@RequestParam String pin1,
                                @RequestParam String pin2,
                                @RequestParam String pin3,
                                @RequestParam String pin4,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        // Combine the PIN digits
        String enteredPin = pin1 + pin2 + pin3 + pin4;
        
        // Check if the entered PIN matches the secret PIN
        if (SECRET_PIN.equals(enteredPin)) {
            // Set PIN authenticated flag in session
            session.setAttribute("pinAuthenticated", true);
            return "redirect:/staff-user-selection";
        } else {
            // Authentication failed
            redirectAttributes.addFlashAttribute("error", "Invalid PIN. Please try again.");
            return "redirect:/staff-pin-auth";
        }
    }
    
    @GetMapping("/staff-user-selection")
    public String showStaffUserSelection(HttpSession session, Model model) {
        // Check if user has authenticated with PIN
        Boolean pinAuthenticated = (Boolean) session.getAttribute("pinAuthenticated");
        if (pinAuthenticated == null || !pinAuthenticated) {
            return "redirect:/staff-pin-auth";
        }
        
        return "staff-user-selection";
    }
    
    @GetMapping("/staff-logout")
    public String staffLogout(HttpSession session) {
        // Clear PIN authentication from session
        session.removeAttribute("pinAuthenticated");
        return "redirect:/staff-pin-auth";
    }
}