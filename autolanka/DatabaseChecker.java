package org.web.autolanka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web.autolanka.entity.Admin;
import org.web.autolanka.entity.User;
import org.web.autolanka.enums.UserStatus;
import org.web.autolanka.enums.UserType;
import org.web.autolanka.repository.AdminRepository;
import org.web.autolanka.repository.UserRepository;
import org.web.autolanka.service.AuthService;
import org.web.autolanka.util.DatabaseConnectionTester;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class DatabaseChecker {
    
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AutoLankaRentals;encrypt=false;trustServerCertificate=true";
        String username = "sa";
        String password = "123";
        
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Establish connection
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database successfully!");
            
            // Check vehicles table
            System.out.println("\n=== VEHICLES TABLE ===");
            String vehicleQuery = "SELECT vehicle_id, vehicle_number, insurance_approval_status, staff_approval_status, approval_status FROM vehicles";
            PreparedStatement vehicleStmt = connection.prepareStatement(vehicleQuery);
            ResultSet vehicleRs = vehicleStmt.executeQuery();
            
            while (vehicleRs.next()) {
                System.out.println("Vehicle ID: " + vehicleRs.getLong("vehicle_id") + 
                                 ", Number: " + vehicleRs.getString("vehicle_number") + 
                                 ", Insurance Status: " + vehicleRs.getString("insurance_approval_status") + 
                                 ", Staff Status: " + vehicleRs.getString("staff_approval_status") + 
                                 ", Overall Status: " + vehicleRs.getString("approval_status"));
            }
            
            // Check insurance officer vehicle approvals table
            System.out.println("\n=== INSURANCE OFFICER VEHICLE APPROVALS TABLE ===");
            String approvalQuery = "SELECT approval_id, vehicle_id, officer_id, approval_status, approval_date FROM insurance_officer_vehicle_approvals";
            PreparedStatement approvalStmt = connection.prepareStatement(approvalQuery);
            ResultSet approvalRs = approvalStmt.executeQuery();
            
            while (approvalRs.next()) {
                System.out.println("Approval ID: " + approvalRs.getLong("approval_id") + 
                                 ", Vehicle ID: " + approvalRs.getLong("vehicle_id") + 
                                 ", Officer ID: " + approvalRs.getLong("officer_id") + 
                                 ", Status: " + approvalRs.getString("approval_status") + 
                                 ", Date: " + approvalRs.getTimestamp("approval_date"));
            }
            
            // Check insurance officers
            System.out.println("\n=== INSURANCE OFFICERS TABLE ===");
            String officerQuery = "SELECT officer_id, user_id, officer_code FROM insurance_officers";
            PreparedStatement officerStmt = connection.prepareStatement(officerQuery);
            ResultSet officerRs = officerStmt.executeQuery();
            
            while (officerRs.next()) {
                System.out.println("Officer ID: " + officerRs.getLong("officer_id") + 
                                 ", User ID: " + officerRs.getLong("user_id") + 
                                 ", Officer Code: " + officerRs.getString("officer_code"));
            }
            
            // Check staff members
            System.out.println("\n=== STAFF MEMBERS TABLE ===");
            String staffQuery = "SELECT staff_id, user_id, staff_code, department, position FROM staff_members";
            PreparedStatement staffStmt = connection.prepareStatement(staffQuery);
            ResultSet staffRs = staffStmt.executeQuery();
            
            boolean hasStaff = false;
            while (staffRs.next()) {
                hasStaff = true;
                System.out.println("Staff ID: " + staffRs.getLong("staff_id") + 
                                 ", User ID: " + staffRs.getLong("user_id") + 
                                 ", Staff Code: " + staffRs.getString("staff_code") +
                                 ", Department: " + staffRs.getString("department") +
                                 ", Position: " + staffRs.getString("position"));
            }
            
            if (!hasStaff) {
                System.out.println("No staff members found in staff_members table");
            }
            
            // Check users
            System.out.println("\n=== USERS TABLE (STAFF and INSURANCE_OFFICER) ===");
            String userQuery = "SELECT user_id, username, user_type, first_name, last_name FROM users WHERE user_type IN ('STAFF', 'INSURANCE_OFFICER')";
            PreparedStatement userStmt = connection.prepareStatement(userQuery);
            ResultSet userRs = userStmt.executeQuery();
            
            while (userRs.next()) {
                System.out.println("User ID: " + userRs.getLong("user_id") + 
                                 ", Username: " + userRs.getString("username") + 
                                 ", Type: " + userRs.getString("user_type") + 
                                 ", Name: " + userRs.getString("first_name") + " " + userRs.getString("last_name"));
            }
            
            // Check if staff users have entries in staff_members table
            System.out.println("\n=== STAFF USERS WITHOUT STAFF_MEMBER ENTRIES ===");
            String missingStaffQuery = "SELECT u.user_id, u.username, u.first_name, u.last_name FROM users u WHERE u.user_type = 'STAFF' AND NOT EXISTS (SELECT 1 FROM staff_members sm WHERE sm.user_id = u.user_id)";
            PreparedStatement missingStaffStmt = connection.prepareStatement(missingStaffQuery);
            ResultSet missingStaffRs = missingStaffStmt.executeQuery();
            
            boolean hasMissingStaff = false;
            while (missingStaffRs.next()) {
                hasMissingStaff = true;
                System.out.println("Missing Staff Entry - User ID: " + missingStaffRs.getLong("user_id") + 
                                 ", Username: " + missingStaffRs.getString("username") + 
                                 ", Name: " + missingStaffRs.getString("first_name") + " " + missingStaffRs.getString("last_name"));
            }
            
            if (!hasMissingStaff) {
                System.out.println("All staff users have corresponding staff_member entries");
            }
            
            // Close connections
            vehicleRs.close();
            vehicleStmt.close();
            approvalRs.close();
            approvalStmt.close();
            officerRs.close();
            officerStmt.close();
            staffRs.close();
            staffStmt.close();
            userRs.close();
            userStmt.close();
            missingStaffRs.close();
            missingStaffStmt.close();
            connection.close();
            
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private DatabaseConnectionTester connectionTester;

    @PostConstruct
    public void init() {
        // Test database connection
        if (!connectionTester.testConnection()) {
            System.err.println("Database connection failed. Please check your database configuration.");
            return;
        }
        // Check if admin user exists
        User existingAdmin = userRepository.findByUsernameAndUserType("Parindi", UserType.ADMIN);
        if (existingAdmin == null) {
            // Create default admin user
            User adminUser = new User();
            adminUser.setUsername("Parindi");
            adminUser.setEmail("admin@autolanka.com");
            adminUser.setPasswordHash("$2a$10$rJ5lNhvt8JKYHUjvRQ4Lq.nN9T0wMaXf5zHk8aG4dSBKKkE8jNnKm"); // Hashed value for '1234'
            adminUser.setFirstName("Parindi");
            adminUser.setLastName("Admin");
            adminUser.setPhone("+94771234567");
            adminUser.setUserType(UserType.ADMIN);
            adminUser.setStatus(UserStatus.ACTIVE);
            adminUser.setCreatedDate(LocalDateTime.now());
            
            // Save the user first
            User savedUser = userRepository.save(adminUser);
            
            // Create admin record
            Admin admin = new Admin();
            admin.setUser(savedUser);
            admin.setAdminCode("ADM-001");
            admin.setAdminLevel("Super Admin");
            admin.setDepartment("IT & Systems");
            admin.setAuthorizationLevel("Full Access");
            
            adminRepository.save(admin);
            
            System.out.println("Database initialization completed - Default admin user 'Parindi' created with password '1234'");
        } else {
            System.out.println("Admin user 'Parindi' already exists in the database.");
        }
    }
}