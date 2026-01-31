// EmailService.java
package org.web.autolanka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendPasswordResetEmail(String to, String token) {
        try {
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("AutoLanka Password Reset Request");
            message.setText(
                "Dear User,\n\n" +
                "You have requested to reset your password for your AutoLanka account.\n\n" +
                "Please click the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you did not request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "AutoLanka Team"
            );
            
            // Display reset link in console for testing purposes
            System.out.println("===============================================");
            System.out.println("PASSWORD RESET LINK (FOR TESTING):");
            System.out.println(resetLink);
            System.out.println("===============================================");
            
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", to);
            System.out.println("Password reset email sent successfully to: " + to);
        } catch (MailException e) {
            logger.error("Failed to send password reset email to: {}", to, e);
            System.err.println("===============================================");
            System.err.println("FAILED TO SEND PASSWORD RESET EMAIL");
            System.err.println("Recipient: " + to);
            System.err.println("Error: " + e.getMessage());
            System.err.println("===============================================");
            
            // Still display the reset link in console for testing when email fails
            System.out.println("===============================================");
            System.out.println("PASSWORD RESET LINK (FOR TESTING - EMAIL FAILED):");
            System.out.println("http://localhost:8080/reset-password?token=" + token);
            System.out.println("===============================================");
            
            // Don't throw exception to avoid breaking the forgot password flow
        } catch (Exception e) {
            logger.error("Unexpected error sending password reset email to: {}", to, e);
            System.err.println("===============================================");
            System.err.println("UNEXPECTED ERROR SENDING PASSWORD RESET EMAIL");
            System.err.println("Recipient: " + to);
            System.err.println("Error: " + e.getMessage());
            System.err.println("===============================================");
            
            // Still display the reset link in console for testing when email fails
            System.out.println("===============================================");
            System.out.println("PASSWORD RESET LINK (FOR TESTING - EMAIL FAILED):");
            System.out.println("http://localhost:8080/reset-password?token=" + token);
            System.out.println("===============================================");
        }
    }
}