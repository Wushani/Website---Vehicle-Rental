// AuthServiceImpl.java
package org.web.autolanka.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.autolanka.dto.ForgotPasswordRequestDTO;
import org.web.autolanka.dto.LoginRequestDTO;
import org.web.autolanka.dto.RegistrationRequestDTO;
import org.web.autolanka.dto.UserResponseDTO;
import org.web.autolanka.entity.Admin;
import org.web.autolanka.entity.Customer;
import org.web.autolanka.entity.FinancialExecutive;
import org.web.autolanka.entity.InsuranceOfficer;
import org.web.autolanka.entity.PasswordResetToken;
import org.web.autolanka.entity.StaffMember;
import org.web.autolanka.entity.User;
import org.web.autolanka.entity.VehicleOwner;
import org.web.autolanka.enums.UserStatus;
import org.web.autolanka.enums.UserType;
import org.web.autolanka.repository.AdminRepository;
import org.web.autolanka.repository.CustomerRepository;
import org.web.autolanka.repository.FinancialExecutiveRepository;
import org.web.autolanka.repository.InsuranceOfficerRepository;
import org.web.autolanka.repository.PasswordResetTokenRepository;
import org.web.autolanka.repository.StaffMemberRepository;
import org.web.autolanka.repository.UserRepository;
import org.web.autolanka.repository.VehicleOwnerRepository;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.service.EmailService;

@Service
public class AuthServiceImpl implements AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private VehicleOwnerRepository vehicleOwnerRepository;
    
    @Autowired
    private StaffMemberRepository staffMemberRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private InsuranceOfficerRepository insuranceOfficerRepository;
    
    @Autowired
    private FinancialExecutiveRepository financialExecutiveRepository;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public UserResponseDTO authenticateUser(LoginRequestDTO loginRequest, UserType userType) {
        User user = findUserByUsernameAndType(loginRequest.getUsername(), userType);
        
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getUserType()
            );
        }
        return null;
    }

    @Override
    @Transactional
    public String registerUser(RegistrationRequestDTO registrationRequest, UserType userType) {
        // Validate role-specific constraints
        validateRoleSpecificConstraints(registrationRequest, userType);
        
        // Password confirmation validation
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        
        // Create the main user entity
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPhone(registrationRequest.getPhone());
        user.setAddress(registrationRequest.getAddress());
        user.setUserType(userType);
        user.setStatus(UserStatus.ACTIVE);
        
        // Save the user
        User savedUser = userRepository.save(user);
        
        // Create role-specific record
        createRoleSpecificRecord(savedUser, registrationRequest, userType);
        
        return savedUser.getUsername();
    }

    @Override
    public void logout(Long userId) {
        // Implementation for logout logic if needed
        // Could be used for audit trails
    }

    @Override
    @Transactional
    public boolean deleteUser(String identifier, String password, UserType userType) {
        User user = findUserByUsernameAndType(identifier, userType);
        
        // Verify password before deleting
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            // Delete role-specific record first
            deleteRoleSpecificRecord(user, userType);
            
            // Delete the main user record
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean resetPassword(String identifier, String currentPassword, String newPassword, UserType userType) {
        User user = findUserByUsernameAndType(identifier, userType);
        
        // Verify current password before updating
        if (user != null && passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            // Update with new password
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequest, UserType userType) {
        try {
            System.out.println("===============================================");
            System.out.println("PROCESSING FORGOT PASSWORD REQUEST");
            System.out.println("Email: " + forgotPasswordRequest.getEmail());
            System.out.println("User Type: " + userType);
            System.out.println("===============================================");
            
            logger.info("Processing forgot password request for email: {} and user type: {}", forgotPasswordRequest.getEmail(), userType);
            
            // Find user by email and user type
            User user = userRepository.findByEmailAndUserType(forgotPasswordRequest.getEmail(), userType);
            
            if (user != null) {
                System.out.println("Found user with email " + forgotPasswordRequest.getEmail() + " and type " + userType + " for password reset");
                logger.info("Found user with email {} and type {} for password reset", forgotPasswordRequest.getEmail(), userType);
                
                // Delete any existing tokens for this user
                System.out.println("Deleting existing tokens for user ID: " + user.getUserId());
                passwordResetTokenRepository.deleteByUser_UserId(user.getUserId());
                
                // Generate a new token
                String token = UUID.randomUUID().toString();
                System.out.println("Generated reset token: " + token);
                
                // Set expiry date to 1 hour from now
                LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);
                System.out.println("Token expiry date: " + expiryDate);
                
                // Create and save the password reset token
                PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, expiryDate);
                System.out.println("Saving password reset token for user: " + user.getEmail());
                passwordResetTokenRepository.save(passwordResetToken);
                System.out.println("Password reset token saved successfully");
                
                // Send email with reset link
                System.out.println("Sending password reset email to: " + user.getEmail());
                emailService.sendPasswordResetEmail(user.getEmail(), token);
                
                System.out.println("===============================================");
                System.out.println("FORGOT PASSWORD PROCESS COMPLETED SUCCESSFULLY");
                System.out.println("Email sent to: " + user.getEmail());
                System.out.println("===============================================");
                
                return true;
            } else {
                System.out.println("No user found with email " + forgotPasswordRequest.getEmail() + " and type " + userType + " for password reset");
                logger.warn("No user found with email {} and type {} for password reset", forgotPasswordRequest.getEmail(), userType);
            }
            return false;
        } catch (Exception e) {
            System.err.println("===============================================");
            System.err.println("ERROR PROCESSING FORGOT PASSWORD REQUEST");
            System.err.println("Email: " + forgotPasswordRequest.getEmail());
            System.err.println("User Type: " + userType);
            System.err.println("Error: " + e.getMessage());
            System.err.println("===============================================");
            e.printStackTrace();
            logger.error("Error processing forgot password request for email: {} and user type: {}", 
                        forgotPasswordRequest.getEmail(), userType, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean resetPasswordWithToken(String token, String newPassword) {
        try {
            System.out.println("===============================================");
            System.out.println("PROCESSING PASSWORD RESET WITH TOKEN");
            System.out.println("Token: " + token);
            System.out.println("===============================================");
            
            logger.info("Processing password reset with token: {}", token);
            
            // Find the token
            Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
            
            if (tokenOptional.isPresent()) {
                PasswordResetToken passwordResetToken = tokenOptional.get();
                System.out.println("Found password reset token for user ID: " + passwordResetToken.getUser().getUserId());
                
                // Check if token is expired
                if (passwordResetToken.isExpired()) {
                    // Delete expired token
                    System.out.println("Token is expired, deleting token");
                    passwordResetTokenRepository.delete(passwordResetToken);
                    logger.warn("Expired password reset token used: {}", token);
                    return false;
                }
                
                // Get the user
                User user = passwordResetToken.getUser();
                System.out.println("Resetting password for user: " + user.getEmail());
                
                // Update password
                user.setPasswordHash(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                System.out.println("Password updated successfully for user: " + user.getEmail());
                
                // Delete the token
                passwordResetTokenRepository.delete(passwordResetToken);
                System.out.println("Password reset token deleted");
                
                System.out.println("===============================================");
                System.out.println("PASSWORD RESET COMPLETED SUCCESSFULLY");
                System.out.println("User: " + user.getEmail());
                System.out.println("===============================================");
                
                logger.info("Password successfully reset for user: {}", user.getEmail());
                return true;
            } else {
                System.out.println("Invalid password reset token: " + token);
                logger.warn("Invalid password reset token used: {}", token);
            }
            return false;
        } catch (Exception e) {
            System.err.println("===============================================");
            System.err.println("ERROR PROCESSING PASSWORD RESET WITH TOKEN");
            System.err.println("Token: " + token);
            System.err.println("Error: " + e.getMessage());
            System.err.println("===============================================");
            e.printStackTrace();
            logger.error("Error processing password reset with token: {}", token, e);
            return false;
        }
    }
    
    @Override
    public boolean validatePasswordResetToken(String token) {
        try {
            System.out.println("Validating password reset token: " + token);
            logger.info("Validating password reset token: {}", token);
            
            Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
            
            if (tokenOptional.isPresent()) {
                PasswordResetToken passwordResetToken = tokenOptional.get();
                boolean isValid = !passwordResetToken.isExpired();
                System.out.println("Password reset token " + token + " is valid: " + isValid);
                logger.info("Password reset token {} is valid: {}", token, isValid);
                return isValid;
            } else {
                System.out.println("Password reset token not found: " + token);
                logger.warn("Password reset token not found: {}", token);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error validating password reset token: " + token);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            logger.error("Error validating password reset token: {}", token, e);
            return false;
        }
    }
    
    @Override
    public Optional<VehicleOwner> findVehicleOwnerById(Long ownerId) {
        return vehicleOwnerRepository.findWithUserById(ownerId);
    }
    
    @Override
    public Optional<VehicleOwner> findVehicleOwnerByUserId(Long userId) {
        return vehicleOwnerRepository.findByUserId(userId);
    }
    
    @Override
    public Optional<Customer> findCustomerByUserId(Long userId) {
        return customerRepository.findByUserId(userId);
    }

    private User findUserByUsernameAndType(String username, UserType userType) {
        // Try to find by username first
        User user = userRepository.findByUsernameAndUserType(username, userType);
        
        // If not found and the input looks like an email, try to find by email
        if (user == null && username.contains("@")) {
            user = userRepository.findByEmailAndUserType(username, userType);
        }
        
        return user;
    }
    
    private void validateRoleSpecificConstraints(RegistrationRequestDTO request, UserType userType) {
        switch (userType) {
            case CUSTOMER:
                if (request.getNic() != null && customerRepository.existsByNic(request.getNic())) {
                    throw new RuntimeException("A customer with this NIC already exists");
                }
                break;
            case VEHICLE_OWNER:
                if (request.getNic() != null && vehicleOwnerRepository.existsByNic(request.getNic())) {
                    throw new RuntimeException("A vehicle owner with this NIC already exists");
                }
                break;
            case STAFF:
                if (request.getSpecialCode() != null && staffMemberRepository.existsByStaffCode(request.getSpecialCode())) {
                    throw new RuntimeException("A staff member with this staff code already exists");
                }
                break;
            case INSURANCE_OFFICER:
                if (request.getSpecialCode() != null && insuranceOfficerRepository.existsByOfficerCode(request.getSpecialCode())) {
                    throw new RuntimeException("An insurance officer with this officer code already exists");
                }
                break;
            case FINANCIAL_EXECUTIVE:
                if (request.getSpecialCode() != null && financialExecutiveRepository.existsByEmployeeCode(request.getSpecialCode())) {
                    throw new RuntimeException("A financial executive with this employee code already exists");
                }
                break;
            case ADMIN:
                if (request.getSpecialCode() != null && adminRepository.existsByAdminCode(request.getSpecialCode())) {
                    throw new RuntimeException("An admin with this admin code already exists");
                }
                break;
        }
    }
    
    private void createRoleSpecificRecord(User user, RegistrationRequestDTO request, UserType userType) {
        switch (userType) {
            case CUSTOMER:
                Customer customer = new Customer(user, request.getNic(), request.getDrivingLicense());
                customerRepository.save(customer);
                break;
                
            case VEHICLE_OWNER:
                VehicleOwner vehicleOwner = new VehicleOwner(user, request.getNic(), request.getBusinessName());
                vehicleOwner.setDrivingLicense(request.getDrivingLicense());
                vehicleOwnerRepository.save(vehicleOwner);
                break;
                
            case STAFF:
                StaffMember staffMember = new StaffMember(user, request.getSpecialCode(), 
                                                         request.getDepartment(), request.getPosition());
                staffMemberRepository.save(staffMember);
                break;
                
            case INSURANCE_OFFICER:
                InsuranceOfficer insuranceOfficer = new InsuranceOfficer(user, request.getSpecialCode(), 
                                                                         request.getInsuranceCompany(), request.getLicenseNumber());
                insuranceOfficerRepository.save(insuranceOfficer);
                break;
                
            case FINANCIAL_EXECUTIVE:
                FinancialExecutive financialExecutive = new FinancialExecutive(user, request.getSpecialCode(), 
                                                                              request.getDepartment(), request.getFinancialQualification(), 
                                                                              request.getExperienceYears());
                financialExecutiveRepository.save(financialExecutive);
                break;
                
            case ADMIN:
                Admin admin = new Admin(user, request.getSpecialCode(), request.getAdminLevel(), 
                                       request.getDepartment(), request.getAuthorizationLevel());
                adminRepository.save(admin);
                break;
        }
    }
    
    private void deleteRoleSpecificRecord(User user, UserType userType) {
        switch (userType) {
            case CUSTOMER:
                customerRepository.deleteByUserId(user.getUserId());
                break;
                
            case VEHICLE_OWNER:
                vehicleOwnerRepository.deleteByUserId(user.getUserId());
                break;
                
            case STAFF:
                staffMemberRepository.deleteByUserId(user.getUserId());
                break;
                
            case INSURANCE_OFFICER:
                insuranceOfficerRepository.deleteByUserId(user.getUserId());
                break;
                
            case FINANCIAL_EXECUTIVE:
                financialExecutiveRepository.deleteByUserId(user.getUserId());
                break;
                
            case ADMIN:
                adminRepository.deleteByUserId(user.getUserId());
                break;
        }
    }
}