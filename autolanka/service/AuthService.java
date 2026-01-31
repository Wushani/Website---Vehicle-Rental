// AuthService.java
package org.web.autolanka.service;

import java.util.Optional;

import org.web.autolanka.dto.ForgotPasswordRequestDTO;
import org.web.autolanka.dto.LoginRequestDTO;
import org.web.autolanka.dto.RegistrationRequestDTO;
import org.web.autolanka.dto.UserResponseDTO;
import org.web.autolanka.entity.Customer;
import org.web.autolanka.entity.VehicleOwner;
import org.web.autolanka.enums.UserType;

public interface AuthService {
    UserResponseDTO authenticateUser(LoginRequestDTO loginRequest, UserType userType);
    String registerUser(RegistrationRequestDTO registrationRequest, UserType userType);
    void logout(Long userId);
    boolean deleteUser(String identifier, String password, UserType userType);
    boolean resetPassword(String identifier, String currentPassword, String newPassword, UserType userType);
    
    // New methods for forgot password functionality
    boolean forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequest, UserType userType);
    boolean resetPasswordWithToken(String token, String newPassword);
    boolean validatePasswordResetToken(String token);
    
    // New method to find vehicle owner by ID
    Optional<VehicleOwner> findVehicleOwnerById(Long ownerId);
    
    // Method to find vehicle owner by User ID
    Optional<VehicleOwner> findVehicleOwnerByUserId(Long userId);
    
    // Method to find customer by User ID
    Optional<Customer> findCustomerByUserId(Long userId);
}