// PasswordEncoderUtil.java
package org.web.autolanka.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Encode password "789"
        String encoded789 = encoder.encode("789");
        System.out.println("BCrypt hash for '789': " + encoded789);
        
        // Encode password "admin123" 
        String encodedAdmin = encoder.encode("admin123");
        System.out.println("BCrypt hash for 'admin123': " + encodedAdmin);
        
        // Verify the encoding works
        System.out.println("Verification '789': " + encoder.matches("789", encoded789));
        System.out.println("Verification 'admin123': " + encoder.matches("admin123", encodedAdmin));
    }
}